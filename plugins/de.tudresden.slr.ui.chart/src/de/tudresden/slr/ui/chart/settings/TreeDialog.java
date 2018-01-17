package de.tudresden.slr.ui.chart.settings;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.xtext.ui.label.DefaultEObjectLabelProvider;

import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.ui.chart.logic.TermSort;
import de.tudresden.slr.ui.chart.settings.pages.SeriesPageBar;

import java.util.Optional;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class TreeDialog extends Dialog implements SelectionListener{

	protected Object result;
	protected Shell shell;
	private TreeViewer treeViewer;
	private Term selectedTerm;
	private Tree tree;
	private SeriesPageBar seriesPageParent;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public TreeDialog(Shell parent, int style) {
		super(parent, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open(SeriesPageBar seriesPageParent) {
		this.seriesPageParent = seriesPageParent;
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return selectedTerm;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(371, 409);
		shell.setText(getText());
		
		treeViewer = new TreeViewer(shell, SWT.BORDER);
		tree = treeViewer.getTree();
		tree.addSelectionListener(this);
		tree.setBounds(10, 36, 346, 298);
		buildTree(treeViewer);
		
		
		Label lblPleaseSelect = new Label(shell, SWT.NONE);
		lblPleaseSelect.setBounds(10, 10, 244, 23);
		lblPleaseSelect.setText("Please select a term:");
		
		Button btnYear = new Button(shell, SWT.NONE);
		btnYear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				seriesPageParent.termSort = TermSort.YEAR;
				shell.dispose();
			}
		});
		btnYear.setBounds(281, 345, 75, 25);
		btnYear.setText("Year");
		
		Button btnSubclasses = new Button(shell, SWT.NONE);
		btnSubclasses.setBounds(200, 345, 75, 25);
		btnSubclasses.setText("Subclasses");
		btnSubclasses.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				seriesPageParent.termSort  = TermSort.SUBCLASS;
				shell.dispose();
			}
		});
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(102, 350, 92, 15);
		lblNewLabel.setText("Group Series Per: ");

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
