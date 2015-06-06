package bibtex.presentation;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.view.ExtendedPropertySheetPage;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetPage;

import bibtex.Document;
import bibtex.presentation.serialization.DocumentStorageEditorInput;
import bibtex.provider.BibtexItemProviderAdapterFactory;

/**
 * An example showing how to create a multi-page editor. This example has 3
 * pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class BibtexEditor extends MultiPageEditorPart implements
		IResourceChangeListener, ISelectionProvider {
	public static final String ID = "bibtex.presentation.BibtexEditor";
	protected Composite parent = null;
	protected Document document;
	protected ComposedAdapterFactory adapterFactory;
	protected AdapterFactoryEditingDomain editingDomain;
	protected int webindex = -1;
	protected Composite webcomposite;
	protected int propertyindex = -1;
	protected PropertySheetPage property;
	private ISelection selection;
	private HashSet<ISelectionChangedListener> selectionListeners = new HashSet<ISelectionChangedListener>();
	private static ArrayList<PropertySheetPage> propertySheetPages = new ArrayList<PropertySheetPage>();
	/**
	 * This listens for when the outline becomes active <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private IPartListener partListener = new IPartListener() {
		@Override
		public void partActivated(IWorkbenchPart p) {
			if (p instanceof PropertySheet) {
				if (propertySheetPages.contains(((PropertySheet) p)
						.getCurrentPage())) {
					getActionBarContributor()
							.setActiveEditor(BibtexEditor.this);
					setSelection(getSelection());

					// handleActivate();
				}
			} else if (p == BibtexEditor.this) {
				// handleActivate();
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
			// Ignore.
		}
	};

	public BibtexEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		initializeEditingDomain();
	}

	/**
	 * This sets up the editing domain for the model editor. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void initializeEditingDomain() {
		// Create an adapter factory that yields item providers.
		//
		adapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		adapterFactory
				.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory
				.addAdapterFactory(new BibtexItemProviderAdapterFactory());
		adapterFactory
				.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		// Create the command stack that will notify this editor as commands are
		// executed.
		//
		BasicCommandStack commandStack = new BasicCommandStack();
		// Add a listener to set the most recent command's affected objects to
		// be the selection of the viewer with focus.
		//
		commandStack.addCommandStackListener(new CommandStackListener() {
			@Override
			public void commandStackChanged(final EventObject event) {
				getContainer().getDisplay().asyncExec(new Runnable() {

					@Override
					public void run() {
						firePropertyChange(IEditorPart.PROP_DIRTY);

						// Try to select the affected objects.
						//
						Command mostRecentCommand = ((CommandStack) event
								.getSource()).getMostRecentCommand();
						for (Iterator<PropertySheetPage> i = propertySheetPages
								.iterator(); i.hasNext();) {
							PropertySheetPage propertySheetPage = i.next();
							if (propertySheetPage.getControl().isDisposed()) {
								i.remove();
							} else {
								propertySheetPage.refresh();
							}
						}
					}
				});
			}
		});
		// Create the editing domain with a special command stack.
		//
		editingDomain = new AdapterFactoryEditingDomain(adapterFactory,
				commandStack, new HashMap<Resource, Boolean>());
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					// TODO: before closing the editor
				}
			});
		}

	}

	protected void pageChange(int newPageIndex) {
		if (newPageIndex == webindex && webcomposite != null) {
			Browser browser = new Browser(webcomposite, SWT.NONE);
			String url = "";
			if (document.getUrl() != null) {
				url = document.getUrl();
			} else if (document.getDoi() != null) {
				url = "http://doi.org/" + document.getDoi();
			} else {
				return;
			}
			browser.setUrl(url);
			webcomposite = null;
		}
		if (newPageIndex == propertyindex) {
			property.setPropertySourceProvider(new AdapterFactoryContentProvider(
					adapterFactory));
			property.selectionChanged(BibtexEditor.this, getSelection());

		}
		super.pageChange(newPageIndex);
	}

	/**
	 * Creates page 0 of the multi-page editor, which contains a text editor.
	 */
	protected void createPage0() {
		Composite localParent = getContainer();
		if (localParent == null) {
			localParent = parent;
		}
		Composite composite = new Composite(localParent, SWT.NONE);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.makeColumnsEqualWidth = true;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		composite.setLayout(layout);

		// Title
		Label label = new Label(composite, SWT.CENTER);
		label.setText(document.getTitle());
		FontDescriptor boldDescriptor = FontDescriptor.createFrom(
				label.getFont()).setStyle(SWT.BOLD);
		Font boldFont = boldDescriptor.createFont(label.getDisplay());
		label.setFont(boldFont);
		label.setLayoutData(gridData);

		// Authors
		label = new Label(composite, SWT.CENTER);
		label.setText(String.join(" and ", document.getAuthors()));
		label.setLayoutData(gridData);

		// Date
		label = new Label(composite, SWT.CENTER);
		String labelText = "";
		if (document.getMonth() != null) {
			labelText += document.getMonth() + " ";
		}
		if (document.getYear() != null) {
			labelText += document.getYear();
		}
		if (labelText.length() > 0) {
			labelText = "published in " + labelText;
		}
		label.setText(labelText.trim());
		// TODO: dispose font
		FontDescriptor italicDescriptor = FontDescriptor.createFrom(
				label.getFont()).setStyle(SWT.ITALIC);
		// TODO: use just one font object
		Font italicFont = italicDescriptor.createFont(label.getDisplay());
		label.setFont(italicFont);
		label.setLayoutData(gridData);

		// Abstract
		StyledText text = new StyledText(composite, SWT.V_SCROLL
				| SWT.READ_ONLY | SWT.WRAP);
		text.setEditable(false);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		text.setLayoutData(gridData);
		if (document.getAbstract() != null) {
			text.setText(document.getAbstract());
		}

		int index = addPage(composite);
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
		property.setPropertySourceProvider(new AdapterFactoryContentProvider(
				adapterFactory));
		propertyindex = addPage(property.getControl());
		setPageText(propertyindex, "Properties");
	}

	/**
	 * Creates page 2 of the multi-page editor, which shows the sorted text.
	 */
	protected void createWebpage() {
		Composite localParent = getContainer();
		if (localParent == null) {
			localParent = parent;
		}
		webcomposite = new Composite(localParent, SWT.NONE);
		FillLayout layout = new FillLayout();
		webcomposite.setLayout(layout);
		webindex = addPage(webcomposite);
		setPageText(webindex, "Webpage");
	}

	/**
	 * Creates page 3 of the multi-page editor, which shows the sorted text.
	 */
	protected void createPdfPage() {
		Composite localParent = getContainer();
		if (localParent == null) {
			localParent = parent;
		}
		Composite composite = new Composite(localParent, SWT.NONE);
		FillLayout layout = new FillLayout();
		composite.setLayout(layout);
		StyledText text = new StyledText(composite, SWT.H_SCROLL | SWT.V_SCROLL);
		text.setEditable(false);

		int index = addPage(composite);
		setPageText(index, "PDF");
	}

	@Override
	protected void createPages() {
		getSite().setSelectionProvider(this);
		if (parent == null) {
			parent = getContainer();
		}
		createPage0();
		createPropertyPage();
		if (document.getUrl() != null || document.getDoi() != null) {
			createWebpage();
		}
		if (document.getAbstract() != null) {
			createPdfPage();
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO: save entry in bib file

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
	public void init(IEditorSite site, IEditorInput editorInput)
			throws PartInitException {
		if (!(editorInput instanceof DocumentStorageEditorInput))
			throw new PartInitException(
					"Invalid Input: Must be DocumentStorageEditorInput");
		super.init(site, editorInput);
		this.setPartName(editorInput.getName());
		site.getPage().addPartListener(partListener);
		extractDocument(editorInput);
		getActionBarContributor().setActiveEditor(BibtexEditor.this);
		setSelection(getSelection());
		// TODO: notify propertysheetpage
		/*
		 * //getActionBarContributor().setActiveEditor(this); if
		 * (propertySheetPages.isEmpty()){ getPropertySheetPage(); } for
		 * (PropertySheetPage p : propertySheetPages){
		 * p.setPropertySourceProvider(new
		 * AdapterFactoryContentProvider(adapterFactory)); p.refresh(); }
		 */
		// setInputWithNotify(editorInput);
	}

	protected void extractDocument(IEditorInput editorInput) {
		try {
			this.document = ((DocumentStorageEditorInput) editorInput)
					.getStorage().getDocument();
			this.selection = new StructuredSelection(this.document);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public ISelection getSelection() {
		return selection;
	}

	public void setSelection(ISelection selection) {
		this.selection = selection;
		// getSite().getSelectionProvider().setSelection(selection);
		for (ISelectionChangedListener l : selectionListeners) {
			l.selectionChanged(new SelectionChangedEvent(this, selection));
		}
		// setStatusLineManager(selection);
	}

	/**
	 * This is how the framework determines which interfaces we implement. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class key) {
		/*
		 * if (key.equals(IContentOutlinePage.class)) { return showOutlineView()
		 * ? getContentOutlinePage() : null; } else
		 */if (key.equals(IPropertySheetPage.class)) {
			return getPropertySheetPage();
		} else if (key.equals(IGotoMarker.class)) {
			return this;
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
				// BibtexEditor.this.setSelectionToViewer(selection);
				BibtexEditor.this.setFocus();
			}

			@Override
			public void setActionBars(IActionBars actionBars) {
				super.setActionBars(actionBars);
				// getActionBarContributor().shareGlobalActions(this,
				// actionBars);
			}
		};
		propertySheetPage
				.setPropertySourceProvider(new AdapterFactoryContentProvider(
						adapterFactory));
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
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		selectionListeners.remove(listener);
	}
}