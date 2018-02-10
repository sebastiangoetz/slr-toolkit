package de.tudresden.slr.ui.chart.settings.pages;

import org.eclipse.emf.common.util.EList;
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
import de.tudresden.slr.ui.chart.logic.BubbleDataTerm;
import de.tudresden.slr.ui.chart.logic.ChartDataProvider;
import de.tudresden.slr.ui.chart.settings.BubbleChartConfiguration;
import de.tudresden.slr.ui.chart.settings.TreeDialogBubble;

public class SeriesPageBubble extends Composite implements SelectionListener, MouseListener, Pages{

	// UI
	private Button btnRadioButtonGrey, btnRadioButtonCustom, btnRadioButtonRandom, btnGetXTerm, btnGetYTerm, btnShowInChartY;
	private List list_y, list_x;	
	private Label labelShowColor, lblxNewLabel, lblySelectedTermIs;
	private Composite compositeFirst;
	
	// Data
	public Term selectedTerm_X;
	public Term selectedTerm_Y;
	private ChartDataProvider chartDataProvider = new ChartDataProvider();	
	private BubbleChartConfiguration settings = BubbleChartConfiguration.get();
	private java.util.List<BubbleDataTerm> termDataY;
	private java.util.List<BubbleDataTerm> termDataX;
	private Button btnShowInChartX;
	private Button btnOneColor;
	private Label lblColorYSeries;
	
	
	public SeriesPageBubble(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		compositeFirst = new Composite(this, SWT.NONE);
		compositeFirst.setLayout(new GridLayout(4, true));
		compositeFirst.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnGetYTerm = new Button(compositeFirst, SWT.NONE);
		btnGetYTerm.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnGetYTerm.setText("Select Y Term");
		btnGetYTerm.addSelectionListener(this);
		
		lblySelectedTermIs = new Label(compositeFirst, SWT.NONE);
		lblySelectedTermIs.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblySelectedTermIs.setText("No Term Selected");
		
		btnGetXTerm = new Button(compositeFirst, SWT.NONE);
		btnGetXTerm.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnGetXTerm.setText("Select X Term");
		btnGetXTerm.addSelectionListener(this);
		
		lblxNewLabel = new Label(compositeFirst, SWT.NONE);
		lblxNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblxNewLabel.setText("No Term Selected");
		
		
		Composite compositeCentre = new Composite(this, SWT.NONE);
		compositeCentre.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_compositeCentre = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_compositeCentre.widthHint = 218;
		compositeCentre.setLayoutData(gd_compositeCentre);
		
		list_y = new List(compositeCentre, SWT.BORDER | SWT.V_SCROLL);
		list_y.setBounds(0, 0, 71, 68);
		list_y.addSelectionListener(this);
		
		list_x = new List(compositeCentre, SWT.BORDER | SWT.V_SCROLL);
		list_x.addSelectionListener(this);
		
		Composite compositeNorth = new Composite(this, SWT.NONE);
		compositeNorth.setLayout(new GridLayout(2, false));
		compositeNorth.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnShowInChartY = new Button(compositeNorth, SWT.CHECK);
		btnShowInChartY.setBounds(0, 0, 111, 20);
		btnShowInChartY.setText("Show in Chart");
		btnShowInChartY.addSelectionListener(this);
		
		btnShowInChartX = new Button(compositeNorth, SWT.CHECK);
		btnShowInChartX.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		btnShowInChartX.setText("Show in Chart");
		btnShowInChartX.addSelectionListener(this);
		
		Composite compositeSouth = new Composite(this, SWT.NONE);
		compositeSouth.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		compositeSouth.setLayout(new GridLayout(6, false));
		
		lblColorYSeries = new Label(compositeSouth, SWT.NONE);
		GridData gd_lblColorYSeries = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblColorYSeries.widthHint = 100;
		lblColorYSeries.setLayoutData(gd_lblColorYSeries);
		lblColorYSeries.setText("Color Y Series:");
		
		labelShowColor = new Label(compositeSouth, SWT.BORDER);
		GridData gd_labelShowColor = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelShowColor.widthHint = 100;
		labelShowColor.setLayoutData(gd_labelShowColor);
		labelShowColor.setBounds(0, 0, 70, 20);
		labelShowColor.setText(" ");
		
		btnRadioButtonGrey = new Button(compositeSouth, SWT.RADIO);
		btnRadioButtonGrey.setText("Grey");
		
		btnRadioButtonCustom = new Button(compositeSouth, SWT.RADIO);
		btnRadioButtonCustom.setText("Custom");
		
		btnRadioButtonRandom = new Button(compositeSouth, SWT.RADIO);
		btnRadioButtonRandom.setSelection(true);
		btnRadioButtonRandom.setText("Random");
		
		btnOneColor = new Button(compositeSouth, SWT.RADIO);
		btnOneColor.setText("One color");
		btnOneColor.addSelectionListener(this);
		btnRadioButtonRandom.addSelectionListener(this);
		btnRadioButtonCustom.addSelectionListener(this);
		btnRadioButtonGrey.addSelectionListener(this);
		labelShowColor.addMouseListener(this);
		
		loadSettings();

	}

	@Override
	protected void checkSubclass() {}

	@Override
	public void widgetSelected(SelectionEvent e) {
		
		if(e.getSource() == btnGetXTerm) {
			TreeDialogBubble treeDialogBubble = new TreeDialogBubble(this.getShell(), SWT.NONE);
			selectedTerm_X = (Term) treeDialogBubble.open();		
			
			if(selectedTerm_X != null) {
				lblxNewLabel.setText("'" + selectedTerm_X.getName()+"'");
				termDataX.clear();
				list_x.removeAll();
				btnRadioButtonRandom.setEnabled(true);
				buildListPerSubclass(selectedTerm_X, list_x, termDataX);
				list_x.setSelection(0);
				refresh(list_x);
			}	
						
		}
		
		if(e.getSource() == btnGetYTerm) {
			TreeDialogBubble treeDialogBubble = new TreeDialogBubble(this.getShell(), SWT.NONE);
			selectedTerm_Y = (Term) treeDialogBubble.open();		
			
			if(selectedTerm_Y != null) {				
				lblySelectedTermIs.setText("'" + selectedTerm_Y.getName()+"'");
				termDataY.clear();
				list_y.removeAll();
				btnRadioButtonRandom.setEnabled(true);
				buildListPerSubclass(selectedTerm_Y,list_y,termDataY);
				list_y.setSelection(0);
				refresh(list_y);
			}	
						
		}
		
		if(e.getSource() == list_y) {			
			refresh(list_y);			
		}
		if(e.getSource() == list_x) {			
			refresh(list_x);			
		}
		
		if(e.getSource() == btnShowInChartY) {
			int selectedItem = list_y.getSelectionIndex();
			termDataY.get(selectedItem).setDisplayed(btnShowInChartY.getSelection());
		}
		if(e.getSource() == btnShowInChartX) {
			int selectedItem = list_x.getSelectionIndex();
			termDataX.get(selectedItem).setDisplayed(btnShowInChartX.getSelection());
		}
			
		if(e.getSource() == btnRadioButtonGrey && list_y.getItemCount() > 0) {
			
			int step = 255/termDataY.size();
			
			int rgbValue = 0;
			for(BubbleDataTerm bubbleDataTerm: termDataY) {
				rgbValue = rgbValue + step;
				bubbleDataTerm.setRGB(new RGB(rgbValue, rgbValue, rgbValue));				
			}
			refresh(list_y);
		}
		
		if(e.getSource() == btnRadioButtonRandom && list_y.getItemCount() > 0) {
			
			for(BubbleDataTerm bubbleDataTerm: termDataY) {
				bubbleDataTerm.setRGBRandom();			
			}
			refresh(list_y);
		}
		
		if(e.getSource() == btnOneColor && list_y.getItemCount() > 0 && btnOneColor.getSelection()) {
			RGB rgb = PageSupport.openAndGetColor(this.getParent(), labelShowColor);
			
			for(BubbleDataTerm bubbleDataTerm: termDataY) {
				bubbleDataTerm.setRGB(rgb);				
			}
			
			refresh(list_y);
		}
		
	}
	
	private void buildListPerSubclass(Term t, List l, java.util.List<BubbleDataTerm> templist) {		
		EList<Term> subterms = t.getSubclasses();
		for(Term subterm : subterms) {
			templist.add(new BubbleDataTerm(subterm));
			l.add(subterm.getName());
		}		
	}

	private void refresh(List l) {
		int index = l.getSelectionIndex();
		if(l == list_x) {
			btnShowInChartX.setSelection(termDataX.get(index).isDisplayed());
		}
		else {
			labelShowColor.setBackground(termDataY.get(index).getColor(this.getDisplay()));
			btnShowInChartY.setSelection(termDataY.get(index).isDisplayed());
		}		
		this.layout();
	}
	
	@Override
	public void mouseUp(MouseEvent e) {
		if(e.getSource() == labelShowColor && list_y.getItemCount() > 0 && btnRadioButtonCustom.getSelection()) {
			RGB rgb = PageSupport.openAndGetColor(this.getParent(), labelShowColor);
			int index = list_y.getSelectionIndex();
			termDataY.get(index).setRGB(rgb);
		}
	}
	@Override
	public void saveSettings() {
		
		settings.setBubbleTermListX(termDataX);
		settings.setBubbleTermListY(termDataY);
		settings.setSelectedTermX(selectedTerm_X);
		settings.setSelectedTermY(selectedTerm_Y);	
			
	}
	@Override
	public void loadSettings() {
		termDataX  = settings.getBubbleTermListX();
		termDataY  = settings.getBubbleTermListY();
		
		selectedTerm_X = settings.getSelectedTermX();
		selectedTerm_Y = settings.getSelectedTermY();
		
		if(!termDataX.isEmpty()) {			
			for(BubbleDataTerm entry :termDataX) {
				list_x.add(entry.getTerm().getName());
			}
		}
		
		if(!termDataY.isEmpty()) {			
			for(BubbleDataTerm entry :termDataY) {
				list_y.add(entry.getTerm().getName());
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
