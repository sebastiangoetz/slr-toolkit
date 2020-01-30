package de.tudresden.slr.model.bibtex.ui.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
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
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
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
import org.eclipse.ui.progress.UIJob;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.bibtex.impl.DocumentImpl;
import de.tudresden.slr.model.bibtex.ui.util.Utils;
import de.tudresden.slr.model.bibtex.util.BibtexResourceImpl;
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
	protected AdapterFactory adapterFactory;
	protected AdapterFactoryEditingDomain editingDomain;
	private TreeViewer viewer;
	private Action refreshAction;
	private Action markingAction;
	private Action openingAction;
	private Action mergingAction;
	private ComboViewer combo;
	private BibtexOpenListener openListener, selectionListener;

	/**
	 * The constructor.
	 */
	public BibtexEntryView() {
		super();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.addResourceChangeListener(markerChangeListener);
		workspace.addResourceChangeListener(projectChangeListener);
		initializeEditingDomain();
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
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
		FilteredTree tree = new FilteredTree(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL, filter, false);
		viewer = tree.getViewer();
		viewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
		ILabelDecorator decorator = PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator();
		viewer.setLabelProvider(
				new DecoratingLabelProvider(new AdapterFactoryLabelProvider(adapterFactory), decorator));
		viewer.setSorter(new ViewerSorter());
		viewer.setInput(editingDomain.getResourceSet());
		viewer.getTree().addKeyListener(createDeleteListener());
		viewer.getTree().addKeyListener(createRefreshListener());
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
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
			combo.setSelection(new StructuredSelection(project));
		}
	}

	private KeyListener createRefreshListener() {
		KeyListener refresher = new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.F5) {
					ISelection s = combo.getSelection();
					if (s != null && s instanceof IStructuredSelection) {
						IProject p = (IProject) (((IStructuredSelection) s).getFirstElement());
						deleteResources();
						registerResources(p);
						viewer.refresh();
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		};
		return refresher;
	}

	@Override
	public void dispose() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.removeResourceChangeListener(markerChangeListener);
		workspace.removeResourceChangeListener(projectChangeListener);
		super.dispose();
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
					StructuredSelection selection = (StructuredSelection) viewer.getSelection();
					if (selection.getFirstElement() instanceof Document) {
						Document document = (Document) selection.getFirstElement();

						editingDomain.getCommandStack().execute(new AbstractCommand() {
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
						final Map<Object, Object> saveOptions = new HashMap<Object, Object>();
						saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED,
								Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
						saveOptions.put(Resource.OPTION_LINE_DELIMITER, Resource.OPTION_LINE_DELIMITER_UNSPECIFIED);

						// Do the work within an operation because this is a
						// long running activity that modifies the workbench.
						WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
							// This is the method that gets invoked when the
							// operation runs.
							//
							@Override
							public void execute(IProgressMonitor monitor) {
								// Save the resources to the file system.
								//
								boolean first = true;
								for (Resource resource : editingDomain.getResourceSet().getResources()) {
									if ((first || !resource.getContents().isEmpty() || Utils.isPersisted(resource))
											&& !editingDomain.isReadOnly(resource)) {
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
							new ProgressMonitorDialog(getSite().getShell()).run(true, false, operation);

							// Refresh the necessary state.
							//
							((BasicCommandStack) editingDomain.getCommandStack()).saveIsDone();
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
	 * Listener for listening to changes to the underlying resources.
	 */
	private IResourceChangeListener markerChangeListener = new IResourceChangeListener() {
		private MarkerVisitor markerVisitor = new MarkerVisitor();

		public void resourceChanged(IResourceChangeEvent event) {
			IResourceDelta delta = event.getDelta();
			if (delta != null) {
				try {
					delta.accept(markerVisitor);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
	};
	/**
	 * Listens to resource changes and updates the BibtexEntryView when projects are
	 * added or removed.
	 */
	private IResourceChangeListener projectChangeListener = new IResourceChangeListener() {
		/**
		 * Fired whenever a resource is changed
		 */
		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			if (event.getType() != IResourceChangeEvent.POST_CHANGE
					&& event.getType() != IResourceChangeEvent.PRE_DELETE) {
				return;
			}
			if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
				handleResourceChangeEvent(event);
			} else if (event.getType() == IResourceChangeEvent.PRE_DELETE) {
				handleResourceDeleteEvent(event);
			}
		}

		/**
		 * Handle deletion of resources (does only handle deletion of projects)
		 * 
		 * @param event
		 */
		private void handleResourceDeleteEvent(IResourceChangeEvent event) {
			IResource resource = event.getResource();
			if (!(resource instanceof IProject)) {
				return;
			}
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IProject[] input = (IProject[]) combo.getInput();
					IStructuredSelection selection = combo.getStructuredSelection();

					// Deleted project is selected
					if (selection.getFirstElement() == resource) {
						if (input.length > 1) {
							int currentIndex = combo.getCombo().getSelectionIndex();
							Object newItem = combo.getElementAt(currentIndex + 1);
							if (newItem != null) {
								combo.setSelection(new StructuredSelection(newItem));
							} else {
								newItem = combo.getElementAt(currentIndex - 1);
								if (newItem != null) {
									combo.setSelection(new StructuredSelection(newItem));
								}
							}
						} else if (input.length == 1) {
							combo.setSelection(StructuredSelection.EMPTY);
						}
					}
					combo.remove(resource);
				}
			});
		}

		/**
		 * Handles addition of new resources (projects only)
		 * 
		 * @param event
		 */
		private void handleResourceChangeEvent(IResourceChangeEvent event) {
			List<IProject> projects = getAddedProjects(event.getDelta());
			if (projects.size() > 0) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						refreshAction.run();
						if (projects.size() > 0 && (combo.getSelection() == null || combo.getSelection().isEmpty())) {
							combo.setSelection(new StructuredSelection(projects.get(0)));
						}
					}
				});
			}
		}

		/**
		 * Returns a list of newly added projects.
		 * 
		 * @param resourceDelta
		 * @return List of newly added projects.
		 */
		private List<IProject> getAddedProjects(IResourceDelta resourceDelta) {
			final List<IProject> projects = new ArrayList<IProject>();
			try {
				resourceDelta.accept(new IResourceDeltaVisitor() {
					public boolean visit(IResourceDelta delta) throws CoreException {
						if (delta.getKind() == IResourceDelta.ADDED
								&& delta.getResource().getType() == IResource.PROJECT) {
							IProject project = (IProject) delta.getResource();
							projects.add(project);
							return false;
						}
						return delta.getResource().getType() == IResource.ROOT;
					}
				});
			} catch (CoreException e) {
				e.printStackTrace();
			}
			return projects;
		}
	};

	/**
	 * Visitor for marker changes
	 */
	private class MarkerVisitor implements IResourceDeltaVisitor {
		/**
		 * Update the BibtextEntryView when markers change.
		 */
		public boolean visit(IResourceDelta delta) throws CoreException {
			if (delta == null) {
				return false;
			}

			IMarkerDelta[] markerDeltas = delta.getMarkerDeltas();

			if (markerDeltas.length > 0) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						refreshAction.run();
					}
				});
			}
			return true;
		}
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
				if (event.getSelection() == null || event.getSelection().isEmpty()) {
					deleteResources();
					return;
				}
				if (event.getSelection() instanceof StructuredSelection) {
					Object element = ((StructuredSelection) event.getSelection()).getFirstElement();
					if (element.equals(lastProject)) {
						return;
					}
					if (element instanceof IProject) {
						IProject project = (IProject) element;
						deleteResources();
						registerResources(project);
						viewer.refresh();
						closeEditors();
						lastProject = project;
						try {
							ResourcesPlugin.getWorkspace().getRoot()
									.setPersistentProperty(new QualifiedName(ID, "project"), lastProject.getName());
						} catch (CoreException e) {
							e.printStackTrace();
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
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IEditorReference[] references = page.findEditors(null, editorId, IWorkbenchPage.MATCH_ID);
		page.closeEditors(references, false);
		references = page.findEditors(null, overviewId, IWorkbenchPage.MATCH_ID);
		page.closeEditors(references, false);
	}

	/**
	 * This sets up the editing domain for the model editor. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void initializeEditingDomain() {
		ModelRegistryPlugin.getModelRegistry().getEditingDomain().ifPresent((domain) -> editingDomain = domain);
		// Add a listener to set the most recent command's affected objects
		// to be the selection of the viewer with focus.
		if (editingDomain == null) {
			System.err.println("[BibtexEntryView#initializeEditingDomain] uninitailised editing domain");
			return;
		}
		adapterFactory = editingDomain.getAdapterFactory();
		editingDomain.getCommandStack().addCommandStackListener(new CommandStackListener() {

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
			if (res.getType() == IResource.FILE && "bib".equals(res.getFileExtension())) {
				URI uri = URI.createURI(((IFile) res).getFullPath().toString());
				editingDomain.getResourceSet().getResource(uri, true);
			} else if (res.getType() == IResource.FILE && "taxonomy".equals(res.getFileExtension())) {
				ModelRegistryPlugin.getModelRegistry().setTaxonomyFile((IFile) res);
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
		if (theSelection != null && !theSelection.isEmpty()) {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					// Try to select the items in the current content viewer of the editor.
					if (BibtexEntryView.this != null) {
						BibtexEntryView.this.viewer.setSelection(new StructuredSelection(theSelection.toArray()), true);
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
				IStructuredSelection s = (IStructuredSelection) viewer.getSelection();
				if(s.getFirstElement() instanceof DocumentImpl) {
					fillContextMenu(manager);
				}
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(openingAction);
		manager.add(new Separator());
		manager.add(mergingAction);
		manager.add(new Separator());
		manager.add(markingAction);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(refreshAction);
	}

	private void makeActions() {
		openListener = new BibtexOpenListener(editorId, IWorkbenchPage.MATCH_INPUT | IWorkbenchPage.MATCH_ID);
		selectionListener = new BibtexOpenListener(overviewId, IWorkbenchPage.MATCH_ID);

		refreshAction = new Action() {
			@Override
			public void run() {
				refreshProjectCombo();
				viewer.refresh();
			}
		};
		refreshAction.setText("Refresh");
		refreshAction.setToolTipText("Refreshes the tree. Make sure you selected a project before");
		refreshAction.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_SYNCED));

		markingAction = new Action() {
			@Override
			public void run() {
				TreeSelection select = (TreeSelection) viewer.getSelection();
				if (select == null || !(select.getFirstElement() instanceof Document)) {
					return;
				}
				Document doc = (Document) select.getFirstElement();
				Utils.mark(doc, "Just marking for tests", ID);
			}
		};
		markingAction.setText("Mark");
		markingAction.setToolTipText("Mark document for ProblemsView");
		markingAction.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_WARN_TSK));

		openingAction = new Action() {
			@Override
			public void run() {
				TreeSelection select = (TreeSelection) viewer.getSelection();
				if (select == null || !(select.getFirstElement() instanceof Document)) {
					return;
				}
				openListener.openEditor(select, true);
			}
		};
		openingAction.setText("Open");
		openingAction.setToolTipText("Open document in an editor");

		mergingAction = new Action() {
			@Override
			public void run() {
				TreeSelection select = (TreeSelection) viewer.getSelection();
				// TODO: allow more than 9 documents?
				if (select.size() > 1 && select.size() < 9) {
					List<BibtexResourceImpl> resourceList = new ArrayList<BibtexResourceImpl>();
					for (@SuppressWarnings("unchecked")
					Iterator<Object> i = select.iterator(); i.hasNext();) {
						Object o = i.next();
						if (!(o instanceof BibtexResourceImpl)) {
							// FIXME: continue??
							return;
						}
						resourceList.add((BibtexResourceImpl) o);
					}

					BibtexMergeData mergeData = new BibtexMergeData(resourceList);
					ProgressBarDemo pb = new ProgressBarDemo(getSite().getShell());
					UIJob job = new UIJob(Display.getCurrent(), "My Job") {
					    @Override
					    public IStatus runInUIThread(IProgressMonitor monitor) {
					    	pb.setBlockOnOpen(false);
					        mergeData.createSimilarityMatrix();
					        mergeData.extractConflicts();
					        if (pb != null) pb.close();
					        return Status.OK_STATUS;
					    }

					};
					job.setUser(true);
					job.schedule(500);
					pb.open();

					BibtexMergeDialog dialog = new BibtexMergeDialog(
							PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), mergeData);
					if (dialog.open() == Window.OK) {
						refreshAction.run();
					}
				} else {
					return;
				}
			}
		};
		mergingAction.setText("Merge BibTeX files...");
		mergingAction.setToolTipText("Merge two or more BibTeX files");
	}

	private void hookActions() {
		viewer.addOpenListener(openListener);
		viewer.addSelectionChangedListener(selectionListener);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private void refreshProjectCombo() {
		StructuredSelection selection = (StructuredSelection) combo.getSelection();
		combo.setInput(ResourcesPlugin.getWorkspace().getRoot().getProjects());
		combo.setSelection(selection, true);
		combo.refresh();
	}
}
