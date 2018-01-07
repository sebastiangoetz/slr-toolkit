package de.tudresden.slr.ui.chart.settings.pages;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;

import de.tudresden.slr.ui.chart.settings.ChartConfiguration;

import org.eclipse.swt.widgets.Scale;

public class LegendPage extends Composite implements SelectionListener, MouseListener, Pages{
	private Text text;
	private Combo comboOutline, comboPosition;
	private Label labelColorShow, lblMaxPercent;
	private Scale scale;
	
	ChartConfiguration settings = ChartConfiguration.get();
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LegendPage(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		Label lblSetTitle = new Label(this, SWT.NONE);
		GridData gd_lblSetTitle = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblSetTitle.widthHint = 150;
		lblSetTitle.setLayoutData(gd_lblSetTitle);
		lblSetTitle.setText("Set Title");
		
		text = new Text(this, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblBackgroundColor = new Label(this, SWT.NONE);
		lblBackgroundColor.setText("Background Color");
		
		labelColorShow = new Label(this, SWT.BORDER);
		GridData gd_labelColorShow = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelColorShow.widthHint = 100;
		labelColorShow.setLayoutData(gd_labelColorShow);
		labelColorShow.setText(" ");
		labelColorShow.setBackground(PageSupport.getColor(parent, 0));
		labelColorShow.addMouseListener(this);
		
		Label lblOutline = new Label(this, SWT.NONE);
		lblOutline.setText("Outline Style");
		
		comboOutline = new Combo(this, SWT.READ_ONLY);
		comboOutline.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		comboOutline.add("None");
		comboOutline.add("Dashed");
		comboOutline.add("Dash Dotted");
		comboOutline.add("Dotted");
		comboOutline.add("Solid");
		comboOutline.select(0);
		
		lblMaxPercent = new Label(this, SWT.NONE);
		lblMaxPercent.setText("Max. Percent");
		
		scale = new Scale(this, SWT.NONE);
		scale.setIncrement(2);
		GridData gd_scale = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_scale.widthHint = 309;
		scale.setLayoutData(gd_scale);
		scale.setPageIncrement(2);
		scale.setMaximum(50);
		scale.setSelection(10);
		scale.addSelectionListener(this);
		
		Label lblPosition = new Label(this, SWT.NONE);
		lblPosition.setText("Position");
		
		comboPosition = new Combo(this, SWT.READ_ONLY);
		comboPosition.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		comboPosition.add("Right");
		comboPosition.add("Left");
		comboPosition.add("Below");
		comboPosition.add("Top");
		comboPosition.select(0);		

		loadSettings();
	}

	
	private String getTitle() {return text.getText();}
	private void setTitle(String title) {text.setText(title);}
	
	private RGB getColor() {return labelColorShow.getBackground().getRGB();}
	private void setColor(RGB rgb) {labelColorShow.setBackground(new Color(this.getDisplay(),rgb));}
	
	private Position getPosition() {return PageSupport.getPosition(comboPosition.getSelectionIndex());}
	private void setPosition(Position position) {comboPosition.select(PageSupport.setPosition(position));}
	
	private LineStyle getOutline() {return PageSupport.getLineStyle(comboOutline.getSelectionIndex());}
	private void setOutline(LineStyle lineStyle) {comboOutline.select(PageSupport.setLineStyle(lineStyle));};
	
	private double getPercent() {return (double) scale.getSelection()/100;}
	private void setPercent(double value) {scale.setSelection((int)(value*100));}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		if(e.getSource() == scale) {
			
			lblMaxPercent.setText("Max. Percent:  " + scale.getSelection()+ "%");			
			lblMaxPercent.getParent().layout(); 
		}
	}

	@Override
	public void mouseUp(MouseEvent e) {
		
		if(e.getSource() == labelColorShow) {
			RGB rgb = PageSupport.openAndGetColor(this.getParent(), labelColorShow);
		}
		
	}
	
	@Override
	public void saveSettings() {
		if(getOutline() == null)
			settings.getLegendSettings().setLegendShowOutline(false);
		else {
			settings.getLegendSettings().setLegendShowOutline(true);
			settings.getLegendSettings().setLegendOutlineStyle(getOutline());
		}
		
		settings.getLegendSettings().setLegendShadowRGB(getColor());
		settings.getLegendSettings().setLegendBackgroundRGB(getColor());		
		settings.getLegendSettings().setLegendPosition(getPosition());		
		settings.getLegendSettings().setLegendMaxPercent(getPercent());		
		settings.getLegendSettings().setLegendTitle(getTitle());				
	}
	
	@Override
	public void loadSettings() {
		setTitle(settings.getLegendSettings().getLegendTitle());
		setColor(settings.getLegendSettings().getLegendBackgroundRGB());
		setPosition(settings.getLegendSettings().getLegendPosition());
		setPercent(settings.getLegendSettings().getLegendMaxPercent());
		if(settings.getLegendSettings().isLegendShowOutline())
			setOutline(settings.getLegendSettings().getLegendOutlineStyle());
		else
			setOutline(null);
				
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {}
	@Override
	public void mouseDoubleClick(MouseEvent e) {}
	@Override
	public void mouseDown(MouseEvent e) {}	
	@Override
	protected void checkSubclass() {}

}
