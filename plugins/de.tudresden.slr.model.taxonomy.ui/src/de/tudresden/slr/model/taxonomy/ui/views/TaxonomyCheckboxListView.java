package de.tudresden.slr.model.taxonomy.ui.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.Queue;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.label.DefaultEObjectLabelProvider;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;

public class TaxonomyCheckboxListView extends ViewPart implements
		ISelectionListener, Observer, ICheckStateListener {

	public static final String ID = "de.tudresden.slr.model.taxonomy.ui.views.TaxonomyCheckboxListView";

	private ContainerCheckedTreeViewer viewer;
	private Action doubleClickAction;
	private ViewContentProvider contentProvider;

	class ViewContentProvider implements IStructuredContentProvider,
			ITreeContentProvider {

		public ViewContentProvider(Viewer v) {
		}

		@Override
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {

		}

		@Override
		public void dispose() {
		}

		@Override
		public Object[] getElements(Object parent) {
			return getChildren(parent);
		}

		@Override
		public Object getParent(Object child) {
			if (child instanceof EObject) {
				return ((EObject) child).eContainer();
			}
			return null;
		}

		@Override
		public Object[] getChildren(Object parent) {
			if (parent instanceof Model) {
				return ((Model) parent).getDimensions().toArray();
			}
			if (parent instanceof Term) {
				return ((Term) parent).getSubclasses().toArray();
			}
			// if (parent instanceof EObject) {
			// return ((EObject) parent).eContents().toArray();
			// }
			return new Object[0];
		}

		@Override
		public boolean hasChildren(Object parent) {
			if (parent instanceof EObject) {
				return getChildren(parent).length > 0;
			}
			return false;
		}
	}

	/**
	 * The constructor.
	 */
	public TaxonomyCheckboxListView() {
		ModelRegistryPlugin.getModelRegistry().addObserver(this);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		viewer = new ContainerCheckedTreeViewer(parent, SWT.MULTI
				| SWT.H_SCROLL | SWT.V_SCROLL);
		contentProvider = new ViewContentProvider(viewer);
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(new DefaultEObjectLabelProvider());
		viewer.addCheckStateListener(this);
		viewer.setSorter(null);

		getSite().setSelectionProvider(viewer);

		// Create the help context id for the viewer's control
		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(viewer.getControl(),
						"de.tudresden.slr.model.taxonomy.ui.viewer");
		hookDoubleClickAction();
		contributeToActionBars();

		getSite().getWorkbenchWindow().getSelectionService()
				.addPostSelectionListener(this);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part instanceof XtextEditor && !selection.isEmpty()) {
			final XtextEditor editor = (XtextEditor) part;
			final IXtextDocument document = editor.getDocument();

			document.readOnly(new IUnitOfWork.Void<XtextResource>() {
				@Override
				public void process(XtextResource resource) throws Exception {
					IParseResult parseResult = resource.getParseResult();
					if (parseResult != null) {
						ICompositeNode root = parseResult.getRootNode();
						EObject taxonomy = NodeModelUtils
								.findActualSemanticObjectFor(root);
						if (taxonomy instanceof Model) {
							ModelRegistryPlugin.getModelRegistry()
									.setActiveTaxonomy((Model) taxonomy);
						}
					}
				}
			});
		}
	}

	@Override
	public void dispose() {
		getSite().getWorkbenchWindow().getSelectionService()
				.removePostSelectionListener(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		// taxonomy has changed
		if (arg instanceof Model) {
			viewer.setInput(arg);
		}
		// document has changed
		if (arg instanceof Document) {
			setTicks((Document) arg);
		}
		viewer.expandAll();
	}

	@Override
	public void checkStateChanged(CheckStateChangedEvent event) {
		Optional<Document> activeDocument = ModelRegistryPlugin
				.getModelRegistry().getActiveDocument();
		if (activeDocument.isPresent()) {
			Document document = activeDocument.get();
			Command changeCommand = new ExecuteCommand() {
				@Override
				public void execute() {
					setTermChanged(document, (Term) event.getElement(),
							event.getChecked());
				}
			};
			executeCommand(changeCommand);
		}
	}

	private Term findTerm(Document document, Term term) {
		Queue<Term> queue = new LinkedList<>(document.getTaxonomy()
				.getDimensions());
		while (!queue.isEmpty()) {
			Term head = queue.poll();
			if (head.getUuid().equals(term.getUuid())) {
				return head;
			}
			queue.addAll(head.getSubclasses());
		}
		return null;
	}

	private void setTermChanged(Document document, Term element, boolean add) {
		if (add) {
			if (element.eContainer() instanceof Term) {
				Term parent = findTerm(document, (Term) element.eContainer());
				parent.getSubclasses().add(EcoreUtil.copy(element));
			} else { // Model
				document.getTaxonomy().getDimensions()
						.add(EcoreUtil.copy(element));
			}
		} else { // delete
			Iterator<Term> iter = null;
			if (element.eContainer() instanceof Term) {
				Term parent = findTerm(document, (Term) element.eContainer());
				iter = parent.getSubclasses().iterator();
			} else { // Model
				iter = document.getTaxonomy().getDimensions().iterator();
			}
			while (iter.hasNext()) {
				Term next = iter.next();
				if (next.getUuid().equals(element.getUuid())) {
					iter.remove();
					break;
				}
			}
		}
	}

	private void executeCommand(Command command) {
		Optional<AdapterFactoryEditingDomain> editingDomain = ModelRegistryPlugin
				.getModelRegistry().getEditingDomain();
		editingDomain.ifPresent((domain) -> domain.getCommandStack().execute(
				command));
	}

	private void setTicks(Document document) {
		viewer.setCheckedElements(new Object[0]);
		Queue<Term> queue = new LinkedList<>(document.getTaxonomy()
				.getDimensions());
		List<Term> checkedTerms = new ArrayList<>();
		while (!queue.isEmpty()) {
			Term queued = queue.poll();
			Term term = getTerm(queued);
			if (term.getSubclasses().isEmpty()) {
				checkedTerms.add(term);
			}
			queue.addAll(queued.getSubclasses());
		}
		viewer.setCheckedElements(checkedTerms.toArray());
	}

	private Term getTerm(Term term) {
		Optional<Model> taxonomy = ModelRegistryPlugin.getModelRegistry()
				.getActiveTaxonomy();
		if (taxonomy.isPresent()) {
			Queue<Term> queue = new LinkedList<>(taxonomy.get().getDimensions());
			while (!queue.isEmpty()) {
				Term queued = queue.poll();
				if (queued.getName().equals(term.getName())) {
					if (queued.eContainer() instanceof Model) {
						return queued;
					} else if (queued.eContainer() instanceof Term
							&& term.eContainer() instanceof Term) {
						Term parent = (Term) queued.eContainer();
						Term otherParent = (Term) term.eContainer();
						if (parent.getName().equals(otherParent.getName())) {
							return queued;
						}
					}
				} else {
					queue.addAll(queued.getSubclasses());
				}
			}
		}
		return term;
	}
}
