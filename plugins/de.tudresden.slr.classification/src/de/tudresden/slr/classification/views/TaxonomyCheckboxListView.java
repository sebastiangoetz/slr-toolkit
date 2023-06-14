package de.tudresden.slr.classification.views;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.TreeViewerEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
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

import de.tudresden.slr.model.TermUtils;
import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.utils.SearchUtils;
import de.tudresden.slr.model.utils.TaxonomyIterator;
import de.tudresden.slr.utils.Utils;
import de.tudresden.slr.utils.taxonomy.manipulation.TermRenamer;

public class TaxonomyCheckboxListView extends ViewPart implements ISelectionListener, Observer, ICheckStateListener {
	public static final String ID = "de.tudresden.slr.model.taxonomy.ui.views.TaxonomyCheckboxListView";
	private ContainerCheckedTreeViewer viewer;
	private TermContentProvider contentProvider;
	private TextCellEditor cellEditor;

	/**
	 * The constructor.
	 */
	public TaxonomyCheckboxListView() {
		ModelRegistryPlugin.getModelRegistry().addObserver(this);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		Optional<Model> m = ModelRegistryPlugin.getModelRegistry().getActiveTaxonomy();
		contentProvider = new TermContentProvider(viewer);
		viewer = new ContainerCheckedTreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(contentProvider);
		DecoratingLabelProvider p = new DecoratingLabelProvider(new DefaultEObjectLabelProvider(),
				new TaxonomyLabelDecorator());
		viewer.setLabelProvider(p);
		viewer.addCheckStateListener(this);
		viewer.setSorter(null);
		viewer.setComparer(new IElementComparer() {

			@Override
			public int hashCode(Object element) {
				if (element instanceof Term t) {
					return TermUtils.termToString(t).hashCode();
				} else {
					return 0;
				}
			}

			@Override
			public boolean equals(Object a, Object b) {
				if (a instanceof Term t1 && b instanceof Term t2) {
					return TermUtils.termToString(t1).equals(TermUtils.termToString(t2));
				} else {
					return false;
				}
			}
		});

		cellEditor = new MyTextCellEditor(viewer.getTree());

		TreeViewerEditor.create(viewer, new ColumnViewerEditorActivationStrategy(viewer) {
			protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED
								&& event.keyCode == SWT.F2);
			}
		}, TreeViewerEditor.DEFAULT);
		enableEditing();

		if (m.isPresent()) {
			viewer.setInput(m.get());
		}
		viewer.expandAll();

		// Create right-click menu
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(viewer.getTree());
		viewer.getTree().setMenu(menu);
		getSite().registerContextMenu(menuManager, viewer);
		getSite().setSelectionProvider(viewer);
		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(),
				"de.tudresden.slr.model.taxonomy.ui.viewer");
		getSite().getWorkbenchWindow().getSelectionService().addPostSelectionListener(this);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private Term findTermByName(String name) {
		for (Object o : viewer.getExpandedElements()) {
			if (o instanceof Term t && t.getName().equals(name)) {
				return t;
			}
		}
		return null;
	}

	private void checkTheseTerms(EList<Term> terms) {
		for (Term t : terms) {
			System.out.println("checking " + t.getName());
			Term tt = findTermByName(t.getName());
			viewer.setChecked(t, true);
			if (!t.getSubclasses().isEmpty()) {
				checkTheseTerms(t.getSubclasses());
			}
		}
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
						EObject taxonomy = NodeModelUtils.findActualSemanticObjectFor(root);
						if (taxonomy instanceof Model) {
							ModelRegistryPlugin.getModelRegistry().setActiveTaxonomy((Model) taxonomy);
						}
					}
				}
			});
		}
	}

	@Override
	public void dispose() {
		ModelRegistryPlugin.getModelRegistry().deleteObserver(this);
		getSite().getWorkbenchWindow().getSelectionService().removePostSelectionListener(this);
		super.dispose();
	}

	@Override
	public void update(Observable o, Object arg) {
		// taxonomy has changed
		if (arg instanceof Model) {
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					viewer.setInput(arg);
				}
			});

			return;
		}
		// document has changed
		if (arg instanceof Document) {
			setTicks((Document) arg);
			Utils.unmark((Document) arg);
			showTaxonomyConfilcts((Document) arg);
		}
	}

	@Override
	public void checkStateChanged(CheckStateChangedEvent event) {
		Optional<Document> activeDocument = ModelRegistryPlugin.getModelRegistry().getActiveDocument();
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
		viewer.update(event.getElement(), null);
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

	private void addTerm(Document document, Term element, boolean removeSubclasses) {
		final Term copy = EcoreUtil.copy(element);
		if (removeSubclasses) {
			copy.getSubclasses().clear();
		}
		if (element.eContainer() instanceof Term) {
			final Term elementContainer = (Term) element.eContainer();
			Term parent = SearchUtils.findTermInDocument(document, elementContainer);
			if (parent == null) {
				addTerm(document, elementContainer, true);
				parent = SearchUtils.findTermInDocument(document, elementContainer);
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
			Term elementParent = SearchUtils.findTermInDocument(document, elementContainer);
			parent = elementParent.getSubclasses();
			if (parent.size() == 1) {
				removeTerm(document, elementContainer);
			}
		} else { // Model
			parent = document.getTaxonomy().getDimensions();
		}
		parent.removeIf(t -> t.getName().equals(element.getName()));
	}

	private void executeCommand(Command command) {
		Optional<AdapterFactoryEditingDomain> editingDomain = ModelRegistryPlugin.getModelRegistry().getEditingDomain();
		editingDomain.ifPresent((domain) -> domain.getCommandStack().execute(command));
	}

	private void setTicks(Document document) {
		viewer.setCheckedElements(new Object[0]);
		TaxonomyIterator iter = new TaxonomyIterator(document.getTaxonomy());
		Stream<Term> stream = StreamSupport.stream(iter.spliterator(), false);
		Model taxonomy = (Model) viewer.getInput();
		List<Term> checkedTerms = stream.map(term -> SearchUtils.findTermInTaxonomy(taxonomy, term))
				.filter(term -> term.getSubclasses().isEmpty()).collect(Collectors.toList());
		viewer.setCheckedElements(checkedTerms.toArray());
	}

	/*
	 * enable in-place renaming of taxonomy terms
	 */
	private void enableEditing() {
		TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.NONE);
		column.setLabelProvider(new TreeLabelProvider());
		column.setEditingSupport(new EditingSupport(viewer) {

			@Override
			protected void setValue(Object element, Object value) {
				if (!value.toString().equals("")) {
					TermRenamer.rename((Term) element, value.toString());
					viewer.refresh();
				}
			}

			@Override
			protected Object getValue(Object element) {
				if (element instanceof Term) {
					return ((Term) element).getName();
				}
				return "";
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return cellEditor;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});

		viewer.getControl().addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {
				column.getColumn().setWidth(((Tree) e.getSource()).getBounds().width);
			}

			@Override
			public void controlMoved(ControlEvent e) {
			}

		});

	}

	private void matchTaxonomy(String[] path, EList<Term> generalTerms, EList<Term> fileTerms, Document document) {
		String[] newPath = new String[] {};
		if (generalTerms.isEmpty() || fileTerms.isEmpty()) {
			return;
		}
		for (Term fileTerm : fileTerms) {
			boolean foundMatch = false;
			for (Term generalTerm : generalTerms) {
				if (fileTerm.getName().equals(generalTerm.getName())) {
					foundMatch = true;
					if (!fileTerm.getSubclasses().isEmpty()) {
						newPath = Arrays.copyOf(path, path.length + 1);
						newPath[newPath.length - 1] = generalTerm.getName();
						matchTaxonomy(newPath, generalTerm.getSubclasses(), fileTerm.getSubclasses(), document);
					}
				}
			}
			if (!foundMatch) {
				String txt = "Taxonomy match conflict: '" + fileTerm.getName() + "' in ";
				if (path.length >= 1) {
					txt += "'" + path[path.length - 1] + "'";
				} else {
					txt += "root";
				}

				// search for move quickfix and set path2
				String path2String = getPossibleMovePath(fileTerm.getName(),
						ModelRegistryPlugin.getModelRegistry().getActiveTaxonomy().get().getDimensions(), "");

				String pathString = "";
				for (String entry : path) {
					pathString += "/" + entry;
				}
				pathString += "/" + fileTerm.getName();

				Utils.mark(document, txt, pathString, path2String, ID);
			}
		}
	}

	public String getPossibleMovePath(String search, EList<Term> terms, String path) {
		for (Term term : terms) {
			String newPath = path + "/" + term.getName();
			if (term.getName().equals(search)) {
				return newPath;
			}
			if (!term.getSubclasses().isEmpty()) {
				String result = getPossibleMovePath(search, term.getSubclasses(), newPath);
				if (!result.equals("/")) {
					return result;
				}
			}
		}
		return "/";
	}

	public void showTaxonomyConfilcts(Document document) {
		EList<Term> generalTerms = ModelRegistryPlugin.getModelRegistry().getActiveTaxonomy().get().getDimensions();
		EList<Term> fileTerms = document.getTaxonomy().getDimensions();

		if (generalTerms.isEmpty())
			System.out.println("generalTerms empty");
		if (fileTerms.isEmpty())
			System.out.println("fileTerms empty");

		if (!generalTerms.isEmpty() && !fileTerms.isEmpty()) {
			matchTaxonomy(new String[] {}, generalTerms, fileTerms, document);
		}

	}

	class TreeLabelProvider extends ColumnLabelProvider {
		public String getText(Object element) {
			if (element instanceof Term) {
				Map<Document, Term> termsInDocuments = SearchUtils.findDocumentsWithTerm((Term) element);
				return ((Term) element).getName() + " (" + termsInDocuments.size() + ")";
			}
			return element.toString();
		}
	}

	class MyTextCellEditor extends TextCellEditor {
		int minHeight = 0;

		public MyTextCellEditor(Tree tree) {
			super(tree, SWT.BORDER);
			Text txt = (Text) getControl();

			Font fnt = txt.getFont();
			FontData[] fontData = fnt.getFontData();
			if (fontData != null && fontData.length > 0) {
				minHeight = fontData[0].getHeight() + 10;
			}
		}

		public LayoutData getLayoutData() {
			LayoutData data = super.getLayoutData();
			if (minHeight > 0) {
				data.minimumHeight = minHeight;
			}
			return data;
		}
	}
}
