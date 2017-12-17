package de.tudresden.slr.ui.chart.settings;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.wb.swt.SWTResourceManager;

public class neueu extends Composite {
	Label colorLabel;
	private Text titleEdit;
	
	protected Button checkBoxStrike;
	protected Button checkBoxUnderline;
	protected Button checkBoxItalic;
	protected Button checkBoxBolt;

	
	public neueu(Composite parent, int style) {
		super(parent, style);
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		fillLayout.marginWidth = 5;
		fillLayout.marginHeight = 5;
		setLayout(fillLayout);
		
		Group groupTitle = new Group(this, SWT.NONE);
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
		
		Combo comboTitleSize = new Combo(groupTitle, SWT.READ_ONLY);
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
		
		Label lblNewLabel = new Label(composite_1, SWT.BORDER);
		lblNewLabel.setBackground(new Color(groupTitle.getShell().getDisplay(), new RGB(255,255,255)));;
		lblNewLabel.setText("                             ");
		
		Button btnNewButton = new Button(composite_1, SWT.NONE);
		btnNewButton.setText("Set Color");
		btnNewButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColorDialog dlg = new ColorDialog(groupTitle.getShell());
				dlg.setRGB(lblNewLabel.getBackground().getRGB());
				dlg.setText("Choose a Color");
				RGB rgb = dlg.open();
		        if (rgb != null) {
		        	//colorTitle = rgb;
		        	lblNewLabel.setBackground(new Color(groupTitle.getShell().getDisplay(), rgb));
		        }
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Group groupBlock = new Group(this, SWT.NONE);
		groupBlock.setText("Block Settings");
		groupBlock.setLayout(new GridLayout(2, false));
		
		Label lblOutlineStyle = new Label(groupBlock, SWT.NONE);
		GridData gd_lblOutlineStyle = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblOutlineStyle.widthHint = 120;
		lblOutlineStyle.setLayoutData(gd_lblOutlineStyle);
		lblOutlineStyle.setText("Outline Style");
		
		Combo combo = new Combo(groupBlock, SWT.NONE);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblColor_1 = new Label(groupBlock, SWT.NONE);
		lblColor_1.setText("Color");
		
		Composite composite_2 = new Composite(groupBlock, SWT.NONE);
		GridLayout gl_composite_2 = new GridLayout(2, false);
		gl_composite_2.marginWidth = 0;
		composite_2.setLayout(gl_composite_2);
		
		Label lblNewLabel_1 = new Label(composite_2, SWT.BORDER);
		lblNewLabel_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblNewLabel_1.setText("                             ");
		
		Button btnNewButton_1 = new Button(composite_2, SWT.NONE);
		btnNewButton_1.setText("Set Color");
		
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

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
