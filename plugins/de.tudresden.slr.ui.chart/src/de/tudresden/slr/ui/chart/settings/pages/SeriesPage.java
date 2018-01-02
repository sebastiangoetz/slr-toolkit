package de.tudresden.slr.ui.chart.settings.pages;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.List;

import java.util.*;

import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.ui.chart.logic.ChartDataProvider;
import de.tudresden.slr.ui.chart.settings.ChartConfiguration;
import de.tudresden.slr.ui.chart.settings.TreeDialog;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class SeriesPage extends Composite implements SelectionListener, MouseListener{

	private Button btnRadioButtonGrey, btnRadioButtonCustom, btnRadioButtonRandom, btnNewButton;
	private Button btnCheckButton;
	private List list;
	private java.util.List<RGB> colorList = new ArrayList<>();
	public SortedMap<String, Boolean> visibleMap = new TreeMap<String, Boolean>();
	public Term selectedTerm;
	public static boolean perSubTerm = true;
	
	private Random random = new Random();
	
	
	private Label labelShowColor;
	private Composite compositeFirst;
	private Label lblSelectedTermIs;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SeriesPage(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		compositeFirst = new Composite(this, SWT.NONE);
		compositeFirst.setLayout(new GridLayout(2, false));
		compositeFirst.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		btnNewButton = new Button(compositeFirst, SWT.NONE);
		btnNewButton.setText("Get Term");
		
		lblSelectedTermIs = new Label(compositeFirst, SWT.NONE);
		GridData gd_lblSelectedTermIs = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblSelectedTermIs.widthHint = 367;
		lblSelectedTermIs.setLayoutData(gd_lblSelectedTermIs);
		lblSelectedTermIs.setText("No Term Selected");
		btnNewButton.addSelectionListener(this);
		
		
		
		Composite compositeCentre = new Composite(this, SWT.NONE);
		compositeCentre.setLayout(new GridLayout(1, false));
		compositeCentre.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, true, 1, 1));
		
		list = new List(compositeCentre, SWT.BORDER | SWT.V_SCROLL);
		GridData gd_list = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1);
		gd_list.widthHint = 400;
		list.setLayoutData(gd_list);
		list.setBounds(0, 0, 71, 68);
		list.addSelectionListener(this);
		
		Composite compositeNorth = new Composite(this, SWT.NONE);
		compositeNorth.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		FillLayout fl_compositeNorth = new FillLayout(SWT.HORIZONTAL);
		fl_compositeNorth.spacing = 5;
		compositeNorth.setLayout(fl_compositeNorth);
		
		btnRadioButtonGrey = new Button(compositeNorth, SWT.RADIO);
		btnRadioButtonGrey.setText("Grey");
		btnRadioButtonGrey.addSelectionListener(this);
		
		btnRadioButtonCustom = new Button(compositeNorth, SWT.RADIO);
		btnRadioButtonCustom.setText("Custom");
		btnRadioButtonCustom.addSelectionListener(this);
		
		btnRadioButtonRandom = new Button(compositeNorth, SWT.RADIO);
		btnRadioButtonRandom.setSelection(true);
		btnRadioButtonRandom.setText("Random");
		btnRadioButtonRandom.addSelectionListener(this);
		
		Composite compositeSouth = new Composite(this, SWT.NONE);
		compositeSouth.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		compositeSouth.setLayout(new GridLayout(5, false));
		
		btnCheckButton = new Button(compositeSouth, SWT.CHECK);
		btnCheckButton.setBounds(0, 0, 111, 20);
		btnCheckButton.setText("Show in Chart");
		btnCheckButton.addSelectionListener(this);
		
		labelShowColor = new Label(compositeSouth, SWT.BORDER);
		GridData gd_labelShowColor = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelShowColor.widthHint = 100;
		labelShowColor.setLayoutData(gd_labelShowColor);
		labelShowColor.setBounds(0, 0, 70, 20);
		labelShowColor.setText(" ");
		labelShowColor.addMouseListener(this);
		new Label(compositeSouth, SWT.NONE);
		new Label(compositeSouth, SWT.NONE);
		new Label(compositeSouth, SWT.NONE);

	}

	@Override
	protected void checkSubclass() {}

	@Override
	public void widgetSelected(SelectionEvent e) {
		
		if(e.getSource() == btnNewButton) {
			TreeDialog treeDialog = new TreeDialog(this.getShell(), SWT.NONE);
			selectedTerm = (Term) treeDialog.open();
			
			lblSelectedTermIs.setText("Selected Term is: '" + selectedTerm.getName()+"'");
			list.removeAll();
			colorList.clear();
			visibleMap.clear();			
			btnRadioButtonRandom.setEnabled(true);
			ChartDataProvider chartDataProvider = new ChartDataProvider();
			
			if(selectedTerm != null && perSubTerm) {
				SortedMap<String, Integer> sortedMap = chartDataProvider.calculateNumberOfPapersPerClass(selectedTerm);	
				for(Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
					list.add(entry.getKey());
					colorList.add(new RGB(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					visibleMap.put(entry.getKey(), true);
				}
			}
			else {
				
				SortedMap<String, Integer> sortedMap = chartDataProvider.calculateNumberOfCitesPerYearForClass(selectedTerm);				
				for(Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
					list.add(entry.getKey());
					colorList.add(new RGB(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					visibleMap.put(entry.getKey(), true);
				}
			}
			
			list.setSelection(0);
			refresh();
		}
		
		if(e.getSource() == list) {			
			refresh();			
		}
		
		if(e.getSource() == btnCheckButton) {
			visibleMap.put(list.getItem(list.getSelectionIndex()),btnCheckButton.getSelection());
		}
			
		if(e.getSource() == btnRadioButtonGrey && list.getItemCount() > 0) {
			
			int step = 255/colorList.size();
			int rgbValue = 0;
			for(int i = 0; i < colorList.size(); i++) {
				rgbValue = rgbValue + step;
				colorList.set(i, new RGB(rgbValue ,rgbValue, rgbValue));
			}
			refresh();
		}
		
		if(e.getSource() == btnRadioButtonRandom && list.getItemCount() > 0) {
			for(int i = 0; i < colorList.size(); i++) {
				colorList.set(i, new RGB(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			}
			refresh();
		}
		
	}
	
	private void refresh() {
		int index = list.getSelectionIndex();
		labelShowColor.setBackground(new Color(this.getShell().getDisplay(), colorList.get(index)));
		btnCheckButton.setSelection(visibleMap.get(list.getItem(index)));
		this.layout();
	}
	
	@Override
	public void mouseUp(MouseEvent e) {
		if(e.getSource() == labelShowColor && list.getItemCount() > 0 && btnRadioButtonCustom.getSelection()) {
			RGB rgb = PageSupport.openAndGetColor(this.getParent(), labelShowColor);
			colorList.set(list.getSelectionIndex(), rgb);
		}
	}
	
	public void saveSettings() {
		
		ArrayList<Fill> fillList = new ArrayList<>();
		int i = 0;
		for(Map.Entry<String, Boolean> entry : visibleMap.entrySet()) {
			if(entry.getValue()) {
				RGB u = colorList.get(i);
				fillList.add(ColorDefinitionImpl.create(u.red, u.green, u.blue));
			}
			i = i+1;	
		}
		
		ChartConfiguration.getSeriesSettings().setSeriesUseCustomColors(true);
		ChartConfiguration.getSeriesSettings().setSeriesColor(fillList);
		
		ChartConfiguration.getDataSettings().setColorList(colorList);
		ChartConfiguration.getDataSettings().setSelectedTerm(selectedTerm);
		ChartConfiguration.getDataSettings().setVisibleMap(visibleMap);
		ChartConfiguration.getDataSettings().setPerSubTerm(perSubTerm);
	}
	
	public void loadSettings() {
		colorList = ChartConfiguration.getDataSettings().getColorList();
		selectedTerm = ChartConfiguration.getDataSettings().getSelectedTerm();
		visibleMap = ChartConfiguration.getDataSettings().getVisibleMap();
		perSubTerm = ChartConfiguration.getDataSettings().isPerSubTerm();
	}
	
	private void displaySettings() {
		
	}
	
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {}

	@Override
	public void mouseDoubleClick(MouseEvent e) {}

	@Override
	public void mouseDown(MouseEvent e) {}

	
}
