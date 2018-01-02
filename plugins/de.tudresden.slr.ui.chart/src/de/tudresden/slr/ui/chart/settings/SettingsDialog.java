package de.tudresden.slr.ui.chart.settings;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.RGB;
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
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.ui.label.DefaultEObjectLabelProvider;

import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.ui.chart.logic.BarChartGenerator;
import de.tudresden.slr.ui.chart.logic.BarDataTerm;
import de.tudresden.slr.ui.chart.logic.ChartDataProvider;
import de.tudresden.slr.ui.chart.logic.ChartGenerator;
import de.tudresden.slr.ui.chart.settings.pages.GeneralPage;
import de.tudresden.slr.ui.chart.settings.pages.LegendPage;
import de.tudresden.slr.ui.chart.settings.pages.SeriesPage;
import de.tudresden.slr.ui.chart.views.ICommunicationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.SelectionAdapter;

public class SettingsDialog extends Dialog implements SelectionListener{

	private Object result;
	private Shell shell;
	
	private Combo comboChartSelect;
	private StackLayout sl_stackComposite;
	private Composite pageBarChart, pageBubbleChart, stackComposite, pageHeatChart; 
;
	private Optional<Model> m;
	private Button okButton, applyButton, closeButton;
	
	private GeneralPage generalPage;
	private LegendPage legendPage;
	private SeriesPage seriesPage;
	
	private ICommunicationView view;
	
	private BarFolder barFolder;
	private IViewPart part;

	
	public SettingsDialog(Shell parent, int style) {
		super(parent, style);
		setText("Chart Settings");
	}
	
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

	private void createContents() {
		createShell();
		
		createNorth();
		
		createCenter();
		
		createSouth();
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if(e.getSource() == closeButton) {
			shell.close();
			return;
		}
		
		if(e.getSource() == applyButton) {			
			collectAndSaveSettings();			
			return;
		}
		
		if(e.getSource() == okButton) {
			collectAndSaveSettings();
			shell.close();
			return;
		}
		
		if(e.getSource() == comboChartSelect) {
			switch (comboChartSelect.getSelectionIndex()) {
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
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		sl_stackComposite.topControl = pageBarChart;
		
	}
	
	private void buildBarSettings() {
		
		TabFolder folderBarChart = new TabFolder(pageBarChart, SWT.NONE);
		
		TabItem itemFolderBarChart_1 = new TabItem(folderBarChart, SWT.NONE);
		TabItem itemFolderBarChart_2 = new TabItem(folderBarChart, SWT.NONE);
		TabItem itemFolderBarChart_3 = new TabItem(folderBarChart, SWT.NONE);
		
		generalPage = new GeneralPage(folderBarChart, SWT.NONE);
		itemFolderBarChart_1.setControl(generalPage);
		itemFolderBarChart_1.setText("General");
		
		legendPage = new LegendPage(folderBarChart, SWT.NONE);
		itemFolderBarChart_2.setControl(legendPage);
		itemFolderBarChart_2.setText("Legend");
		
		seriesPage = new SeriesPage(folderBarChart, SWT.NONE);
		itemFolderBarChart_3.setControl(seriesPage);
		itemFolderBarChart_3.setText("Series");
		
	}
	
	private void buildBubbleSettings() {
		pageBubbleChart.setLayout(new FillLayout());
		TabFolder folderBubbleChart = new TabFolder(pageBubbleChart, SWT.NONE);
		
		TabItem tbtmNewItem21 = new TabItem(folderBubbleChart, SWT.NONE);
		tbtmNewItem21.setText("Test 3");
		
		TabItem tbtmNewItem22 = new TabItem(folderBubbleChart, SWT.NONE);
		tbtmNewItem22.setText("Test 4");
	}
	
	private void buildHeatSettings() {
		pageHeatChart.setLayout(new FillLayout());
		TabFolder folderHeatChart = new TabFolder(pageHeatChart, SWT.NONE);
		
		TabItem tbtmNewItem31 = new TabItem(folderHeatChart, SWT.NONE);
		tbtmNewItem31.setText("Test 5");
		
		TabItem tbtmNewItem32 = new TabItem(folderHeatChart, SWT.NONE);
		tbtmNewItem32.setText("Test 6");
		
	}
	
	private void createShell() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE);
		shell.setSize(750, 600);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));
	}
	
	private void createNorth() {
		
		Composite northComposite = new Composite(shell, SWT.NONE);
		northComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		northComposite.setLayout(new GridLayout(2, false));
		
		Label comboLabel = new Label(northComposite, SWT.NONE);
		comboLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		comboLabel.setText("Please select your chart type: ");
		
		comboChartSelect = new Combo(northComposite, SWT.NONE | SWT.READ_ONLY);
		comboChartSelect.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboChartSelect.add("Bar");
		comboChartSelect.add("Bubble");
		comboChartSelect.add("Heat");
		comboChartSelect.select(-1);

		comboChartSelect.addSelectionListener(this);
		
	}
	
	private void createCenter() {
		
		stackComposite = new Composite(shell, SWT.NONE);
		stackComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sl_stackComposite = new StackLayout();
		stackComposite.setLayout(sl_stackComposite);
		
		pageBarChart = new Composite (stackComposite, SWT.NONE);
		pageBubbleChart = new Composite (stackComposite, SWT.NONE);
		pageHeatChart = new Composite (stackComposite, SWT.NONE);
		
		pageBarChart.setLayout(new FillLayout());
		
		buildBarSettings();
		buildBubbleSettings();
		buildHeatSettings();
		
	}
	
	private void createSouth() {
		Composite southComposite = new Composite(shell, SWT.NONE);
		southComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		southComposite.setLayout(new GridLayout(3, false));
		
		okButton = new Button(southComposite, SWT.NONE);
		
		okButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		okButton.setText("        Save and Close        ");
		okButton.addSelectionListener(this);
		
		applyButton = new Button(southComposite, SWT.NONE);
		applyButton.addSelectionListener(this);
		applyButton.setText("    Apply    ");
		
		closeButton = new Button(southComposite, SWT.NONE);
		closeButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		closeButton.setText("    Close    ");
		closeButton.addSelectionListener(this);
	}
	
	private void collectAndSaveSettings() {		

		generalPage.saveSettings();
		legendPage.saveSettings();
		seriesPage.saveSettings();
		
		List<BarDataTerm> data = ChartConfiguration.get().getBarTermList();
		SortedMap<String, Integer> citeChartData = new TreeMap<>();
		if(data.isEmpty()) {
			return;
			}
		for(BarDataTerm term: data) {
			if(term.isDisplayed())
				citeChartData.put(term.getTerm(), term.getSize());
		}
		if(!citeChartData.isEmpty()) {
			view = (ICommunicationView) part;
			Chart citeChart = ChartGenerator.createCiteBar(citeChartData);
			view.getPreview().setDataPresent(true);
			view.setAndRenderChart(citeChart);	
		}
		
	}
	
	public void setViewPart(IViewPart part) {
		this.part = part;
	}
		
}
	
	
	
	
	
	
	
	
