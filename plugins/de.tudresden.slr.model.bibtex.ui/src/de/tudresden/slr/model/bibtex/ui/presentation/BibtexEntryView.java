package de.tudresden.slr.model.bibtex.ui.presentation;


import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
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
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.part.ViewPart;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.bibtex.impl.DocumentImpl;
import de.tudresden.slr.model.bibtex.ui.util.Utils;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;

/**
 * This view shows the data of {@link DocumentImpl}. It uses different pages:
 * one for title, author and abstract; one for properties, one for web access
 * and one for local file call.
 * 
 * @author Manuel Brauer
 */

public class BibtexEntryView extends ViewPart {

	public static final String ID = "de.tudresden.slr.model.bibtex.ui.presentation.BibtexEntryView";
	public static final String editorId = BibtexEditor.ID;
	public static final String overviewId = BibtexOverviewEditor.ID;
	public static final String confirmation = "This will close all opened documents without saving them. Do you wish to proceed?";
	protected AdapterFactory adapterFactory;
	protected AdapterFactoryEditingDomain editingDomain;
	private TreeViewer viewer;
	private Action refreshAction;
	private Action markingAction;
	private Action openingAction;
	private ComboViewer combo;
	private BibtexOpenListener openListener, selectionListener;

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
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		parent.setLayout(layout);
		combo = new ComboViewer(parent, SWT.READ_ONLY);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		combo.setSorter(new ViewerSorter());
		combo.setContentProvider(ArrayContentProvider.getInstance());
		combo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof IProject) {
					IProject project = (IProject) element;
					return project.getName();
				}
				return super.getText(element);
			}
		});
		combo.addSelectionChangedListener(createProjectListener());
		combo.getCombo().setLayoutData(gridData);

		combo.setInput(ResourcesPlugin.getWorkspace().getRoot().getProjects());
		String projectName = null;
		try {
			projectName = ResourcesPlugin.getWorkspace().getRoot()
					.getPersistentProperty(new QualifiedName(ID, "project"));
		} catch (CoreException e) {
			e.printStackTrace();
		}

		BibtexFilter filter = new BibtexFilter();
		FilteredTree tree = new FilteredTree(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL, filter, false);
		viewer = tree.getViewer();
		// drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new AdapterFactoryContentProvider(
				adapterFactory));
		ILabelDecorator decorator = PlatformUI.getWorkbench()
				.getDecoratorManager().getLabelDecorator();
		viewer.setLabelProvider(new DecoratingLabelProvider(
				new AdapterFactoryLabelProvider(adapterFactory), decorator));
		viewer.setSorter(new ViewerSorter());
		viewer.setInput(editingDomain.getResourceSet());
		viewer.getTree().addKeyListener(createDeleteListener());
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

		if (projectName != null) {
			IProject project = ResourcesPlugin.getWorkspace().getRoot()
					.getProject(projectName);
			combo.setSelection(new StructuredSelection(project));
		}
	}

	/**
	 * listener for releasing DEL key. Removes selected document from domain.
	 * 
	 * @return
	 */
	private KeyListener createDeleteListener() {
		KeyListener deleter = new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode != SWT.DEL) {
					return;
				}
				if (viewer.getSelection() instanceof StructuredSelection) {
					StructuredSelection selection = (StructuredSelection) viewer
							.getSelection();
					if (selection.getFirstElement() instanceof Document) {
						Document document = (Document) selection
								.getFirstElement();

						editingDomain.getCommandStack().execute(
								new AbstractCommand() {

									@Override
									public boolean prepare() {
										return true;
									}

									@Override
									public void redo() {
										execute();
									}

									@Override
									public void execute() {
										if (document.eResource() != null) {
											EcoreUtil.remove(document);
										}
									}
								});
						// Save only resources that have actually changed.
						//
						final Map<Object, Object> saveOptions = new HashMap<Object, Object>();
						saveOptions
								.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED,
										Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
						saveOptions.put(Resource.OPTION_LINE_DELIMITER,
								Resource.OPTION_LINE_DELIMITER_UNSPECIFIED);
						// Do the work within an operation because this is a
						// long running
						// activity that modifies the workbench.
						//
						WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
							// This is the method that gets invoked when the
							// operation runs.
							//
							@Override
							public void execute(IProgressMonitor monitor) {
								// Save the resources to the file system.
								//
								boolean first = true;
								for (Resource resource : editingDomain
										.getResourceSet().getResources()) {
									if ((first
											|| !resource.getContents()
													.isEmpty() || Utils
												.isPersisted(resource))
											&& !editingDomain
													.isReadOnly(resource)) {
										try {
											resource.save(saveOptions);
										} catch (Exception exception) {
										}
										first = false;
									}
								}
							}
						};
						try {
							// This runs the options, and shows progress.
							//
							new ProgressMonitorDialog(getSite().getShell())
									.run(true, false, operation);

							// Refresh the necessary state.
							//
							((BasicCommandStack) editingDomain
									.getCommandStack()).saveIsDone();
							firePropertyChange(IEditorPart.PROP_DIRTY);
						} catch (Exception exception) {
							// Something went wrong that shouldn't.
							//
						}
						viewer.setSelection(selection);
						viewer.getTree().forceFocus();
						viewer.refresh();

						// TODO: close Editor which contains the deleted
						// document
					}
				}

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// do nothing
			}
		};
		return deleter;
	}

	/**
	 * create a listener for listening the comboviewer.
	 * 
	 * @return the listener
	 */
	private ISelectionChangedListener createProjectListener() {
		ISelectionChangedListener result = new ISelectionChangedListener() {
			private IProject lastProject = null;

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSelection() == null
						|| event.getSelection().isEmpty()) {
					return;
				}
				if (event.getSelection() instanceof StructuredSelection) {
					Object element = ((StructuredSelection) event
							.getSelection()).getFirstElement();
					if (element.equals(lastProject)) {
						return;
					}
					if (element instanceof IProject) {
						IProject project = (IProject) element;
						if (lastProject == null
								|| editingDomain.getResourceSet()
										.getResources().isEmpty()
								|| requestConfirmation(confirmation)) {
							deleteResources();
							registerResources(project);
							viewer.refresh();
							closeEditors();
							// TODO: refresh Taxonomy
							lastProject = project;
							try {
								ResourcesPlugin
										.getWorkspace()
										.getRoot()
										.setPersistentProperty(
												new QualifiedName(ID, "project"),
												lastProject.getName());
							} catch (CoreException e) {
								e.printStackTrace();
							}
						} else if (lastProject == null) {
							combo.setSelection(null);
						} else {
							combo.setSelection(new StructuredSelection(
									lastProject));
						}
					}
				}
			}
		};
		return result;
	}

	/**
	 * While changing the resources (caused by changing project) all open bibtex
	 * editors have to be closed without saving their content.
	 */
	protected void closeEditors() {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IEditorReference[] references = page.findEditors(null, editorId,
				IWorkbenchPage.MATCH_ID);
		page.closeEditors(references, false);
		references = page
				.findEditors(null, overviewId, IWorkbenchPage.MATCH_ID);
		page.closeEditors(references, false);
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

		closeEditors();
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
			} else if (res.getType() == IResource.FILE && "taxonomy".equals(res.getFileExtension())){
				URI uri = URI.createURI(((IFile) res).getFullPath().toString());
				ModelRegistryPlugin.getModelRegistry().setActiveTaxonomyDocument(uri);
			}
		}
	}

	private void deleteResources() {
		for (Resource resource : editingDomain.getResourceSet().getResources()) {
			resource.unload();
		}
		editingDomain.getResourceSet().getResources().clear();
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
		// manager.add(action1);
		// manager.add(new Separator());
		// manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(openingAction);
		manager.add(new Separator());
		manager.add(markingAction);
		// drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		// manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(refreshAction);
		// manager.add(action2);
		// manager.add(new Separator());
		// drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		openListener = new BibtexOpenListener(editorId,
				IWorkbenchPage.MATCH_INPUT | IWorkbenchPage.MATCH_ID);
		selectionListener = new BibtexOpenListener(overviewId,
				IWorkbenchPage.MATCH_ID);
		/*
		 * ResourcesPlugin.getWorkspace().addResourceChangeListener( new
		 * IResourceChangeListener() {
		 * 
		 * @Override public void resourceChanged(IResourceChangeEvent event) {
		 * refreshProjectCombo();
		 * 
		 * } }, IResourceChangeEvent.POST_CHANGE);
		 */
		refreshAction = new Action() {
			@Override
			public void run() {
				refreshProjectCombo();
				viewer.refresh();
			}
		};
		refreshAction.setText("Refresh");
		refreshAction
				.setToolTipText("Refreshes the tree. Make sure you selected a project before");
		refreshAction.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ELCL_SYNCED));

		markingAction = new Action() {
			@Override
			public void run() {
				TreeSelection select = (TreeSelection) viewer.getSelection();
				if (select == null
						|| !(select.getFirstElement() instanceof Document)) {
					return;
				}
				Document doc = (Document) select.getFirstElement();
				Utils.mark(doc, "Just marking for tests", ID);
				// showMessage("Action 2 executed");
			}
		};
		markingAction.setText("Mark");
		markingAction.setToolTipText("Mark document for ProblemsView");
		markingAction.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_WARN_TSK));
		openingAction = new Action() {
			@Override
			public void run() {
				TreeSelection select = (TreeSelection) viewer.getSelection();
				if (select == null
						|| !(select.getFirstElement() instanceof Document)) {
					return;
				}
				openListener.openEditor(select, true);
				// showMessage("Action 2 executed");
			}
		};
		openingAction.setText("Open");
		openingAction.setToolTipText("Open document in an editor");
	}

	private void hookActions() {
		viewer.addOpenListener(openListener);
		viewer.addSelectionChangedListener(selectionListener);
	}

	private boolean requestConfirmation(String message) {
		return MessageDialog.openConfirm(viewer.getControl().getShell(),
				BibtexEntryView.this.getTitle(), message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private void refreshProjectCombo() {
		StructuredSelection selection = (StructuredSelection) combo
				.getSelection();
		combo.setInput(ResourcesPlugin.getWorkspace().getRoot().getProjects());
		combo.setSelection(selection, true);
		combo.refresh();
	}
}
