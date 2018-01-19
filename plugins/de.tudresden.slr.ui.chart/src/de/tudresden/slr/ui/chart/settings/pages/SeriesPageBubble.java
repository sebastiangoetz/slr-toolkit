package de.tudresden.slr.ui.chart.settings.pages;

import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.ui.chart.logic.BarDataTerm;
import de.tudresden.slr.ui.chart.logic.ChartDataProvider;
import de.tudresden.slr.ui.chart.settings.ChartConfiguration;
import de.tudresden.slr.ui.chart.settings.TreeDialogBubble;

public class SeriesPageBubble extends Composite implements SelectionListener, MouseListener, Pages{

	// UI
	private Button btnRadioButtonGrey, btnRadioButtonCustom, btnRadioButtonRandom, btnNewButton_1, btnNewButton, btnCheckButton;
	private List list_x, list_y;	
	private Label labelShowColor, lblNewLabel, lblSelectedTermIs;
	private Composite compositeFirst;
	
	// Data
	private java.util.List<BarDataTerm> barTermList= new ArrayList<>();
	public Term selectedTerm_X;
	public Term selectedTerm_Y;
	private ChartDataProvider chartDataProvider = new ChartDataProvider();	
	private ChartConfiguration settings = ChartConfiguration.BUBBLECHARTCONFIG;
	
	public SeriesPageBubble(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		compositeFirst = new Composite(this, SWT.NONE);
		compositeFirst.setLayout(new GridLayout(4, true));
		compositeFirst.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnNewButton = new Button(compositeFirst, SWT.NONE);
		btnNewButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnNewButton.setText("Get X Terms");
		btnNewButton.addSelectionListener(this);
		
		lblSelectedTermIs = new Label(compositeFirst, SWT.NONE);
		lblSelectedTermIs.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblSelectedTermIs.setText("No Term Selected");
		
		btnNewButton_1 = new Button(compositeFirst, SWT.NONE);
		btnNewButton_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnNewButton_1.setText("Get Y Terms");
		btnNewButton_1.addSelectionListener(this);
		
		lblNewLabel = new Label(compositeFirst, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblNewLabel.setText("No Term Selected");
		btnNewButton.addSelectionListener(this);
		
		
		
		Composite compositeCentre = new Composite(this, SWT.NONE);
		compositeCentre.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_compositeCentre = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_compositeCentre.widthHint = 218;
		compositeCentre.setLayoutData(gd_compositeCentre);
		
		list_x = new List(compositeCentre, SWT.BORDER | SWT.V_SCROLL);
		list_x.setBounds(0, 0, 71, 68);
		
		list_y = new List(compositeCentre, SWT.BORDER | SWT.V_SCROLL);
		list_x.addSelectionListener(this);
		
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
		
		loadSettings();

	}

	@Override
	protected void checkSubclass() {}

	@Override
	public void widgetSelected(SelectionEvent e) {
		
		if(e.getSource() == btnNewButton) {
			TreeDialogBubble treeDialogBubble = new TreeDialogBubble(this.getShell(), SWT.NONE);
			selectedTerm_X = (Term) treeDialogBubble.open();		
			
			if(selectedTerm_X != null) {
				lblSelectedTermIs.setText("'" + selectedTerm_X.getName()+"'");
				barTermList.clear();
				list_x.removeAll();
				btnRadioButtonRandom.setEnabled(true);
				buildListPerSubclass(selectedTerm_X, list_x);
				list_x.setSelection(0);
				refresh();
			}	
						
		}
		
		if(e.getSource() == btnNewButton_1) {
			TreeDialogBubble treeDialogBubble = new TreeDialogBubble(this.getShell(), SWT.NONE);
			selectedTerm_Y = (Term) treeDialogBubble.open();		
			
			if(selectedTerm_Y != null) {
				lblSelectedTermIs.setText("'" + selectedTerm_Y.getName()+"'");
				barTermList.clear();
				list_y.removeAll();
				btnRadioButtonRandom.setEnabled(true);
				buildListPerSubclass(selectedTerm_Y,list_y);
				list_y.setSelection(0);
				refresh();
			}	
						
		}
		
		if(e.getSource() == list_x) {			
			refresh();			
		}
		
		if(e.getSource() == btnCheckButton) {
			int selectedItem = list_x.getSelectionIndex();
			barTermList.get(selectedItem).setDisplayed(btnCheckButton.getSelection());
		}
			
		if(e.getSource() == btnRadioButtonGrey && list_x.getItemCount() > 0) {
			
			int step = 255/barTermList.size();
			
			int rgbValue = 0;
			for(BarDataTerm barDataTerm: barTermList) {
				rgbValue = rgbValue + step;
				barDataTerm.setRgb(new RGB(rgbValue, rgbValue, rgbValue));				
			}
			refresh();
		}
		
		if(e.getSource() == btnRadioButtonRandom && list_x.getItemCount() > 0) {
			
			for(BarDataTerm barDataTerm: barTermList) {
				barDataTerm.setRGBRandom();;				
			}
			refresh();
		}
		
	}
	
	private void buildListPerSubclass(Term t, List l) {
		SortedMap<String, Integer> sortedMap = chartDataProvider.calculateNumberOfPapersPerClass(t);	
		for(Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
			l.add(entry.getKey() + " (" +entry.getValue() + ")");
		}
		
	}

	private void refresh() {
		int index = list_x.getSelectionIndex();
		labelShowColor.setBackground(barTermList.get(index).getColor(this.getDisplay()));
		btnCheckButton.setSelection(barTermList.get(index).isDisplayed());
		this.layout();
	}
	
	@Override
	public void mouseUp(MouseEvent e) {
		if(e.getSource() == labelShowColor && list_x.getItemCount() > 0 && btnRadioButtonCustom.getSelection()) {
			RGB rgb = PageSupport.openAndGetColor(this.getParent(), labelShowColor);
			int index = list_x.getSelectionIndex();
			barTermList.get(index).setRgb(rgb);
		}
	}
	@Override
	public void saveSettings() {
		
		settings.setBarTermList(barTermList);
		//settings.setSelectedTerm(selectedTerm);
		//settings.setTermSort(termSort);
			
	}
	@Override
	public void loadSettings() {
		barTermList = settings.getBarTermList();
		//selectedTerm = settings.getSelectedTerm();
		//termSort = settings.getTermSort();
		
		if(!barTermList.isEmpty()) {			
			for(BarDataTerm entry :barTermList) {
				list_x.add(entry.getTerm()+ " (" +entry.getSize()+ ")");
			}
		}		
	}
	
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {}

	@Override
	public void mouseDoubleClick(MouseEvent e) {}

	@Override
	public void mouseDown(MouseEvent e) {}

	
}
