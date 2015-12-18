package de.tudresden.slr.model.taxonomy.ui.views;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
import de.tudresden.slr.model.utils.SearchUtils;
import de.tudresden.slr.model.utils.TaxonomyIterator;

public class TaxonomyCheckboxListView extends ViewPart implements
		ISelectionListener, Observer, ICheckStateListener {

	public static final String ID = "de.tudresden.slr.model.taxonomy.ui.views.TaxonomyCheckboxListView";

	private ContainerCheckedTreeViewer viewer;
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
		contributeToActionBars();

		getSite().getWorkbenchWindow().getSelectionService()
				.addPostSelectionListener(this);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
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
					final Term element = (Term) event.getElement();
					setTermChanged(document, element, event.getChecked());
				}
			};
			executeCommand(changeCommand);
		}
	}

	private void setTermChanged(Document document, Term element, boolean add) {
		if (add) {
			addTerm(document, element);
		} else { // delete
			removeTerm(document, element);
		}
	}

	private void addTerm(Document document, Term element) {
		addTerm(document, element, false);
	}

	private void addTerm(Document document, Term element,
			boolean removeSubclasses) {
		final Term copy = EcoreUtil.copy(element);
		if (removeSubclasses) {
			copy.getSubclasses().clear();
		}
		if (element.eContainer() instanceof Term) {
			final Term elementContainer = (Term) element.eContainer();
			Term parent = SearchUtils.findTermInDocument(document,
					elementContainer);
			if (parent == null) {
				addTerm(document, elementContainer, true);
				parent = SearchUtils.findTermInDocument(document,
						elementContainer);
			}
			parent.getSubclasses().add(copy);
		} else { // Model
			document.getTaxonomy().getDimensions().add(copy);
		}
	}

	private void removeTerm(Document document, Term element) {
		final List<Term> parent;
		if (element.eContainer() instanceof Term) {
			final Term elementContainer = (Term) element.eContainer();
			Term elementParent = SearchUtils.findTermInDocument(document,
					elementContainer);
			parent = elementParent.getSubclasses();
			if (parent.size() == 1) {
				removeTerm(document, elementContainer);
			}
		} else { // Model
			parent = document.getTaxonomy().getDimensions();
		}
		parent.removeIf(t -> t.hashCode() == element.hashCode());
	}

	private void executeCommand(Command command) {
		Optional<AdapterFactoryEditingDomain> editingDomain = ModelRegistryPlugin
				.getModelRegistry().getEditingDomain();
		editingDomain.ifPresent((domain) -> domain.getCommandStack().execute(
				command));
	}

	private void setTicks(Document document) {
		viewer.setCheckedElements(new Object[0]);
		TaxonomyIterator iter = new TaxonomyIterator(document.getTaxonomy());
		Stream<Term> stream = StreamSupport.stream(iter.spliterator(), false);
		List<Term> checkedTerms = stream
				.map(term -> SearchUtils.findTermInTaxonomy(term))
				.filter(term -> term.getSubclasses().isEmpty())
				.collect(Collectors.toList());
		viewer.setCheckedElements(checkedTerms.toArray());
	}
}
