package de.tudresden.slr.model.bibtex.ui.presentation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.view.ExtendedPropertySheetPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetPage;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.bibtex.ui.serialization.DocumentStorageEditorInput;
import de.tudresden.slr.model.bibtex.ui.util.Utils;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;

/**
 * An example showing how to create a multi-page editor. This example has 3
 * pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class BibtexEditor extends MultiPageEditorPart implements ISelectionProvider {
	public static final String ID = "de.tudresden.slr.model.bibtex.presentation.BibtexEditor";

	// TODO: prettify
	protected Composite parent = null;
	protected Document document;
	protected AdapterFactoryContentProvider contentProvider;
	protected AdapterFactoryEditingDomain editingDomain;
	protected int pdfIndex = -1;
	protected int webindex = -1;
	protected Browser browser;
	protected int propertyindex = -1;
	protected PropertySheetPage property;
	private ISelection selection;
	private Set<ISelectionChangedListener> selectionListeners = new HashSet<>();
	private static List<PropertySheetPage> propertySheetPages = new ArrayList<>();

	protected Collection<Resource> changedResources = new ArrayList<Resource>();
	protected Collection<Resource> removedResources = new ArrayList<Resource>();
	protected Collection<Resource> savedResources = new ArrayList<Resource>();
	/**
	 * This listens for when the outline becomes active <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private IPartListener partListener = new IPartListener() {
		@Override
		public void partActivated(IWorkbenchPart part) {
			if (part instanceof PropertySheet) {
				if (propertySheetPages.contains(((PropertySheet) part).getCurrentPage())) {
					getActionBarContributor().setActiveEditor(BibtexEditor.this);
					setSelection(getSelection());
				}
			} else if (part == BibtexEditor.this) {
				ModelRegistryPlugin.getModelRegistry().setActiveDocument(document);
			}
		}

		@Override
		public void partBroughtToTop(IWorkbenchPart p) {
			// Ignore.
		}

		@Override
		public void partClosed(IWorkbenchPart p) {
			// Ignore.
		}

		@Override
		public void partDeactivated(IWorkbenchPart p) {
			// Ignore.
		}

		@Override
		public void partOpened(IWorkbenchPart p) {
		}
	};

	public BibtexEditor() {
		super();
		initializeEditingDomain();
	}

	/**
	 * This sets up the editing domain for the model editor. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void initializeEditingDomain() {
		ModelRegistryPlugin.getModelRegistry().getEditingDomain().ifPresent((domain) -> editingDomain = domain);
		contentProvider = new AdapterFactoryContentProvider(editingDomain.getAdapterFactory());
	}

	@Override
	protected void pageChange(int newPageIndex) {
		if (newPageIndex == webindex && browser != null) {
			String url = "";
			if (document.getUrl() != null) {
				url = document.getUrl();
			} else if (document.getDoi() != null) {
				url = "http://doi.org/" + document.getDoi();
			} else {
				return;
			}
			browser.setUrl(url);
			// call url only once
			browser = null;
		} else if (newPageIndex == propertyindex) {
			property.setPropertySourceProvider(contentProvider);
			property.selectionChanged(BibtexEditor.this, getSelection());

		} else if (newPageIndex == pdfIndex) {
			openPdf();
			this.setActivePage(0);
			return;
		}
		super.pageChange(newPageIndex);
	}

	/**
	 * open the file document which is refered to in the bibtex entry. The path
	 * has to start from the root of the project where the bibtex entry is
	 * included.
	 */
	private void openPdf() {
		IFile res = Utils.getIFilefromDocument(document);
		if (res == null || res.getProject() == null) {
			MessageDialog.openInformation(this.getSite().getShell(), "Bibtex" + document.getKey(), "Root or Resource not found");
			return;
		}
		IFile file = res.getProject().getFile(document.getFile());
		if (file.exists()) {
			IFileStore fileStore = EFS.getLocalFileSystem().getStore(file.getLocation());
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

			try {
				IDE.openEditorOnFileStore(page, fileStore);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		} else {
			MessageDialog.openInformation(this.getSite().getShell(), "Bibtex" + document.getKey(), "Document not found");
		}
	}

	private void createPageLabel(Composite composite, GridData gridData) {
		Label label = new Label(composite, SWT.LEFT);
		label.setText(document.getTitle());
		FontDescriptor boldDescriptor = FontDescriptor.createFrom(label.getFont()).setStyle(SWT.BOLD);
		Font boldFont = boldDescriptor.createFont(label.getDisplay());
		label.setFont(boldFont);
		label.setLayoutData(gridData);
	}

	private void createAuthorsLabel(Composite composite, GridData gridData) {
		Label label = new Label(composite, SWT.LEFT);
		label.setText(String.join(" and ", document.getAuthors()));
		label.setLayoutData(gridData);
	}

	private void createDateLabel(Composite composite, GridData gridData) {
		Label label = new Label(composite, SWT.LEFT);
		StringBuilder labelText = new StringBuilder();
		if (document.getMonth() != null && !document.getMonth().isEmpty()) {
			labelText.append(document.getMonth()).append(" ");
		}
		if (document.getYear() != null && !document.getYear().isEmpty()) {
			labelText.append(document.getYear());
		}
		label.setText(labelText.toString());
		label.setLayoutData(gridData);
	}

	private void createAbstractText(Composite composite) {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalSpan = 1;
		gridData.horizontalSpan = 2;
		
		StyledText text = new StyledText(composite, SWT.V_SCROLL | SWT.READ_ONLY | SWT.WRAP);
		text.setEditable(false);
		text.setLayoutData(gridData);
		if (document.getAbstract() != null) {
			text.setText(document.getAbstract());
		}
	}

	/**
	 * Creates page 0 of the multi-page editor, which contains a text editor.
	 */
	protected void createAbstractPage() {
		Composite localParent = getContainer();
		if (localParent == null) {
			localParent = parent;
		}
		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));

		GridData gridData = new GridData();
		
		Label lblTitle = new Label(container, SWT.NONE);
		lblTitle.setText("Title");
		createPageLabel(container, gridData);
		Label lblAuthor = new Label(container, SWT.NONE);
		lblAuthor.setText("Author");
		createAuthorsLabel(container, gridData);
		Label lblPublished = new Label(container, SWT.NONE);
		lblPublished.setText("Published");
		createDateLabel(container, gridData);
		createAbstractText(container);

		int index = addPage(container);
		setPageText(index, "Abstract");
	}

	/**
	 * Creates page 1 of the multi-page editor, which allows you to change the
	 * font used in page 2.
	 */
	protected void createPropertyPage() {
		property = new PropertySheetPage();
		Composite localParent = getContainer();
		if (localParent == null) {
			localParent = parent;
		}
		property.createControl(localParent);
		property.setPropertySourceProvider(contentProvider);
		propertyindex = addPage(property.getControl());
		setPageText(propertyindex, "Properties");
	}

	/**
	 * Creates a browser for downloading the document which the bibtex entry
	 * refers to. This only works if there is an URL or DOI in the bibtex entry.
	 */
	protected void createWebpage() {
		if (document.getUrl() == null && document.getDoi() == null) {
			return;
		}
		Composite localParent = getContainer();
		if (localParent == null) {
			localParent = parent;
		}
		Composite webcomposite = new Composite(localParent, SWT.NONE);
		FillLayout layout = new FillLayout();
		webcomposite.setLayout(layout);
		browser = new Browser(webcomposite, SWT.NONE);
		browser.setLayout(layout);
		webindex = addPage(webcomposite);
		setPageText(webindex, "Webpage");
	}

	/**
	 * Creates an anchor for opening the research paper. This only works if the
	 * paper is referred in the bibtex entry and the named file exists.
	 */
	protected void createPdfPage() {
		if (document.getFile() != null && !document.getFile().isEmpty()) {
			IFile res = Utils.getIFilefromDocument(document);
			if (res == null) {
				return;
			}
			IFile projFile = res.getProject().getFile(document.getFile());
			if (projFile.exists()) {
				Composite localParent = getContainer();
				if (localParent == null) {
					localParent = parent;
				}
				Composite composite = new Composite(localParent, SWT.NONE);
				pdfIndex = addPage(composite);
				setPageText(pdfIndex, "PDF");
			}
		}

	}

	@Override
	protected void createPages() {
		getSite().setSelectionProvider(this);
		if (parent == null) {
			parent = getContainer();
		}
		createAbstractPage();
		createPropertyPage();
		createWebpage();
		createPdfPage();
	}

	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		// Save only resources that have actually changed.
		//
		final Map<Object, Object> saveOptions = new HashMap<Object, Object>();
		saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
		saveOptions.put(Resource.OPTION_LINE_DELIMITER, Resource.OPTION_LINE_DELIMITER_UNSPECIFIED);

		// Do the work within an operation because this is a long running
		// activity that modifies the workbench.
		WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
			// This is the method that gets invoked when the operation runs.
			@Override
			public void execute(IProgressMonitor monitor) {
				// Save the resources to the file system.
				boolean first = true;
				for (Resource resource : editingDomain.getResourceSet().getResources()) {
					if ((first || !resource.getContents().isEmpty() || Utils
							.isPersisted(resource))
							&& !editingDomain.isReadOnly(resource)) {
						try {
							long timeStamp = resource.getTimeStamp();
							resource.save(saveOptions);
							if (resource.getTimeStamp() != timeStamp) {
								savedResources.add(resource);
							}
						} catch (Exception exception) {
						}
						first = false;
					}
				}
			}
		};
		try {
			// This runs the options, and shows progress.
			new ProgressMonitorDialog(getSite().getShell()).run(true, false, operation);

			// Refresh the necessary state.
			((BasicCommandStack) editingDomain.getCommandStack()).saveIsDone();
			firePropertyChange(IEditorPart.PROP_DIRTY);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void doSaveAs() {
		// TODO: save entry as new bib file
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	@Override
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		if (!(editorInput instanceof DocumentStorageEditorInput)) {
			PartInitException pie = new PartInitException("Invalid Input: Must be DocumentStorageEditorInput");
			pie.printStackTrace();
			throw pie;
		}
		super.init(site, editorInput);
		setPartName(editorInput.getName());
		site.getPage().addPartListener(partListener);
		extractDocument(editorInput);
		getActionBarContributor().setActiveEditor(BibtexEditor.this);
		setSelection(getSelection());
		ModelRegistryPlugin.getModelRegistry().setActiveDocument(document);
	}

	protected void extractDocument(IEditorInput editorInput) {
		try {
			this.document = ((DocumentStorageEditorInput) editorInput).getStorage().getDocument();
			this.selection = new StructuredSelection(this.document);
			ModelRegistryPlugin.getModelRegistry().setActiveDocument(document);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ISelection getSelection() {
		return selection;
	}

	@Override
	public void setSelection(ISelection selection) {
		this.selection = selection;
		final SelectionChangedEvent event = new SelectionChangedEvent(this, selection);
		selectionListeners.forEach(listener -> listener.selectionChanged(event));
	}

	/**
	 * This is how the framework determines which interfaces we implement. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object getAdapter(Class key) {
		if (key.equals(IPropertySheetPage.class)) {
			return getPropertySheetPage();
		} else if (key.equals(IGotoMarker.class)) {
			throw new RuntimeException("Returning this as IGotoMarker.");
			//return this;
		} else {
			return super.getAdapter(key);
		}
	}

	/**
	 * This accesses a cached version of the property sheet. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IPropertySheetPage getPropertySheetPage() {
		PropertySheetPage propertySheetPage = new ExtendedPropertySheetPage(
				editingDomain) {
			@Override
			public void setSelectionToViewer(List<?> selection) {
				BibtexEditor.this.setFocus();
			}

			@Override
			public void setActionBars(IActionBars actionBars) {
				super.setActionBars(actionBars);
			}
		};
		propertySheetPage.setPropertySourceProvider(contentProvider);
		propertySheetPages.add(propertySheetPage);
		propertySheetPage.handleEntrySelection(getSelection());
		propertySheetPage.setRootEntry(null);
		return propertySheetPage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IEditorActionBarContributor getActionBarContributor() {
		return getEditorSite().getActionBarContributor();
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		selectionListeners.add(listener);
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		selectionListeners.remove(listener);
	}

	protected IResourceChangeListener resourceChangeListener = new IResourceChangeListener() {
		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			IResourceDelta delta = event.getDelta();
			try {
				class ResourceDeltaVisitor implements IResourceDeltaVisitor {
					protected ResourceSet resourceSet = editingDomain.getResourceSet();
					protected Collection<Resource> changedResources = new ArrayList<Resource>();
					protected Collection<Resource> removedResources = new ArrayList<Resource>();

					@Override
					public boolean visit(IResourceDelta delta) {
						if (delta.getResource().getType() == IResource.FILE) {
							if (delta.getKind() == IResourceDelta.REMOVED || delta.getKind() == IResourceDelta.CHANGED && delta.getFlags() != IResourceDelta.MARKERS) {
								Resource resource = resourceSet.getResource(URI.createPlatformResourceURI(delta.getFullPath().toString(),true), false);
								if (resource != null) {
									if (delta.getKind() == IResourceDelta.REMOVED) {
										removedResources.add(resource);
									} else if (!savedResources.remove(resource)) {
										changedResources.add(resource);
									}
								}
							}
							return false;
						}
						return true;
					}

					public Collection<Resource> getChangedResources() {
						return changedResources;
					}

					public Collection<Resource> getRemovedResources() {
						return removedResources;
					}
				}

				final ResourceDeltaVisitor visitor = new ResourceDeltaVisitor();
				delta.accept(visitor);

				if (!visitor.getRemovedResources().isEmpty()) {
					getSite().getShell().getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							removedResources.addAll(visitor.getRemovedResources());
							if (!isDirty()) {
								getSite().getPage().closeEditor(BibtexEditor.this, false);
							}
						}
					});
				}

				if (!visitor.getChangedResources().isEmpty()) {
					getSite().getShell().getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							changedResources.addAll(visitor.getChangedResources());
							if (getSite().getPage().getActiveEditor() == BibtexEditor.this) {
								handleActivate();
							}
						}
					});
				}
			} catch (CoreException exception) {
				exception.printStackTrace();
			}
		}
	};

	protected void handleActivate() {
		// Recompute the read only state.
		if (editingDomain.getResourceToReadOnlyMap() != null) {
			editingDomain.getResourceToReadOnlyMap().clear();
			// Refresh any actions that may become enabled or disabled.
			setSelection(getSelection());
		}

		if (!removedResources.isEmpty()) {
			removedResources.clear();
			changedResources.clear();
			savedResources.clear();
		} else if (!changedResources.isEmpty()) {
			changedResources.removeAll(savedResources);
			handleChangedResources();
			changedResources.clear();
			savedResources.clear();
		}
	}

	/**
	 * Handles what to do with changed resources on activation. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void handleChangedResources() {
		if (!changedResources.isEmpty()) {
			if (isDirty()) {
				changedResources.addAll(editingDomain.getResourceSet().getResources());
			}
			editingDomain.getCommandStack().flush();

			for (Resource resource : changedResources) {
				if (resource.isLoaded()) {
					resource.unload();
					try {
						resource.load(Collections.EMPTY_MAP);
					} catch (IOException exception) {
						exception.printStackTrace();
					}
				}
			}

		}
	}

	@Override
	public boolean isDirty() {
		return ((BasicCommandStack) editingDomain.getCommandStack()).isSaveNeeded();
	}
}
