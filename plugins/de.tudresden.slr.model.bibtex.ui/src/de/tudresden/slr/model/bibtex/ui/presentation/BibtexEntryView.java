package de.tudresden.slr.model.bibtex.ui.presentation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.command.BasicCommandStack;
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
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.bibtex.impl.DocumentImpl;
import de.tudresden.slr.model.bibtex.provider.BibtexItemProviderAdapterFactory;

/**
 * This view shows the data of {@link DocumentImpl}. It uses different pages:
 * one for title, author and abstract; one for properties, one for web access
 * and one for local file call.
 * 
 * @author Manuel Brauer
 */

public class BibtexEntryView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "bibtex.presentation.BibtexEntryView";
	public static final String editorId = BibtexEditor.ID;
	public static final String overviewId = BibtexOverviewEditor.ID;
	/**
	 * This is the one adapter factory used for providing views of the model.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ComposedAdapterFactory adapterFactory;
	/**
	 * This keeps track of the editing domain that is used to track all changes
	 * to the model. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected AdapterFactoryEditingDomain editingDomain;

	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action action1;
	private Action action2;
	private BibtexOpenListener openListener, selectionListener;

	/*
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */

	class TreeObject implements IAdaptable {
		private String name;
		private Object content;
		private TreeParent parent;

		public TreeObject(String name, Object content) {
			this.name = name;
			this.content = content;
		}

		public String getName() {
			return name;
		}

		public void setParent(TreeParent parent) {
			this.parent = parent;
		}

		public TreeParent getParent() {
			return parent;
		}

		public void setContent(Object content) {
			this.content = content;
		}

		public Object getContent() {
			return content;
		}

		@Override
		public String toString() {
			return getName();
		}

		@Override
		public Object getAdapter(Class key) {
			return null;
		}
	}

	class TreeParent extends TreeObject {
		private ArrayList<TreeObject> children;

		public TreeParent(String name) {
			super(name, null);
			children = new ArrayList<TreeObject>();
		}

		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}

		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}

		public Object[] getChildren() {
			Object[] ret = new Object[children.size()];
			TreeObject child;
			for (int i = 0; i < ret.length; i++) {
				child = children.get(i);
				if (!(child instanceof TreeParent)) {
					ret[i] = child.getContent();
				} else {
					ret[i] = child;
				}
			}
			return ret;
		}

		public boolean hasChildren() {
			return children.size() > 0;
		}
	}

	class ViewContentProvider implements IStructuredContentProvider,
			ITreeContentProvider {
		private TreeParent invisibleRoot;

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
					initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}

		@Override
		public Object getParent(Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject) child).getParent();
			}
			return null;
		}

		@Override
		public Object[] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent) parent).getChildren();
			}
			return new Object[0];
		}

		@Override
		public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent) parent).hasChildren();
			return false;
		}

		/*
		 * We will set up a dummy model to initialize tree heararchy. In a real
		 * code, you will connect to a real model and expose its hierarchy.
		 */
		private void initialize() {
			invisibleRoot = new TreeParent("");

			IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
					.getProjects();
			TreeParent projTree, bibTree;
			for (IProject project : projects) {
				projTree = new TreeParent(project.getName());
				invisibleRoot.addChild(projTree);

				IResource[] resources;
				try {
					resources = project.members();
				} catch (CoreException e) {
					e.printStackTrace();
					continue;
				}
				for (IResource res : resources) {
					if (res.getType() != IResource.FILE
							|| !"bib".equals(res.getFileExtension())) {
						continue;
					}
					bibTree = new TreeParent(res.getName());
					projTree.addChild(bibTree);
					Resource resource = editingDomain.createResource(res
							.getFullPath().toString());
					try {
						resource.load(null);
					} catch (IOException e) {
						e.printStackTrace();
						continue;
					}
					TreeObject tobj;
					Document doc;
					for (EObject eobj : resource.getContents()) {
						if (!(eobj instanceof Document)) {
							continue;
						}
						doc = (Document) eobj;
						tobj = new TreeObject(doc.getKey(), doc);
						bibTree.addChild(tobj);
					}
					// adapterFactory.getFactoryForType(res);
				}
			}
			/*
			 * TreeObject to1 = new TreeObject("Leaf 1"); TreeObject to2 = new
			 * TreeObject("Leaf 2"); TreeObject to3 = new TreeObject("Leaf 3");
			 * TreeParent p1 = new TreeParent("Parent 1"); p1.addChild(to1);
			 * p1.addChild(to2); p1.addChild(to3);
			 * 
			 * TreeObject to4 = new TreeObject("Leaf 4"); TreeParent p2 = new
			 * TreeParent("Parent 2"); p2.addChild(to4);
			 * 
			 * TreeParent root = new TreeParent("Root"); root.addChild(p1);
			 * root.addChild(p2);
			 * 
			 * invisibleRoot = new TreeParent(""); invisibleRoot.addChild(root);
			 */}
	}

	class ViewLabelProvider extends LabelProvider {

		@Override
		public String getText(Object obj) {
			if (obj instanceof Document) {
				return ((Document) obj).getKey();
			}
			return obj.toString();
		}

		@Override
		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			if (obj instanceof TreeParent)
				imageKey = ISharedImages.IMG_OBJ_FOLDER;
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(imageKey);
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
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		makeActions();
		hookContextMenu();
		hookActions();
		contributeToActionBars();
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
		/*
		 * // Add a listener to set the most recent command's affected objects
		 * to // be the selection of the viewer with focus. //
		 * commandStack.addCommandStackListener(new CommandStackListener() {
		 * 
		 * @Override public void commandStackChanged(final EventObject event) {
		 * getSite().getShell().getDisplay().asyncExec(new Runnable() {
		 * 
		 * @Override public void run() {
		 * firePropertyChange(IEditorPart.PROP_DIRTY);
		 * 
		 * // Try to select the affected objects. // Command mostRecentCommand =
		 * ((CommandStack) event .getSource()).getMostRecentCommand(); if
		 * (mostRecentCommand != null) { setSelectionToViewer(mostRecentCommand
		 * .getAffectedObjects()); } for (Iterator<PropertySheetPage> i =
		 * propertySheetPages .iterator(); i.hasNext();) { PropertySheetPage
		 * propertySheetPage = i.next(); if
		 * (propertySheetPage.getControl().isDisposed()) { i.remove(); } else {
		 * propertySheetPage.refresh(); } } } }); } });
		 */
		// Create the editing domain with a special command stack.
		//
		editingDomain = new AdapterFactoryEditingDomain(adapterFactory,
				commandStack, new HashMap<Resource, Boolean>());
	}

	/**
	 * This sets the selection into whichever viewer is active. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
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
				BibtexEntryView.this.fillContextMenu(manager);
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
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		action2 = new Action() {
			@Override
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
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
}