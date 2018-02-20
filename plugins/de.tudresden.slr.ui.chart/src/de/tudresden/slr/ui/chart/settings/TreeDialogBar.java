package de.tudresden.slr.ui.chart.settings;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.xtext.ui.label.DefaultEObjectLabelProvider;

import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.ui.chart.logic.TermSort;

public class TreeDialogBar extends Dialog implements SelectionListener{

	protected Object result;
	protected Shell shlTerms;
	private TreeViewer treeViewer;
	private Term selectedTerm;
	private Tree tree;
	private TermSort termSort;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public TreeDialogBar(Shell parent, int style) {
		super(parent, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Map.Entry<Term,TermSort> open() {
		createContents();
		shlTerms.open();
		shlTerms.layout();
		Display display = getParent().getDisplay();
		while (!shlTerms.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return new  AbstractMap.SimpleEntry<Term,TermSort>(selectedTerm,termSort);
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlTerms = new Shell(getParent(), getStyle());
		shlTerms.setSize(309, 441);
		shlTerms.setText("Terms");
		shlTerms.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(shlTerms, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		
		Label lblPleaseSelect = new Label(composite, SWT.NONE);
		lblPleaseSelect.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblPleaseSelect.setText("Please select a term:");
		
		Composite composite_1 = new Composite(shlTerms, SWT.NONE);
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_composite_1.widthHint = 107;
		composite_1.setLayoutData(gd_composite_1);
		composite_1.setLayout(new GridLayout(1, false));
		
		treeViewer = new TreeViewer(composite_1, SWT.BORDER);
		tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tree.addSelectionListener(this);
		buildTree(treeViewer);
		
		Composite composite_2 = new Composite(shlTerms, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_2.setLayout(new GridLayout(3, false));
		
		Label lblNewLabel = new Label(composite_2, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		lblNewLabel.setSize(92, 15);
		lblNewLabel.setText("Group Series Per: ");
		
		Button btnYear = new Button(composite_2, SWT.NONE);
		btnYear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				termSort = TermSort.YEAR;
				shlTerms.dispose();
			}
		});
		btnYear.setText("Year");
		
		Button btnSubclasses = new Button(composite_2, SWT.NONE);
		btnSubclasses.setText("Subclasses");
		btnSubclasses.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				termSort  = TermSort.SUBCLASS;
				shlTerms.dispose();
			}
		});

	}
	
	private void buildTree(TreeViewer treeViewer) {	
		
		Optional<Model> m = ModelRegistryPlugin.getModelRegistry().getActiveTaxonomy();
		TreeContentProvider contentProvider = new TreeContentProvider(treeViewer);		
		treeViewer.setContentProvider(contentProvider);
		treeViewer.setLabelProvider(new DefaultEObjectLabelProvider());
		treeViewer.setSorter(null);
		treeViewer.setInput(m.get());	
		
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if(e.getSource() == tree ) {
			if(!(tree.getSelectionCount() > 1)) {
				
				IStructuredSelection currentSelection = (IStructuredSelection) treeViewer.getSelection();
				
				selectedTerm = (Term) currentSelection.getFirstElement();
				
			}
		}
		
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {}
}
