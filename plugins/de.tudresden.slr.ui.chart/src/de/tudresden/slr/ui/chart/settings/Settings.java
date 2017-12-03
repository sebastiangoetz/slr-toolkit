package de.tudresden.slr.ui.chart.settings;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.xtext.ui.label.DefaultEObjectLabelProvider;

import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.ui.chart.logic.BarChartGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

public class Settings extends Dialog {
	private ViewContentProvider contentProvider;

	protected Object result;
	protected Shell shell;
	private Text text;
	private TreeViewer treeViewer;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public Settings(Shell parent, int style) {
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
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(805, 618);
		shell.setText(getText());
		shell.setLayout(null);
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 10, 779, 537);
		
		TabItem tbtmY = new TabItem(tabFolder, SWT.NONE);
		tbtmY.setText("Y");
		
		TabItem tbtmX = new TabItem(tabFolder, SWT.NONE);
		Composite composite1 = new Composite(tabFolder, SWT.NONE);
		
		tbtmX.setControl(composite1);
		
		GridLayout gl_composite1 = new GridLayout(2, false);
		composite1.setLayout(gl_composite1);
		
		TreeViewer treeViewer = new TreeViewer(composite1, SWT.BORDER);
		
		contentProvider = new ViewContentProvider(treeViewer);
		treeViewer.setContentProvider(contentProvider);
		treeViewer.setLabelProvider(new DefaultEObjectLabelProvider());
		Optional<Model> m = ModelRegistryPlugin.getModelRegistry().getActiveTaxonomy();
		treeViewer.setInput(m.get());
		
		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		new Label(composite1, SWT.NONE);
		
		tbtmX.setText("X");		
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmX.setControl(composite_2);
		
		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmX.setControl(composite_3);
		
		TabItem tbtmTitel = new TabItem(tabFolder, SWT.NONE);
		tbtmTitel.setText("Titel");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmTitel.setControl(composite);
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.marginTop = 5;
		gl_composite.marginRight = 5;
		gl_composite.marginLeft = 5;
		gl_composite.marginBottom = 5;
		composite.setLayout(gl_composite);
		
		Label lblTitel = new Label(composite, SWT.NONE);
		lblTitel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTitel.setText("Titel");
		
		text = new Text(composite, SWT.BORDER);
		GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = 199;
		text.setLayoutData(gd_text);
		
		Label lblFarbe = new Label(composite, SWT.NONE);
		lblFarbe.setText("Farbe");
		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColorDialog dlg = new ColorDialog(shell);
				dlg.setRGB(new RGB(0, 0, 255));
				RGB rgb = dlg.open();
				 if (rgb != null) {
				      Color color = new Color(shell.getDisplay(), rgb);
				      composite.setBackground(color);
				      
				      color.dispose();
				    }
			}
		});
		btnNewButton.setText("Color");
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("New Label");
		
		Combo combo = new Combo(composite, SWT.READ_ONLY);
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_combo.widthHint = 174;
		combo.setLayoutData(gd_combo);
		combo.add("Times");
		combo.add("Comics");
		combo.add("Easy");
		combo.select(0);
		
		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setText(combo.getItem(combo.getSelectionIndex()));
		new Label(composite, SWT.NONE);
		
		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("New Item");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(composite_1);
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btnNewButton_1.setBounds(714, 553, 75, 25);
		btnNewButton_1.setText("Close");
		
		Button btnNewButton_2 = new Button(shell, SWT.NONE);
		btnNewButton_2.setBounds(633, 553, 75, 25);
		btnNewButton_2.setText("Apply");

	}
}
