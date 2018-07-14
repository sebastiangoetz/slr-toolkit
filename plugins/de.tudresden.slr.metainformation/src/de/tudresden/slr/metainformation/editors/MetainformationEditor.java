package de.tudresden.slr.metainformation.editors;

import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import de.tudresden.slr.metainformation.MetainformationActivator;
import de.tudresden.slr.metainformation.data.Author;
import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;
import de.tudresden.slr.metainformation.util.DataProvider;
import de.tudresden.slr.metainformation.util.MetainformationUtil;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;

public class MetainformationEditor extends EditorPart implements IEditorPart {

	private SlrProjectMetainformation metainformation;
	private boolean dirty = false;
	private IPath activeFilePath;
	private Text textboxFile, textboxTitle, textboxKeywords, textboxAbstract, textboxDescriptionTaxonomy;
	private org.eclipse.swt.widgets.List listAuthorsList;
	private DataProvider dataProvider;
	
	//Dummy author to ensure consistency of metainformation storage
	Author dummyAuthor = new Author("John Doe", "johnd@mail.tld", "University of XYZ");
	
	public  MetainformationEditor() {
		// TODO Auto-generated constructor stub
	}
	
	protected AdapterFactory adapterFactory;
	protected AdapterFactoryEditingDomain editingDomain;
	
//	private void registerResources(IProject project) {
//		IResource[] resources = null;
//		try {
//			resources = project.members();
//		} catch (CoreException e) {
//			e.printStackTrace();
//			return;
//		}
//		for (IResource res : resources) {
//			if (res.getType() == IResource.FILE && "bib".equals(res.getFileExtension())) {
//				URI uri = URI.createURI(((IFile) res).getFullPath().toString());
//				editingDomain.getResourceSet().getResource(uri, true);
//			} else if (res.getType() == IResource.FILE && "taxonomy".equals(res.getFileExtension())){
//				ModelRegistryPlugin.getModelRegistry().setTaxonomyFile((IFile) res);
//			}
//		}
//	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		try {

			File file = new File(activeFilePath.toOSString());
			JAXBContext jaxbContext = JAXBContext.newInstance(SlrProjectMetainformation.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			SlrProjectMetainformation toSave = new SlrProjectMetainformation();
			toSave.setKeywords(textboxKeywords.getText());
			toSave.setProjectAbstract(textboxAbstract.getText());
			toSave.setTaxonomyDescription(textboxDescriptionTaxonomy.getText());
			toSave.setTitle(textboxTitle.getText());
			
			//authors list must not be empty or null, insert dummy when it is null/empty
			if(metainformation.getAuthorsList() == null || metainformation.getAuthorsList().isEmpty()) {
				List<Author> l = new ArrayList<Author>();
				l.add(dummyAuthor);
				metainformation.setAuthorsList(l);
			}
			toSave.setAuthorsList(metainformation.getAuthorsList());
			
			jaxbMarshaller.marshal(toSave, file);
			
			setDirty(false);
		} catch (JAXBException e) {
			e.printStackTrace();
			//TODO error message
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
		
		activeFilePath = ((IFileEditorInput) input).getFile().getLocation();
		
		//testSave(activeFilePath.toOSString());
	}

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
			redrawAuthorsList();
		} catch (JAXBException e) {
			e.printStackTrace();
			// TODO error message
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
		dataProvider = new DataProvider();
		
		GridLayout layout = new GridLayout(2, false);
		layout.numColumns = 2;
		parent.setLayout(layout);

//		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.V_SCROLL);
//		Composite child = new Composite(scrolledComposite, SWT.NONE);
//		new Text(scrolledComposite, SWT.BORDER);
//		child.setLayout(new FillLayout());
//	    // Create the buttons
//	    new Button(child, SWT.PUSH).setText("One");
		
		//multipurpose layouts for all form elements
		GridLayout gridLayout2Columns = new GridLayout();
		gridLayout2Columns.numColumns = 2;
		
		GridData gridDataGroups = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridDataGroups.horizontalSpan = 2;
		
		GridData gridSmallTextboxes = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		
		Group keyFactsGroup = new Group(parent, SWT.NONE);
		keyFactsGroup.setText("Key facts");		
		keyFactsGroup.setLayout(gridLayout2Columns);
		keyFactsGroup.setLayoutData(gridDataGroups);
		
		//Labels and Textboxes for key facts group
		new Label(keyFactsGroup, SWT.NONE).setText("File");
		textboxFile = new Text(keyFactsGroup, SWT.BORDER);
		textboxFile.setEditable(false);
		textboxFile.setLayoutData(gridSmallTextboxes);

		new Label(keyFactsGroup, SWT.NONE).setText("Title");
		textboxTitle = new Text(keyFactsGroup, SWT.BORDER);
		textboxTitle.setLayoutData(gridSmallTextboxes);

		new Label(keyFactsGroup, SWT.NONE).setText("Keywords");
		textboxKeywords = new Text(keyFactsGroup, SWT.BORDER);
		textboxKeywords.setLayoutData(gridSmallTextboxes);
		
		//begin authorsgroup
		Group authorsGroup = new Group(keyFactsGroup, SWT.NONE);
		authorsGroup.setText("Authors");
		authorsGroup.setLayout(new RowLayout());
		GridData gridDataAuthorsGroup = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridDataAuthorsGroup.horizontalSpan = 2;
		authorsGroup.setLayoutData(gridDataAuthorsGroup);
		
		Button buttonAddAuthor = new Button(authorsGroup, 0);
		buttonAddAuthor.setText("Add");
		
		Button buttonEditAuthor = new Button(authorsGroup, 0);
		buttonEditAuthor.setText("Edit");
		
		Button buttonDeleteAuthor = new Button(authorsGroup, 0);
		buttonDeleteAuthor.setText("Delete");
		
		//List is initialized later on, because initialization of form fields needs to be done first
		listAuthorsList = new org.eclipse.swt.widgets.List(authorsGroup, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		listAuthorsList.setLayoutData(new RowData(1000, 50));
		//end authors group
		
		Group descriptionGroup = new Group(parent, SWT.NONE);
		descriptionGroup.setText("Description");
		descriptionGroup.setLayout(gridLayout2Columns);
		descriptionGroup.setLayoutData(gridDataGroups);

		//grid data for textboxes abstract & description
		GridData gridDataBigTextboxes = new GridData();
		gridDataBigTextboxes.horizontalAlignment = SWT.FILL;
		gridDataBigTextboxes.grabExcessHorizontalSpace = true;
		gridDataBigTextboxes.verticalAlignment = SWT.FILL;
		gridDataBigTextboxes.grabExcessVerticalSpace = true;
		gridDataBigTextboxes.heightHint = 200;

		new Label(descriptionGroup, SWT.NONE).setText("Abstract");
		textboxAbstract = new Text(descriptionGroup, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		textboxAbstract.setLayoutData(gridDataBigTextboxes);

		new Label(descriptionGroup, SWT.NONE).setText("Description of the taxonomy");
		textboxDescriptionTaxonomy = new Text(descriptionGroup, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		textboxDescriptionTaxonomy.setLayoutData(gridDataBigTextboxes);
		
		Group annotateTaxonomyGroup = new Group(parent, SWT.NONE);
		annotateTaxonomyGroup.setText("Annotate Taxonomy");
		annotateTaxonomyGroup.setLayout(gridLayout2Columns);
		annotateTaxonomyGroup.setLayoutData(gridDataGroups);
		
		Combo comboDropDown = new Combo(annotateTaxonomyGroup, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		//TODO functionality
		List<Term> dimensions = dataProvider.getAllDimensionsOrdered();
		for(Term t : dimensions) {
			comboDropDown.add(t.getName());
		}
		GridData comboLayoutData = new GridData();
		comboLayoutData.widthHint = 100;
		comboDropDown.setLayoutData(comboLayoutData);
		
		Text termAnnotation = new Text(annotateTaxonomyGroup, SWT.SINGLE | SWT.BORDER);
		termAnnotation.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		Button refreshTaxonomy = new Button(annotateTaxonomyGroup, 0);
		refreshTaxonomy.setText("Refresh");
		
		initFormFields();
		
		
		//Listener for author group buttons
		buttonAddAuthor.addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection: {
		        	NewAndEditAuthorDialog dialog = new NewAndEditAuthorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
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
		
		buttonEditAuthor.addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection: {
		        	int editIndex = listAuthorsList.getSelectionIndex();
					if(editIndex < 0) break;
					
		        	NewAndEditAuthorDialog dialog = new NewAndEditAuthorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
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
		
		buttonDeleteAuthor.addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection: {
					if(listAuthorsList.getSelectionIndex() < 0) break;
					
					metainformation.getAuthorsList().remove(listAuthorsList.getSelectionIndex());
					
					if(metainformation.getAuthorsList().isEmpty()) {
						MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Info", "List of authors must not be empty. A dummy will be inserted.");
						metainformation.getAuthorsList().add(dummyAuthor);
					}
					setDirty(true);
					redrawAuthorsList();
				}
					break;
				}
			}
	      });


		ModifyListener modifiedDirty = new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				setDirty(true);
			}
		};

		textboxTitle.addModifyListener(modifiedDirty);
		textboxKeywords.addModifyListener(modifiedDirty);
		textboxAbstract.addModifyListener(modifiedDirty);
		textboxDescriptionTaxonomy.addModifyListener(modifiedDirty);
		
	    refreshTaxonomy.addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event e) {
	          switch (e.type) {
	          case SWT.Selection:{
	      		List<Term> dimensions = new DataProvider().getAllDimensionsOrdered();
	    		for(Term t : dimensions) {
	    			comboDropDown.add(t.getName());
	    		}
	    		if(comboDropDown.getItemCount() > 0) {
		    		comboDropDown.select(0);
	    		}
	          }
	          default:{
	        	  //TODO
	          }
	            break;
	          }
	        }
	      });
	    
	    

	}

	protected void redrawAuthorsList() {
		if(listAuthorsList.getItems().length != 0) {
			listAuthorsList.removeAll();
		}
		for (Author a : metainformation.getAuthorsList()) {
			listAuthorsList.add(a.toString());
		}
	}

	@Override
	public void setFocus() {
	}

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

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(m, file);
			// jaxbMarshaller.marshal(m, System.out);

		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

}