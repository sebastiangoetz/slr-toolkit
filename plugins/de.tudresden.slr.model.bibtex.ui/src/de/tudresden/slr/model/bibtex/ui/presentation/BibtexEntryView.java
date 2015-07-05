package de.tudresden.slr.model.bibtex.ui.presentation;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import de.tudresden.slr.Utils;
import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.bibtex.impl.DocumentImpl;
import de.tudresden.slr.model.bibtex.provider.BibtexItemProviderAdapterFactory;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;

/**
 * This view shows the data of {@link DocumentImpl}. It uses different pages:
 * one for title, author and abstract; one for properties, one for web access
 * and one for local file call.
 * 
 * @author Manuel Brauer
 */

public class BibtexEntryView extends ViewPart implements
		IResourceChangeListener {

	public static final String ID = "de.tudresden.slr.model.bibtex.ui.presentation.BibtexEntryView";
	public static final String editorId = BibtexEditor.ID;
	public static final String overviewId = BibtexOverviewEditor.ID;
	protected ComposedAdapterFactory adapterFactory;
	protected AdapterFactoryEditingDomain editingDomain;
	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action action1;
	private Action action2;
	private BibtexOpenListener openListener, selectionListener;

	class ViewContentProvider implements IStructuredContentProvider,
			ITreeContentProvider {
		private String invisibleRoot;

		@Override
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			return;
		}

		@Override
		public void dispose() {
		}

		@Override
		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				if (invisibleRoot == null)
					invisibleRoot = new String("");
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}

		@Override
		public Object getParent(Object child) {
			if (child instanceof IProject) {
				return invisibleRoot;
			}
			if (child instanceof IFile) {
				return ((IFile) child).getProject();
			}
			if (child instanceof Document) {
				return Utils.getIFilefromDocument((Document) child);

			}
			return null;
		}

		@Override
		public Object[] getChildren(Object parent) {
			if (parent == invisibleRoot) {
				IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
						.getProjects();
				return projects;
			}
			if (parent instanceof IProject) {
				IResource[] resources;
				try {
					resources = ((IProject) parent).members();
				} catch (CoreException e) {
					e.printStackTrace();

					return new Object[0];
				}
				LinkedList<IFile> bibFiles = new LinkedList<IFile>();
				for (IResource res : resources) {
					if (res.getType() == IResource.FILE
							&& "bib".equals(res.getFileExtension())) {
						bibFiles.add((IFile) res);
					}
				}
				return bibFiles.toArray();
			}
			if (parent instanceof IFile) {
				URI uri = URI.createURI(((IFile) parent).getFullPath()
						.toString());
				Resource resource = editingDomain.getResourceSet().getResource(
						uri, true);
				if (resource == null) {
					return new Object[0];
				}

				return resource.getContents().toArray();

			}
			return new Object[0];
		}

		private boolean hasChildrenIProject(IProject parent) {
			IResource[] resources;
			try {
				resources = parent.members();
			} catch (CoreException e) {
				e.printStackTrace();
				return false;
			}
			for (IResource res : resources) {
				if (res.getType() == IResource.FILE
						&& "bib".equals(res.getFileExtension())) {
					return true;
				}
			}
			return false;
		}

		private boolean hasChildrenIFile(IFile parent) {
			URI uri = URI.createURI(parent.getFullPath().toString());
			Resource resource = editingDomain.getResourceSet().getResource(uri,
					true);

			for (EObject eobj : resource.getContents()) {
				if (eobj instanceof Document) {
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean hasChildren(Object parent) {
			if (parent instanceof String) {
				// invisibleRoot
				return true;
			}
			if (parent instanceof IProject) {
				return hasChildrenIProject((IProject) parent);
			}
			if (parent instanceof IFile) {
				return hasChildrenIFile((IFile) parent);
			}
			return false;
		}
	}

	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public BibtexEntryView() {
		super();
		initializeEditingDomain();
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		BibtexFilter filter = new BibtexFilter();
		FilteredTree tree = new FilteredTree(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL, filter, false);
		viewer = tree.getViewer();
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new ViewContentProvider());
		// viewer.setContentProvider(new AdapterFactoryContentProvider(
		// adapterFactory));
		ILabelDecorator decorator = PlatformUI.getWorkbench()
				.getDecoratorManager().getLabelDecorator();
		viewer.setLabelProvider(new DecoratingLabelProvider(
				new ViewLabelProvider(), decorator));
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		viewer.expandAll();
		// this is needed to let other views know what is currently selected
		// in my case the Chart View wants to display data
		// see
		// https://wiki.eclipse.org/FAQ_How_do_I_make_a_view_respond_to_selection_changes_in_another_view%3F

		getSite().setSelectionProvider(viewer);

		makeActions();
		hookContextMenu();
		hookActions();
		contributeToActionBars();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
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

		// Add a listener to set the most recent command's affected objects
		// to be the selection of the viewer with focus.
		commandStack.addCommandStackListener(new CommandStackListener() {

			@Override
			public void commandStackChanged(final EventObject event) {
				getSite().getShell().getDisplay().asyncExec(() -> {
					firePropertyChange(IEditorPart.PROP_DIRTY);

					// Try to select the affected objects.
					// Command mostRecentCommand = ((CommandStack) event
					// .getSource()).getMostRecentCommand();
					// if (mostRecentCommand != null) {
					// setSelectionToViewer(mostRecentCommand
					// .getAffectedObjects());
					// }
					// for (Iterator<PropertySheetPage> i =
					// propertySheetPages
					// .iterator(); i.hasNext();) {
					// PropertySheetPage propertySheetPage = i.next();
					// if (propertySheetPage.getControl().isDisposed()) {
					// i.remove();
					// } else {
					// propertySheetPage.refresh();
					// }
					// }
					});
			}
		});

		// Create the editing domain with a special command stack.
		//
		editingDomain = new AdapterFactoryEditingDomain(adapterFactory,
				commandStack, new HashMap<Resource, Boolean>());

		ModelRegistryPlugin.getModelRegistry().setEditingDomain(editingDomain);
	}

	public void setSelectionToViewer(Collection<?> collection) {
		// TODO: check if it is necessary
		final Collection<?> theSelection = collection;
		// Make sure it's okay.
		//
		if (theSelection != null && !theSelection.isEmpty()) {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					// Try to select the items in the current content viewer of
					// the editor.
					//
					if (BibtexEntryView.this != null) {
						BibtexEntryView.this.viewer
								.setSelection(new StructuredSelection(
										theSelection.toArray()), true);
					}
				}
			};
			getSite().getShell().getDisplay().asyncExec(runnable);
		}
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		action1 = new Action() {
			@Override
			public void run() {
				// TODO: security checks
				TreeSelection select = (TreeSelection) viewer.getSelection();
				if (select == null
						|| !(select.getFirstElement() instanceof Document)) {
					return;
				}
				Document doc = (Document) select.getFirstElement();
				IFile file = (IFile) ((ITreeContentProvider) viewer
						.getContentProvider()).getParent(select
						.getFirstElement());
				try {
					QualifiedName qName = new QualifiedName(
							BibtexDecorator.QUALIFIER, doc.getKey());
					if (file.getPersistentProperty(qName) != null
							&& BibtexDecorator.ERROR.equals(file
									.getPersistentProperty(qName))) {
						file.setPersistentProperty(qName, null);
					} else {
						file.setPersistentProperty(qName, BibtexDecorator.ERROR);
					}
					showMessage("Action 1 executed");
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		action1.setText("Decorate");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		action2 = new Action() {
			@Override
			public void run() {
				TreeSelection select = (TreeSelection) viewer.getSelection();
				if (select == null
						|| !(select.getFirstElement() instanceof Document)) {
					return;
				}
				Document doc = (Document) select.getFirstElement();
				Utils.mark(doc, "Just marking for tests", ID);
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Mark");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		openListener = new BibtexOpenListener(editorId,
				IWorkbenchPage.MATCH_INPUT | IWorkbenchPage.MATCH_ID);
		selectionListener = new BibtexOpenListener(overviewId,
				IWorkbenchPage.MATCH_ID);
	}

	private void hookActions() {
		viewer.addOpenListener(openListener);
		viewer.addSelectionChangedListener(selectionListener);
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(),
				"Bibtex Entries", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();

	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			reloadSources(event.getDelta());
			// for (Resource resource : editingDomain.getResourceSet()
			// .getResources()) {
			// resource.unload();
			// try {
			// resource.load(Collections.emptyMap());
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// }
		}
		Display.getDefault().asyncExec(() -> {
			viewer.setInput(getViewSite());
			// viewer.refresh();
			});

	}

	/**
	 * unload and load only affected resources, not all resources.
	 * 
	 * @param delta
	 *            top level delta from the {@link IResourceChangeEvent}
	 */
	private void reloadSources(IResourceDelta delta) {
		LinkedList<IResourceDelta> result = new LinkedList<IResourceDelta>();
		result.add(delta);
		do {
			IResourceDelta marchingDelta = result.removeFirst();
			if (marchingDelta.getAffectedChildren() != null
					&& marchingDelta.getAffectedChildren().length > 0) {
				// we are only interested in the affected files
				for (IResourceDelta deltaChild : marchingDelta
						.getAffectedChildren()) {
					result.add(deltaChild);
				}
				continue;
			}
			// current delta has no affected children
			// delta contains the affected file itself
			URI uri = URI.createPlatformResourceURI(marchingDelta.getFullPath()
					.toString(), true);
			Resource resource = editingDomain.getResourceSet().getResource(uri,
					true); // is it necessary to load a loaded resource?
			if (resource == null) {
				// System.err.println("Resource "
				// + marchingDelta.getFullPath().toString()
				// + " does not exist.");
				return;
			}
			resource.unload();
			try {
				resource.load(Collections.emptyMap());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (result.size() > 0);
	}
}
