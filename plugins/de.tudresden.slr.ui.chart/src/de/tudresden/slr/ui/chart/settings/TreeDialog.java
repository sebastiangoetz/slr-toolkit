package de.tudresden.slr.ui.chart.settings;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.xtext.ui.label.DefaultEObjectLabelProvider;

import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;

import java.util.Optional;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class TreeDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	TreeViewer treeViewer;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public TreeDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return treeViewer.getSelection();
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(371, 404);
		shell.setText(getText());
		
		treeViewer = new TreeViewer(shell, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setBounds(10, 30, 346, 307);
		buildTree(treeViewer);
		
		Label lblPleaseSelect = new Label(shell, SWT.NONE);
		lblPleaseSelect.setBounds(10, 10, 79, 15);
		lblPleaseSelect.setText("Please Select");
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		btnNewButton.setBounds(281, 340, 75, 25);
		btnNewButton.setText("Ok");

	}
	
	private void buildTree(TreeViewer treeViewer) {	
		
		Optional<Model> m = ModelRegistryPlugin.getModelRegistry().getActiveTaxonomy();
		TreeContentProvider contentProvider = new TreeContentProvider(treeViewer);		
		treeViewer.setContentProvider(contentProvider);
		treeViewer.setLabelProvider(new DefaultEObjectLabelProvider());
		treeViewer.setSorter(null);
		treeViewer.setInput(m.get());	
		
	}
}
