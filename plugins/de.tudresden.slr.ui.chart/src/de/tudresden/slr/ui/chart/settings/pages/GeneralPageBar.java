package de.tudresden.slr.ui.chart.settings.pages;

import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.tudresden.slr.ui.chart.settings.ChartConfiguration;
import de.tudresden.slr.ui.chart.settings.parts.BlockSettings;
import de.tudresden.slr.ui.chart.settings.parts.GeneralSettings;

public class GeneralPageBar extends Composite implements MouseListener, Pages{

	private Label labelShowColor, labelShowColor2;
	private Text text;
	private Combo comboTitleSize, comboBlockOutline;
	private Button btnUnderline, btnBolt, btnItalic, btnShowLables;
	
	private GeneralSettings settingsGeneral = ChartConfiguration.BARCHARTCONFIG.getGeneralSettings();
	private BlockSettings settingsBlock = ChartConfiguration.BARCHARTCONFIG.getBlockSettings();
	
	public GeneralPageBar(Composite parent, int style) {
		
		super(parent, SWT.NONE);
		
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		fillLayout.marginWidth = 5;
		fillLayout.marginHeight = 5;
		setLayout(fillLayout);
		
		Group grpTitleSettings = new Group(this, SWT.NONE);
		grpTitleSettings.setText("Title Settings");
		grpTitleSettings.setLayout(new GridLayout(2, false));
		
		Label lblSetTitle = new Label(grpTitleSettings, SWT.NONE);
		lblSetTitle.setText("Chart Title");
		
		text = new Text(grpTitleSettings, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFontSize = new Label(grpTitleSettings, SWT.NONE);
		lblFontSize.setText("Title Font Size");
		
		comboTitleSize = new Combo(grpTitleSettings, SWT.READ_ONLY);
		comboTitleSize.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		comboTitleSize.add("12");
		comboTitleSize.add("14");
		comboTitleSize.add("16");
		comboTitleSize.add("18");
		comboTitleSize.add("20");
		comboTitleSize.add("22");
		comboTitleSize.add("24");
		comboTitleSize.add("26");
		comboTitleSize.add("28");
		comboTitleSize.add("36");
		comboTitleSize.add("48");
		comboTitleSize.add("72");
		comboTitleSize.select(0);
		
		Label lblColor = new Label(grpTitleSettings, SWT.NONE);
		GridData gd_lblColor = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblColor.widthHint = 150;
		lblColor.setLayoutData(gd_lblColor);
		lblColor.setText("Title Color");
		
		labelShowColor = new Label(grpTitleSettings, SWT.BORDER);
		GridData gd_labelShowColor = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelShowColor.widthHint = 100;
		labelShowColor.setLayoutData(gd_labelShowColor);
		labelShowColor.setBackground(new Color(parent.getShell().getDisplay(), new RGB(255,255,255)));
		
		Label lblFont = new Label(grpTitleSettings, SWT.NONE);
		lblFont.setText("Font");
		
		Composite composite = new Composite(grpTitleSettings, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		
		btnUnderline = new Button(composite, SWT.CHECK);
		btnUnderline.setText("Underline");
		
		btnItalic = new Button(composite, SWT.CHECK);
		btnItalic.setText("Italic");
		
		btnBolt = new Button(composite, SWT.CHECK);
		btnBolt.setText("Bolt");
		labelShowColor.addMouseListener(this);
		
		Group grpBlockSettings = new Group(this, SWT.NONE);
		grpBlockSettings.setText("Block Settings");
		grpBlockSettings.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(grpBlockSettings, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 150;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("Block Outline Style");
		
		comboBlockOutline = new Combo(grpBlockSettings, SWT.READ_ONLY);
		comboBlockOutline.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		comboBlockOutline.add("None");
		comboBlockOutline.add("Dotted");
		comboBlockOutline.add("Dash-Dotted");
		comboBlockOutline.add("Dashed");
		comboBlockOutline.add("Solid");
		comboBlockOutline.select(0);
		
		Label lblColor_1 = new Label(grpBlockSettings, SWT.NONE);
		lblColor_1.setText("Background Color");
		
		labelShowColor2 = new Label(grpBlockSettings, SWT.BORDER);
		GridData gd_labelShowColor2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelShowColor2.widthHint = 100;
		labelShowColor2.setLayoutData(gd_labelShowColor2);
		labelShowColor2.setText(" ");
		labelShowColor2.setBackground(PageSupport.getColor(parent, 0));
		
		Label lblLables = new Label(grpBlockSettings, SWT.NONE);
		lblLables.setText("Bar Labels");
		
		btnShowLables = new Button(grpBlockSettings, SWT.CHECK);
		btnShowLables.setText("Show Labels in Chart");
		labelShowColor2.addMouseListener(this);
		
		loadSettings();

	}
	@Override
	public void mouseUp(MouseEvent e) {
		if(e.getSource() == labelShowColor) {
			RGB rgb = PageSupport.openAndGetColor(this.getParent(), labelShowColor);
		}
		if(e.getSource() == labelShowColor2) {
			RGB rgb = PageSupport.openAndGetColor(this.getParent(), labelShowColor2);
		}		
	}
	@Override
	public void saveSettings() {
		
		settingsGeneral.setChartTitle(getTitle());
		settingsGeneral.setChartTitleColor(getTitleColor());
		settingsGeneral.setChartTitleSize(getTitleSize());
		settingsGeneral.setChartTitleBold(getBolt());
		settingsGeneral.setChartTitleItalic(getItalic());
		settingsGeneral.setChartTitleUnderline(getUnterline());
		
		settingsGeneral.setChartShowLabels(isChartShowLabels());//
	
		settingsBlock.setBlockBackgroundRGB(getBlockColor());
		
		if(getBlockOutline() == null)
			settingsBlock.setBlockShowOutline(false);
		else {
			settingsBlock.setBlockShowOutline(true);
			settingsBlock.setBlockOutlineStyle(getBlockOutline());
		}
	}
	@Override
	public void loadSettings() {
		setTitle(settingsGeneral.getChartTitle());
		setTitleColor(settingsGeneral.getChartTitleColor());
		setTitleSize(settingsGeneral.getChartTitleSize());
		setBolt(settingsGeneral.isChartTitleBold());
		setItalic(settingsGeneral.isChartTitleItalic());
		setUnterline(settingsGeneral.isChartTitleUnderline());
		setBlockColor(settingsBlock.getBlockBackgroundRGB());
		
		setChartShowLabels(settingsGeneral.isChartShowLabels());//
		
		if(settingsBlock.isBlockShowOutline())
			setBlockOutline(settingsBlock.getBlockOutlineStyle());
		else
			setBlockOutline(null);
	}
	
	private boolean getBolt() {return btnBolt.getSelection();}
	private void setBolt(boolean value) {btnBolt.setSelection(value);}
	
	private boolean getItalic() {return btnItalic.getSelection();}
	private void setItalic(boolean value) {btnItalic.setSelection(value);}
	
	private boolean getUnterline() {return btnUnderline.getSelection();}
	private void setUnterline(boolean value) {btnUnderline.setSelection(value);}
	
	private String getTitle() {return text.getText();}
	private void setTitle(String title) {text.setText(title);}
	
	private int getTitleSize() {return Integer.valueOf(comboTitleSize.getItem(comboTitleSize.getSelectionIndex()));}
	private void setTitleSize(int size) {comboTitleSize.select(PageSupport.setFontSize(size));}
	
	private LineStyle getBlockOutline() {return PageSupport.getLineStyle(comboBlockOutline.getSelectionIndex());}
	private void setBlockOutline(LineStyle lineStyle) {comboBlockOutline.select((PageSupport.setLineStyle(lineStyle)));}
	
	private RGB getTitleColor() {return labelShowColor.getBackground().getRGB();}
	private void setTitleColor(RGB rgb) {labelShowColor.setBackground(new Color(this.getDisplay(), rgb));}
	
	private RGB getBlockColor() {return labelShowColor2.getBackground().getRGB();}
	private void setBlockColor(RGB rgb) {labelShowColor2.setBackground(new Color(this.getDisplay(), rgb));}
	
	private boolean isChartShowLabels() {return btnShowLables.getSelection();}
	private void setChartShowLabels(boolean value) {btnShowLables.setSelection(value);}

	@Override
	protected void checkSubclass() {}
	@Override
	public void mouseDoubleClick(MouseEvent e) {}
	@Override
	public void mouseDown(MouseEvent e) {}
}
