package de.tudresden.slr.metainformation.editors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import de.tudresden.slr.metainformation.MetainformationActivator;
import de.tudresden.slr.metainformation.data.Author;
import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;
import de.tudresden.slr.metainformation.util.DataProvider;
import de.tudresden.slr.metainformation.util.MetainformationUtil;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;

public class MetainformationEditor extends EditorPart implements IEditorPart {

	private SlrProjectMetainformation metainformation;
	private boolean dirty = false;
	private IPath activeFilePath;
	private Text textboxFile, textboxTitle, textboxKeywords, textboxAbstract, textboxDescriptionTaxonomy;
	private Text textboxMainDimensions, textboxDimensions, textboxDocuments;
	private Button buttonAddAuthor, buttonEditAuthor, buttonDeleteAuthor;

	// AdapterFactory and EditingDomain for loading of resources, copied and adapted
	// from de.tudresden.slr.model.bibtex.ui.presentation.BibtexEntryView
	protected AdapterFactory adapterFactory;
	protected AdapterFactoryEditingDomain editingDomain;

	private org.eclipse.swt.widgets.List listAuthorsList;
	// Dummy author to ensure consistency of metainformation storage
	private Author dummyAuthor = new Author("John Doe", "johnd@mail.tld", "University of XYZ");
	private DataProvider dataProvider;

	public MetainformationEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			// marshalling of metainformation object to xml data
			File file = new File(activeFilePath.toOSString());
			JAXBContext jaxbContext = JAXBContext.newInstance(SlrProjectMetainformation.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			SlrProjectMetainformation toSave = new SlrProjectMetainformation();
			toSave.setKeywords(textboxKeywords.getText());
			toSave.setProjectAbstract(textboxAbstract.getText());
			toSave.setTaxonomyDescription(textboxDescriptionTaxonomy.getText());
			toSave.setTitle(textboxTitle.getText());

			// authors list must not be empty or null, insert dummy when it is null/empty
			if (metainformation.getAuthorsList() == null || metainformation.getAuthorsList().isEmpty()) {
				List<Author> l = new ArrayList<Author>();
				l.add(dummyAuthor);
				metainformation.setAuthorsList(l);
			}
			toSave.setAuthorsList(metainformation.getAuthorsList());

			jaxbMarshaller.marshal(toSave, file);

			setDirty(false);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (Exception e) {

		}
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		
		//to avoid errors, close editor, when project is not open
		//use case : project deleted
		if(!getProjectFromIEditorPart(this).isOpen()) {
			this.getEditorSite().getPage().closeEditor(this, false);
		}

		// initialize whole project
		unloadDocumentResources();
		initializeWholeProject(getProjectFromIEditorPart(this));

		// set filename as displayed tab name
		activeFilePath = ((IFileEditorInput) input).getFile().getLocation();
		String pathString = activeFilePath.toOSString();
		this.setPartName(pathString.substring(pathString.lastIndexOf(File.separator) + 1));

		activeFilePath = ((IFileEditorInput) input).getFile().getLocation();
	}

	/**
	 * Initializes the editor's form fields. Unmarshalls XML object to
	 * metainformation object and sets both path and object in the plugin's
	 * singleton.
	 */
	public void initFormFields() {
		try {
			File file = new File(activeFilePath.toOSString());
			metainformation = MetainformationUtil.getMetainformationFromFile(file);

			MetainformationActivator.setMetainformation(metainformation);
			MetainformationActivator.setCurrentFilepath(activeFilePath.toOSString());

			textboxTitle.setText(metainformation.getTitle());
			textboxKeywords.setText(metainformation.getKeywords());
			textboxDescriptionTaxonomy.setText(metainformation.getTaxonomyDescription());
			textboxAbstract.setText(metainformation.getProjectAbstract());
			textboxFile.setText(activeFilePath.toOSString());

			initializeKeyStatistics();

			redrawAuthorsList();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	protected void setDirty(boolean dirty) {
		if (this.dirty != dirty) {
			this.dirty = dirty;
			this.firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		ScrolledComposite sc = new ScrolledComposite(parent, SWT.V_SCROLL);
		sc.setLayout(new RowLayout());

		Composite masterComposite = new Composite(sc, SWT.NONE);

		sc.setContent(masterComposite);

		// multipurpose 2 column layout for all form elements
		GridLayout gridLayout2Columns = new GridLayout();
		gridLayout2Columns.numColumns = 2;

		masterComposite.setLayout(new GridLayout(1, false));

		GridData gridDataExpand = new GridData();
		gridDataExpand.grabExcessHorizontalSpace = true;
		gridDataExpand.horizontalAlignment = SWT.FILL;

		// multipurpose GridData for Groups, specifies that they should take a whole
		// "row", not just one
		// half of the grid due to 2 columns layout
		GridData gridDataGroups = new GridData(SWT.BEGINNING, SWT.BEGINNING, true, true);
		gridDataGroups.horizontalSpan = 2;
		gridDataGroups.verticalAlignment = SWT.BEGINNING;
		gridDataGroups.grabExcessHorizontalSpace = true;
		gridDataGroups.horizontalAlignment = SWT.FILL;

		Group keyFactsGroup = new Group(masterComposite, SWT.NONE);
		keyFactsGroup.setText("Key facts");
		keyFactsGroup.setLayout(gridLayout2Columns);
		keyFactsGroup.setLayoutData(gridDataGroups);

		// Labels and Textboxes for key facts group
		new Label(keyFactsGroup, SWT.NONE).setText("File");
		textboxFile = new Text(keyFactsGroup, SWT.BORDER);
		textboxFile.setEditable(false);
		textboxFile.setLayoutData(gridDataExpand);

		new Label(keyFactsGroup, SWT.NONE).setText("Title");
		textboxTitle = new Text(keyFactsGroup, SWT.BORDER);
		textboxTitle.setLayoutData(gridDataExpand);

		new Label(keyFactsGroup, SWT.NONE).setText("Keywords");
		textboxKeywords = new Text(keyFactsGroup, SWT.BORDER);
		textboxKeywords.setLayoutData(gridDataExpand);

		// start statistics group
		GridData gridDataStatisticsGroup = new GridData(SWT.BEGINNING, SWT.BEGINNING, true, true);
		gridDataStatisticsGroup.horizontalSpan = 2;
		gridDataStatisticsGroup.verticalAlignment = SWT.BEGINNING;
		gridDataStatisticsGroup.grabExcessHorizontalSpace = true;
		gridDataStatisticsGroup.horizontalAlignment = SWT.FILL;
		Group statisticsGroup = new Group(keyFactsGroup, SWT.NONE);
		statisticsGroup.setText("Key statistics");
		statisticsGroup.setLayout(gridLayout2Columns);
		statisticsGroup.setLayoutData(gridDataStatisticsGroup);

		Button buttonRefresh = new Button(statisticsGroup, SWT.NONE);
		buttonRefresh.setText("Refresh");
		Label textRefresh = new Label(statisticsGroup, SWT.NONE);
		textRefresh.setText("Push \"Refresh\" to display the current key statistics.");

		new Label(statisticsGroup, SWT.NONE).setText("Number of main dimensions");
		textboxMainDimensions = new Text(statisticsGroup, SWT.BORDER);
		textboxMainDimensions.setEditable(false);
		textboxMainDimensions.setLayoutData(gridDataExpand);

		new Label(statisticsGroup, SWT.NONE).setText("Number of dimensions");
		textboxDimensions = new Text(statisticsGroup, SWT.BORDER);
		textboxDimensions.setEditable(false);
		textboxDimensions.setLayoutData(gridDataExpand);

		new Label(statisticsGroup, SWT.NONE).setText("Number of documents");
		textboxDocuments = new Text(statisticsGroup, SWT.BORDER);
		textboxDocuments.setEditable(false);
		textboxDocuments.setLayoutData(gridDataExpand);

		// empty label for space vertical
		new Label(statisticsGroup, SWT.NONE);

		buttonRefresh.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection: {
					dataProvider = new DataProvider();

					int mainDimensionCount = dataProvider.getMainDimensions().size();
					textboxMainDimensions.setText(String.valueOf(mainDimensionCount));

					int dimensionsTotal = dataProvider.getAllDimensionsOrdered().size();
					textboxDimensions.setText(String.valueOf(dimensionsTotal));

					int documents = dataProvider.getNumberOfDocuments();
					textboxDocuments.setText(String.valueOf(documents));

				}
					break;
				}
			}
		});
		// end statistics group

		// begin authorsgroup
		Group authorsGroup = new Group(keyFactsGroup, SWT.NONE);
		authorsGroup.setText("Authors");
		authorsGroup.setLayout(new RowLayout());
		GridData gridDataAuthorsGroup = new GridData(SWT.BEGINNING, SWT.BEGINNING, true, true);
		gridDataAuthorsGroup.horizontalSpan = 2;
		authorsGroup.setLayoutData(gridDataAuthorsGroup);

		buttonAddAuthor = new Button(authorsGroup, 0);
		buttonAddAuthor.setText("Add");

		buttonEditAuthor = new Button(authorsGroup, 0);
		buttonEditAuthor.setText("Edit");

		buttonDeleteAuthor = new Button(authorsGroup, 0);
		buttonDeleteAuthor.setText("Delete");

		// List is initialized later on, because initialization of form fields needs to
		// be done first
		listAuthorsList = new org.eclipse.swt.widgets.List(authorsGroup, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		int listAuthorsListWidth = 1000; // 1000px default width
		RowData rowDataAuthorsList = new RowData(listAuthorsListWidth, 100);

		// UNUSED: Listener for list's parent to determine size of list dynamically.
		// RowData can't (?) set expand
		// horizontally, so this approach is used
		// authorsGroup.addControlListener(new ControlListener() {
		// public void controlResized(ControlEvent e) {
		// double widthFactor = 0.98;
		// int currentWidth = (int) (widthFactor*parent.getSize().x);
		// System.out.println("new width " + currentWidth);
		// rowDataAuthorsList.width = currentWidth;
		// listAuthorsList.setLayoutData(rowDataAuthorsList);
		// }
		//
		// @Override
		// public void controlMoved(ControlEvent e) {
		// }
		// });
		listAuthorsList.setLayoutData(rowDataAuthorsList);

		// empty label for space vertical
		new Label(statisticsGroup, SWT.NONE);
		// end authors group

		// begin description group
		Group descriptionGroup = new Group(masterComposite, SWT.NONE);
		descriptionGroup.setText("Description");
		descriptionGroup.setLayout(gridLayout2Columns);
		descriptionGroup.setLayoutData(gridDataGroups);

		// grid data for textboxes abstract & description
		GridData gridDataBigTextboxes = new GridData();
		gridDataBigTextboxes.grabExcessHorizontalSpace = true;
		gridDataBigTextboxes.horizontalAlignment = SWT.FILL;
		gridDataBigTextboxes.heightHint = 100;

		new Label(descriptionGroup, SWT.NONE).setText("Abstract");
		textboxAbstract = new Text(descriptionGroup, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		textboxAbstract.setLayoutData(gridDataBigTextboxes);

		new Label(descriptionGroup, SWT.NONE).setText("Description of \n the taxonomy");
		textboxDescriptionTaxonomy = new Text(descriptionGroup, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		textboxDescriptionTaxonomy.setLayoutData(gridDataBigTextboxes);
		// end description group

		// default width initialized with 1000px
		masterComposite.setSize(masterComposite.computeSize(1000, SWT.DEFAULT));
		int masterCompositeInitHeight = masterComposite.getSize().y;

		//resize listener has to be in here due to access on composites
		parent.addControlListener(new ControlListener() {
			public void controlResized(ControlEvent e) {
				// setSize of masterComposize to new width and height at initialization time
				// new width is (1-widthFactor) * old parent width. If widthFactor == 1,
				// scrollbars
				// from Textboxes would overlap with scrollbar from whole editor
				double widthFactor = 0.98;
				int newWidth = (int) (widthFactor * parent.getSize().x);
				masterComposite.setSize(parent.computeSize(newWidth, masterCompositeInitHeight));
			}

			@Override
			public void controlMoved(ControlEvent e) {
			}
		});
		
		initFormFields();
		addListeners();

	}
	
	private void addListeners() {
		// Listener for author group buttons
		buttonAddAuthor.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection: {
					NewAndEditAuthorDialog dialog = new NewAndEditAuthorDialog(
							PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
					dialog.create();
					if (dialog.open() == Window.OK) {
						Author newAuthor = new Author(dialog.getName(), dialog.getMail(), dialog.getAffiliation());
						metainformation.getAuthorsList().add(newAuthor);
						redrawAuthorsList();
						setDirty(true);
					}
				}
					break;
				}
			}
		});

		// Listener for edit button
		buttonEditAuthor.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection: {
					int editIndex = listAuthorsList.getSelectionIndex();
					if (editIndex < 0)
						break;

					NewAndEditAuthorDialog dialog = new NewAndEditAuthorDialog(
							PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
					Author authorToEdit = metainformation.getAuthorsList().get(editIndex);
					dialog.initEditAuthor(authorToEdit);
					dialog.create();

					if (dialog.open() == Window.OK) {
						Author editedAuthor = new Author(dialog.getName(), dialog.getMail(), dialog.getAffiliation());
						metainformation.getAuthorsList().set(editIndex, editedAuthor);
						redrawAuthorsList();
						setDirty(true);
					}
				}
					break;
				}
			}
		});

		// listener for delete button
		buttonDeleteAuthor.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection: {
					if (listAuthorsList.getSelectionIndex() < 0)
						break;

					metainformation.getAuthorsList().remove(listAuthorsList.getSelectionIndex());

					if (metainformation.getAuthorsList().isEmpty()) {
						MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
								"Info", "List of authors must not be empty. A dummy will be inserted.");
						metainformation.getAuthorsList().add(dummyAuthor);
					}
					setDirty(true);
					redrawAuthorsList();
				}
					break;
				}
			}
		});

		// generic modifiy-listener, which sets dirty flag, when something is changed.
		ModifyListener modifiedDirty = new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				setDirty(true);
			}
		};

		// add generic modify listener to textboxes
		textboxTitle.addModifyListener(modifiedDirty);
		textboxKeywords.addModifyListener(modifiedDirty);
		textboxAbstract.addModifyListener(modifiedDirty);
		textboxDescriptionTaxonomy.addModifyListener(modifiedDirty);

	}

	/**
	 * Redraws the list of authors. Needed, when an author was added, edited or
	 * deleted.
	 */
	protected void redrawAuthorsList() {
		if (listAuthorsList.getItems().length != 0) {
			listAuthorsList.removeAll();
		}
		for (Author a : metainformation.getAuthorsList()) {
			listAuthorsList.add(a.toString());
		}
	}

	/**
	 * Sets bibtex documents and taxonomy input argument's resources as active
	 * elements in ModelRegistry
	 * 
	 * @param project
	 *            project whose workspace is searched for resources whose file
	 *            extensions are checked. Matching .taxonomy and .bib-files are
	 *            loaded in the ModelRegistry
	 */
	private void initializeWholeProject(IProject project) {
		initializeEditingDomain();

		if (project.isOpen()) {
			IResource[] resources = null;
			try {
				resources = project.members();
			} catch (CoreException e) {
				e.printStackTrace();
				return;
			}
			for (IResource res : resources) {
				if (res.getType() == IResource.FILE && "bib".equals(res.getFileExtension())) {
					URI uri = URI.createURI(((IFile) res).getFullPath().toString());
					editingDomain.getResourceSet().getResource(uri, true);
				} else if (res.getType() == IResource.FILE && "taxonomy".equals(res.getFileExtension())) {
					ModelRegistryPlugin.getModelRegistry().setTaxonomyFile((IFile) res);

				}
			}
		}
	}

	// copied and adapted from
	// de.tudresden.slr.model.bibtex.ui.presentation.BibtexEntryView
	protected void initializeEditingDomain() {
		ModelRegistryPlugin.getModelRegistry().getEditingDomain().ifPresent((domain) -> editingDomain = domain);
		if (editingDomain == null) {
			System.err.println("uninitailised editing domain");
			return;
		}
		adapterFactory = editingDomain.getAdapterFactory();
	}

	/**
	 * Extract the active/selected/current project in which the file from the editor
	 * part lies
	 *
	 * @param editor
	 *            The current editor
	 * @return IProject Project related to the file which is opened in the current
	 *         editor
	 */
	private IProject getProjectFromIEditorPart(IEditorPart editor) {
		IEditorInput input = editor.getEditorInput();
		if (!(input instanceof IFileEditorInput)) {
			return null;
		} else {
			return ((IFileEditorInput) input).getFile().getProject();
		}
	}

	@Override
	public void setFocus() {
		unloadDocumentResources();
		initializeWholeProject(getProjectFromIEditorPart(this));
		initializeKeyStatistics();
	}

	/**
	 * initializes the key statistic text fields
	 */
	private void initializeKeyStatistics() {
		dataProvider = new DataProvider();
		int mainDimensionCount = dataProvider.getMainDimensions().size();
		textboxMainDimensions.setText(String.valueOf(mainDimensionCount));

		int dimensionsTotal = dataProvider.getAllDimensionsOrdered().size();
		textboxDimensions.setText(String.valueOf(dimensionsTotal));

		int documents = dataProvider.getNumberOfDocuments();
		textboxDocuments.setText(String.valueOf(documents));
	}

	private void unloadDocumentResources() {
		if (editingDomain == null)
			return;
		// unload documents prior to loading new ones
		for (Resource resource : editingDomain.getResourceSet().getResources()) {
			resource.unload();
		}
		editingDomain.getResourceSet().getResources().clear();
	}

	/**
	 * Dummy method, can be used to create a non corrupted version of a
	 * metainformation xml document for debugging purposes
	 * 
	 * @param path
	 *            Path to the .xml-File, which is to be generated
	 */
	public void testSave(String path) {
		SlrProjectMetainformation m = new SlrProjectMetainformation();
		m.setTitle("title");
		m.setKeywords("keywords");
		m.setProjectAbstract("abstract");
		m.setTaxonomyDescription("descr");

		Author a = new Author("Name", "Org", "mail");
		Author b = new Author("Nameb", "OrgB", "mailB");
		List<Author> authorList = new ArrayList<Author>();
		authorList.add(a);
		authorList.add(b);
		m.setAuthorsList(authorList);

		try {

			File file = new File(path);
			JAXBContext jaxbContext = JAXBContext.newInstance(SlrProjectMetainformation.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(m, file);

		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

}