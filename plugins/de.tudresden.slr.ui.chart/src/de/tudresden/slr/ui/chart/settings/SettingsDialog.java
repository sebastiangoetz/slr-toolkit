package de.tudresden.slr.ui.chart.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IViewPart;

import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.ui.chart.logic.BarDataTerm;
import de.tudresden.slr.ui.chart.logic.BubbleDataContainer;
import de.tudresden.slr.ui.chart.logic.BubbleDataTerm;
import de.tudresden.slr.ui.chart.logic.ChartDataProvider;
import de.tudresden.slr.ui.chart.logic.ChartGenerator;
import de.tudresden.slr.ui.chart.settings.pages.AxisPageBar;
import de.tudresden.slr.ui.chart.settings.pages.AxisPageBubble;
import de.tudresden.slr.ui.chart.settings.pages.GeneralPageBar;
import de.tudresden.slr.ui.chart.settings.pages.GerneralPageBubble;
import de.tudresden.slr.ui.chart.settings.pages.LegendPage;
import de.tudresden.slr.ui.chart.settings.pages.SeriesPageBar;
import de.tudresden.slr.ui.chart.settings.pages.SeriesPageBubble;
import de.tudresden.slr.ui.chart.settings.pages.SeriesPageHeat;
import de.tudresden.slr.ui.chart.views.ICommunicationView;

public class SettingsDialog extends Dialog implements SelectionListener{

	private Object result;
	private Shell shell;
	
	private Combo comboChartSelect;
	private StackLayout sl_stackComposite;
	private Composite pageBarChart, pageBubbleChart, stackComposite, pageHeatChart; 
;
	private Optional<Model> m;
	private Button okButton, applyButton, closeButton;
	
	private GeneralPageBar generalPageBar;
	private SeriesPageHeat  seriesPageHeat;
	private LegendPage legendPage;
	private SeriesPageBar seriesPageBar;
	private AxisPageBar axisPageBar;
	
	private GerneralPageBubble gerneralPageBubble;
	private SeriesPageBubble seriesPageBubble;
	private AxisPageBubble axisPageBubble;
	
	private ICommunicationView view;
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
		TabItem itemFolderBarChart_4 = new TabItem(folderBarChart, SWT.NONE);
		
		
		generalPageBar = new GeneralPageBar(folderBarChart, SWT.NONE);
		itemFolderBarChart_1.setControl(generalPageBar);
		itemFolderBarChart_1.setText("General");
		
		legendPage = new LegendPage(folderBarChart, SWT.NONE);
		itemFolderBarChart_2.setControl(legendPage);
		itemFolderBarChart_2.setText("Legend");
		
		seriesPageBar = new SeriesPageBar(folderBarChart, SWT.NONE);
		itemFolderBarChart_3.setControl(seriesPageBar);
		itemFolderBarChart_3.setText("Series");
		
		axisPageBar = new AxisPageBar(folderBarChart, SWT.NONE);
		itemFolderBarChart_4.setControl(axisPageBar);
		itemFolderBarChart_4.setText("Axis");
	}
	
	private void buildBubbleSettings() {
		pageBubbleChart.setLayout(new FillLayout());
		TabFolder folderBubbleChart = new TabFolder(pageBubbleChart, SWT.NONE);
		
		TabItem itemFolderBubbleChart_1 = new TabItem(folderBubbleChart, SWT.NONE);
		TabItem itemFolderBubbleChart_2 = new TabItem(folderBubbleChart, SWT.NONE);
		TabItem itemFolderBubbleChart_3 = new TabItem(folderBubbleChart, SWT.NONE);
		
		gerneralPageBubble = new GerneralPageBubble(folderBubbleChart, SWT.NONE);
		seriesPageBubble = new SeriesPageBubble(folderBubbleChart, SWT.NONE);
		axisPageBubble = new AxisPageBubble(folderBubbleChart, SWT.NONE);
		
		itemFolderBubbleChart_1.setControl(gerneralPageBubble);
		itemFolderBubbleChart_2.setControl(seriesPageBubble);
		itemFolderBubbleChart_3.setControl(axisPageBubble);
		
		itemFolderBubbleChart_1.setText("General");
		itemFolderBubbleChart_2.setText("Series");
		itemFolderBubbleChart_3.setText("Axis");
	}
	
	private void buildHeatSettings() {
		pageHeatChart.setLayout(new FillLayout());
		TabFolder folderHeatChart = new TabFolder(pageHeatChart, SWT.NONE);
		
		TabItem tbtmNewItem31 = new TabItem(folderHeatChart, SWT.NONE);
		tbtmNewItem31.setText("Test 5");
		seriesPageHeat = new SeriesPageHeat(folderHeatChart, SWT.NONE);
		tbtmNewItem31.setControl(seriesPageHeat);
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
		
		switch (comboChartSelect.getSelectionIndex()) {
		case 0:{
			generalPageBar.saveSettings();
			legendPage.saveSettings();
			seriesPageBar.saveSettings();
			axisPageBar.saveSettings();
			

			List<BarDataTerm> data = ChartConfiguration.BARCHARTCONFIG.getBarTermList();
			SortedMap<String, Integer> citeChartData = new TreeMap<>();
			if(data.isEmpty()) {
				MessageDialog.openError(shell, "No Items Selected", "No Items Selected, please select items at the Series-Page ");
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
			break;
		}
		case 1:{
			
			gerneralPageBubble.saveSettings();
			seriesPageBubble.saveSettings();
			axisPageBubble.saveSettings();
			
			ChartConfiguration bubbleSettings = ChartConfiguration.BUBBLECHARTCONFIG;
			
			List<BubbleDataTerm> xdata = bubbleSettings.getBubbleTermListX();
			List<BubbleDataTerm> ydata = bubbleSettings.getBubbleTermListY();
			if(xdata.isEmpty() || ydata.isEmpty()) {
				MessageDialog.openError(shell, "No Items Selected", "No Items Selected, please select items at the Series-Page ");
				return;
			}
			
			ChartDataProvider chartDataProvider = new ChartDataProvider();
			List<BubbleDataContainer> bubbleChartData = chartDataProvider.calculateBubbleChartData(bubbleSettings.getSelectedTermX(), 
					bubbleSettings.getSelectedTermY());	
			List<BubbleDataContainer> removeList = new ArrayList<>();
			for(BubbleDataTerm term: xdata) {
				if(!term.isDisplayed()) {
					for(BubbleDataContainer item : bubbleChartData) {
						if(item.getxTerm().getName().equals(term.getTerm().getName())) {
							removeList.add(item);
						}
					}	
				}
			}
			
			for(BubbleDataTerm term: ydata) {
				if(!term.isDisplayed()) {
					for(BubbleDataContainer item : bubbleChartData) {
						if(item.getyTerm().getName().equals(term.getTerm().getName())) {
							removeList.add(item);							
						}
					}
				}		
			}
			
			
			bubbleChartData.removeAll(removeList);
			
			if(!bubbleChartData.isEmpty()) {
				view = (ICommunicationView) part;
				Chart bubbleChart = ChartGenerator.createBubble(bubbleChartData, bubbleSettings.getSelectedTermX(), bubbleSettings.getSelectedTermY());
				view.getPreview().setDataPresent(true);
				view.setAndRenderChart(bubbleChart);	
			}
			break;
			
		}
		case 2:{
			
			view = (ICommunicationView) part;
			Chart radarChart = ChartGenerator.createPie();
			view.getPreview().setDataPresent(true);
			view.setAndRenderChart(radarChart);
		}
		break;
		}
		
		
		
	}
	
	public void setViewPart(IViewPart part) {
		this.part = part;
	}
		
}
	
	
	
	
	
	
	
	
