package de.tudresden.slr.ui.chart.settings;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.xtext.ui.label.DefaultEObjectLabelProvider;

import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;

import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.SelectionAdapter;

public class SettingsDialog extends Dialog implements SelectionListener{

	protected Object result;
	protected Shell shell;
	private Composite stackComposite;
	private Combo combo;
	private StackLayout sl_stackComposite;
	private Composite pageBarChart;
	private Composite pageBubbleChart;
	private Composite pageHeatChart; 
	private TreeContentProvider contentProvider;
	private List list;
	private Optional<Model> m;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public SettingsDialog(Shell parent, int style) {
		super(parent, style);
		setText("Chart Settings");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		m = ModelRegistryPlugin.getModelRegistry().getActiveTaxonomy();
		if(!m.isPresent()) {
			MessageDialog.openError(shell, "No taxonomy found", "Please build or load a taxonomy before creating a chart!");
			return null;
		}
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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE);
		shell.setSize(750, 600);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));
		
		Composite northComposite = new Composite(shell, SWT.NONE);
		northComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		northComposite.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(northComposite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Please select your chart type: ");
		
		combo = new Combo(northComposite, SWT.NONE | SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		combo.add("Bar");
		combo.add("Bubble");
		combo.add("Heat");
		combo.select(-1);
		
		stackComposite = new Composite(shell, SWT.NONE);
		stackComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sl_stackComposite = new StackLayout();
		stackComposite.setLayout(sl_stackComposite);
		
		pageBarChart = new Composite (stackComposite, SWT.NONE);
		pageBubbleChart = new Composite (stackComposite, SWT.NONE);
		pageHeatChart = new Composite (stackComposite, SWT.NONE);
		
		pageBarChart.setLayout(new FillLayout());
		
		TabFolder tabFolder1 = new TabFolder(pageBarChart, SWT.NONE);
		
		TabItem tbtmNewItem = new TabItem(tabFolder1, SWT.NONE);
		tbtmNewItem.setText("X - Data selection");
		
		Composite barDataCompositeContainer = new Composite(tabFolder1, SWT.NONE);
		tbtmNewItem.setControl(barDataCompositeContainer);
		barDataCompositeContainer.setLayout(new GridLayout(1, false));
		
		Composite barDataComposite_North = new Composite(barDataCompositeContainer, SWT.BORDER);
		barDataComposite_North.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		barDataComposite_North.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Label lblNewLabel_1 = new Label(barDataComposite_North, SWT.CENTER);
		lblNewLabel_1.setText("Select a therm");
		
		Label lblNewLabel_2 = new Label(barDataComposite_North, SWT.CENTER);
		lblNewLabel_2.setText("Select a therm");
		
		SashForm barDataSash_Centre = new SashForm(barDataCompositeContainer, SWT.NONE);
		barDataSash_Centre.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TreeViewer treeViewer = new TreeViewer(barDataSash_Centre, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		
		buildTree(treeViewer);
		
		list = new List(barDataSash_Centre, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		barDataSash_Centre.setWeights(new int[] {1, 1});
		
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				
				ISelection selection = treeViewer.getSelection();
				if (selection == null || !(selection instanceof IStructuredSelection)) {
					return;
				}
				IStructuredSelection currentSelection = (IStructuredSelection) selection;
				Term t = (Term) currentSelection.getFirstElement();
				list.removeAll();
				if(!t.getSubclasses().isEmpty()) {
					for(Term term : t.getSubclasses())
						list.add(term.getName());
				}
			}
		});
		
		Composite barDataComposite_South = new Composite(barDataCompositeContainer, SWT.BORDER);
		barDataComposite_South.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		barDataComposite_South.setLayout(new GridLayout(2, false));
		new Label(barDataComposite_South, SWT.NONE);
		
		Button lockButton = new Button(barDataComposite_South, SWT.NONE);
		lockButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		lockButton.setBounds(0, 0, 75, 25);
		lockButton.setText("Lock");
		
		
		
		TabItem tbtmNewItem_1 = new TabItem(tabFolder1, SWT.NONE);
		tbtmNewItem_1.setText("Stuff");	
		
		pageBubbleChart.setLayout(new FillLayout());
		TabFolder tabFolder2 = new TabFolder(pageBubbleChart, SWT.NONE);
		
		TabItem tbtmNewItem21 = new TabItem(tabFolder2, SWT.NONE);
		tbtmNewItem21.setText("Test 3");
		
		TabItem tbtmNewItem22 = new TabItem(tabFolder2, SWT.NONE);
		tbtmNewItem22.setText("Test 4");
		
		pageHeatChart.setLayout(new FillLayout());
		TabFolder tabFolder3 = new TabFolder(pageHeatChart, SWT.NONE);
		
		TabItem tbtmNewItem31 = new TabItem(tabFolder3, SWT.NONE);
		tbtmNewItem31.setText("Test 5");
		
		TabItem tbtmNewItem32 = new TabItem(tabFolder3, SWT.NONE);
		tbtmNewItem32.setText("Test 6");
		
		
		Composite southComposite = new Composite(shell, SWT.NONE);
		southComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		southComposite.setLayout(new GridLayout(3, false));
		
		Button okButton = new Button(southComposite, SWT.NONE);
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				list.setEnabled(false);
			}
		});
		okButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		okButton.setText("New Button");
		
		Button applyButton = new Button(southComposite, SWT.NONE);
		applyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				list.setEnabled(true);
			}
		});
		applyButton.setText("New Button");
		
		Button closeButton = new Button(southComposite, SWT.NONE);
		closeButton.setText("New Button");
		
		buildBarSettings();
		buildBubbleSettings();
		buildHeatSettings();
		
		combo.addSelectionListener(this);

	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		switch (combo.getSelectionIndex()) {
		case 0:
			sl_stackComposite.topControl = pageBarChart;
			stackComposite.layout();
			break;
		case 1:
			sl_stackComposite.topControl = pageBubbleChart;
			stackComposite.layout();
			break;
		case 2:
			sl_stackComposite.topControl = pageHeatChart;
			stackComposite.layout();
			break;
		}
		
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		sl_stackComposite.topControl = pageBarChart;
		
	}
	
	private void buildBarSettings() {
	
	}
	
	private void buildBubbleSettings() {
		
	}
	
	private void buildHeatSettings() {
	
	}
	
	private void buildTree(TreeViewer treeViewer) {	
		
		contentProvider = new TreeContentProvider(treeViewer);		
		treeViewer.setContentProvider(contentProvider);
		treeViewer.setLabelProvider(new DefaultEObjectLabelProvider());
		treeViewer.setSorter(null);
		treeViewer.setInput(m.get());
				
	}
}
