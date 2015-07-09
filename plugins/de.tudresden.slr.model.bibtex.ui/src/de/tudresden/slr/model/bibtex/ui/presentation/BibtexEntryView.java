package de.tudresden.slr.model.bibtex.ui.presentation;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
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
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
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
	protected AdapterFactory adapterFactory;
	protected AdapterFactoryEditingDomain editingDomain;
	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action action1;
	private Action action2;
	private BibtexOpenListener openListener, selectionListener;

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
		viewer.setContentProvider(new AdapterFactoryContentProvider(
				adapterFactory));
		ILabelDecorator decorator = PlatformUI.getWorkbench()
				.getDecoratorManager().getLabelDecorator();
		viewer.setLabelProvider(new DecoratingLabelProvider(
				new AdapterFactoryLabelProvider(adapterFactory), decorator));
		viewer.setSorter(new NameSorter());
		viewer.setInput(editingDomain.getResourceSet());
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
		ModelRegistryPlugin.getModelRegistry().getEditingDomain()
				.ifPresent((domain) -> editingDomain = domain);
		// Add a listener to set the most recent command's affected objects
		// to be the selection of the viewer with focus.
		if (editingDomain == null) {
			System.err
					.println("[BibtexEntryView#initializeEditingDomain] uninitailised editing domain");
			return;
		}
		adapterFactory = editingDomain.getAdapterFactory();
		editingDomain.getCommandStack().addCommandStackListener(
				new CommandStackListener() {

					@Override
					public void commandStackChanged(final EventObject event) {
						getSite().getShell().getDisplay().asyncExec(() -> {
							firePropertyChange(IEditorPart.PROP_DIRTY);
						});
					}
				});

		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		for (IProject project : projects) {
			registerResources(project);
		}
	}

	/**
	 * register the resources of a project at the editing domain.
	 * 
	 * @param project
	 */
	private void registerResources(IProject project) {
		IResource[] resources = null;
		try {
			resources = project.members();
		} catch (CoreException e) {
			e.printStackTrace();
			return;
		}
		for (IResource res : resources) {
			if (res.getType() == IResource.FILE
					&& "bib".equals(res.getFileExtension())) {
				URI uri = URI.createURI(((IFile) res).getFullPath().toString());
				editingDomain.getResourceSet().getResource(uri, true);
			}
		}
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
			viewer.setInput(editingDomain.getResourceSet());
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
			if (uri == null) {
				return;
			}
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
