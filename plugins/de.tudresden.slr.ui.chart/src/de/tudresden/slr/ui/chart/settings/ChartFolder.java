package de.tudresden.slr.ui.chart.settings;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
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
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public abstract class ChartFolder implements SelectionListener{
	protected Text titleEdit;
	protected Text textTitel;
	protected RGB colorTitle;
	protected RGB colorBackground;
	protected RGB colorLegend;
	
	protected Combo comboTitleSize;
	protected Combo comboBlockOutline;
	protected Combo comboOutline;
	protected Combo comboSeparator;
	protected Combo comboPosition;
	
	protected Button checkBoxStrike;
	protected Button checkBoxUnderline;
	protected Button checkBoxItalic;
	protected Button checkBoxBolt;
	protected Button buttonBlockColor;
	protected Button buttonTitleColor;
	protected Button buttonSetColorBackground;
	
	
	protected Label labelBlockColor;
	protected Label labelTitleColor;
	protected Label labelLegendColor;
	
	
	protected TabFolder tabFolder;
	
	protected Scale scalePercent;
	
	
	
	protected abstract void build(TabFolder tabFolder);
	
	protected void buildGeneralItem(Composite parent, TabItem tabItem) {	
				
		tabItem.setText("General");
		tabItem.setControl(parent);
		
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		fillLayout.marginWidth = 5;
		fillLayout.marginHeight = 5;
		parent.setLayout(fillLayout);
		
		Group groupTitle = new Group(parent, SWT.NONE);
		groupTitle.setText("Title Settings");
		groupTitle.setLayout(new GridLayout(2, false));
		
		Label lblTitleText = new Label(groupTitle, SWT.NONE);
		GridData gd_lblTitleText = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblTitleText.widthHint = 120;
		lblTitleText.setLayoutData(gd_lblTitleText);
		lblTitleText.setText("Title Text");
		
		titleEdit = new Text(groupTitle, SWT.BORDER);
		titleEdit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFontSize = new Label(groupTitle, SWT.NONE);
		lblFontSize.setText("Font Size");
		
		comboTitleSize = new Combo(groupTitle, SWT.READ_ONLY);
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
		
		Label lblColor = new Label(groupTitle, SWT.NONE);
		lblColor.setText("Color");
		
		Composite composite_1 = new Composite(groupTitle, SWT.NONE);
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_composite_1.widthHint = 173;
		gd_composite_1.heightHint = 35;
		composite_1.setLayoutData(gd_composite_1);
		GridLayout gl_composite_1 = new GridLayout(2, false);
		gl_composite_1.marginWidth = 0;
		composite_1.setLayout(gl_composite_1);
		
		labelTitleColor = new Label(composite_1, SWT.BORDER);
		labelTitleColor.setBackground(new Color(groupTitle.getShell().getDisplay(), new RGB(255,255,255)));;
		labelTitleColor.setText("                             ");
		colorTitle = labelTitleColor.getBackground().getRGB();
		
		buttonTitleColor = new Button(composite_1, SWT.NONE);
		buttonTitleColor.setText("Set Color");
		buttonTitleColor.addSelectionListener(this);
		
		Group groupBlock = new Group(parent, SWT.NONE);
		groupBlock.setText("Block Settings");
		groupBlock.setLayout(new GridLayout(2, false));
		
		Label lblOutlineStyle = new Label(groupBlock, SWT.NONE);
		GridData gd_lblOutlineStyle = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblOutlineStyle.widthHint = 120;
		lblOutlineStyle.setLayoutData(gd_lblOutlineStyle);
		lblOutlineStyle.setText("Outline Style");
		
		comboBlockOutline = new Combo(groupBlock, SWT.READ_ONLY);
		comboBlockOutline.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboBlockOutline.add("None");
		comboBlockOutline.add("Dotted");
		comboBlockOutline.add("Dash-Dotted");
		comboBlockOutline.add("Dashed");
		comboBlockOutline.add("Solid");
		comboBlockOutline.select(0);
		Label lblColor_1 = new Label(groupBlock, SWT.NONE);
		lblColor_1.setText("Color");
		
		Composite composite_2 = new Composite(groupBlock, SWT.NONE);
		GridLayout gl_composite_2 = new GridLayout(2, false);
		gl_composite_2.marginWidth = 0;
		composite_2.setLayout(gl_composite_2);
		
		labelBlockColor = new Label(composite_2, SWT.BORDER);
		labelBlockColor.setBackground(new Color(parent.getDisplay(),new RGB(255,255,255)));
		labelBlockColor.setText("                             ");
		colorBackground = labelBlockColor.getBackground().getRGB();
		
		buttonBlockColor = new Button(composite_2, SWT.NONE);
		buttonBlockColor.setText("Set Color");
		buttonBlockColor.addSelectionListener(this);
		
		Label lblOther = new Label(groupTitle, SWT.NONE);
		lblOther.setText("Other");
		
		Composite composite = new Composite(groupTitle, SWT.NONE);
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_composite.widthHint = 342;
		composite.setLayoutData(gd_composite);
		GridLayout gl_composite = new GridLayout(4, false);
		gl_composite.marginWidth = 0;
		composite.setLayout(gl_composite);
		
		checkBoxUnderline = new Button(composite, SWT.CHECK);
		checkBoxUnderline.setText("Underline");
		
		checkBoxItalic = new Button(composite, SWT.CHECK);
		checkBoxItalic.setText("Italic");
		
		checkBoxBolt= new Button(composite, SWT.CHECK);
		checkBoxBolt.setText("Bold");
	}
	
	protected void buildLegendItem(Composite parent, TabItem tabItem) {
		
		tabItem.setText("Legend");
		tabItem.setControl(parent);
		parent.setLayout(new GridLayout(2, false));
		
		Label labelBackgroundLegendColor = new Label(parent, SWT.NONE);
		GridData gd_labelBackgroundColor = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelBackgroundColor.widthHint = 120;
		labelBackgroundLegendColor.setLayoutData(gd_labelBackgroundColor);
		labelBackgroundLegendColor.setText("Background Color");
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		labelLegendColor = new Label(composite, SWT.BORDER);
		labelLegendColor.setBackground(new Color(parent.getShell().getDisplay(), new RGB(255,255,255)));
		labelLegendColor.setText("                             ");
		
		colorLegend = labelLegendColor.getBackground().getRGB();
		
		buttonSetColorBackground = new Button(composite, SWT.NONE);
		buttonSetColorBackground.setText("Set Color");
		buttonSetColorBackground.addSelectionListener(this);
		
		Label lblOutline = new Label(parent, SWT.NONE);
		lblOutline.setText("Outline");
		
		comboOutline = new Combo(parent, SWT.READ_ONLY);
		comboOutline.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboOutline.add("None");
		comboOutline.add("Dash Dotted");
		comboOutline.add("Dashed");
		comboOutline.add("Dotted");
		comboOutline.add("Solid");
		comboOutline.select(0);
		
		Label lblSeparator = new Label(parent, SWT.NONE);
		lblSeparator.setText("Separator");
		
		comboSeparator = new Combo(parent, SWT.READ_ONLY);
		comboSeparator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboSeparator.add("None");
		comboSeparator.add("Dash Dotted");
		comboSeparator.add("Dashed");
		comboSeparator.add("Dotted");
		comboSeparator.add("Solid");
		comboSeparator.select(0);
		
		Label lblMaxPercent = new Label(parent, SWT.NONE);
		lblMaxPercent.setText("Max. Percent");
		
		scalePercent = new Scale(parent, SWT.NONE);
		scalePercent.setPageIncrement(5);
		scalePercent.setMaximum(50);
		scalePercent.setMinimum(10);
		
		Label lblPosition = new Label(parent, SWT.NONE);
		lblPosition.setText("Position");
		
		comboPosition = new Combo(parent, SWT.READ_ONLY);
		comboPosition.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboPosition.add("Right");
		comboPosition.add("Left");
		comboPosition.add("Below");
		comboPosition.add("Top");
		comboPosition.select(0);
		
		Label lblTitel = new Label(parent, SWT.NONE);
		lblTitel.setText("Titel");
		
		
		textTitel = new Text(parent, SWT.BORDER);
		textTitel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));	
		
		
	}
}
