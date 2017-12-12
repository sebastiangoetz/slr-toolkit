package de.tudresden.slr.ui.chart.settings;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public abstract class ChartFolder {
	protected Text titleEdit;
	protected RGB colorTitle;
	protected RGB colorBackground;
	protected Combo comboTitleSize;
	
	protected Button checkBoxStrike;
	protected Button checkBoxUnderline;
	protected Button checkBoxItalic;
	protected Button checkBoxBolt;
	
	
	protected TabFolder tabFolder;
	
	protected abstract void build(TabFolder tabFolder);
	
	protected void buildGeneralItem(Composite parent, TabItem tabItem) {	
				
		tabItem.setText("General");
		tabItem.setControl(parent);
		
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = 30;
		gridLayout.marginBottom = 5;
		gridLayout.marginRight = 5;
		gridLayout.marginLeft = 5;
		gridLayout.marginTop = 5;
		parent.setLayout(gridLayout);
		
		Label labelTitle = new Label(parent, SWT.NONE);
		GridData gd_labelTitle = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelTitle.widthHint = 150;
		labelTitle.setLayoutData(gd_labelTitle);
		labelTitle.setText("Title Text");
		
		titleEdit = new Text(parent, SWT.BORDER);
		titleEdit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFontSize = new Label(parent, SWT.NONE);
		lblFontSize.setText("Font Size");
		
		comboTitleSize = new Combo(parent, SWT.READ_ONLY);
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_combo.widthHint = 50;
		comboTitleSize.setLayoutData(gd_combo);
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
		
		Label lblColor = new Label(parent, SWT.NONE);
		lblColor.setText("Color");
		
		Composite composite_1 = new Composite(parent, SWT.NONE);
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_composite_1.widthHint = 173;
		gd_composite_1.heightHint = 35;
		composite_1.setLayoutData(gd_composite_1);
		GridLayout gl_composite_1 = new GridLayout(2, false);
		gl_composite_1.marginWidth = 0;
		composite_1.setLayout(gl_composite_1);
		
		Label lblNewLabel = new Label(composite_1, SWT.BORDER);
		lblNewLabel.setBackground(new Color(parent.getShell().getDisplay(), new RGB(255,255,255)));;
		lblNewLabel.setText("                             ");
		
		Button btnNewButton = new Button(composite_1, SWT.NONE);
		btnNewButton.setText("Set Color");
		btnNewButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColorDialog dlg = new ColorDialog(parent.getShell());
				dlg.setRGB(lblNewLabel.getBackground().getRGB());
				dlg.setText("Choose a Color");
				RGB rgb = dlg.open();
		        if (rgb != null) {
		        	colorTitle = rgb;
		        	lblNewLabel.setBackground(new Color(parent.getShell().getDisplay(), rgb));
		        }
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Label lblOther = new Label(parent, SWT.NONE);
		lblOther.setText("Other");
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_composite.widthHint = 342;
		composite.setLayoutData(gd_composite);
		GridLayout gl_composite = new GridLayout(4, false);
		gl_composite.marginWidth = 0;
		composite.setLayout(gl_composite);
		
		checkBoxStrike = new Button(composite, SWT.CHECK);
		checkBoxStrike.setText("Strike Through");
		
		checkBoxUnderline = new Button(composite, SWT.CHECK);
		checkBoxUnderline.setText("Underline");
		
		checkBoxItalic = new Button(composite, SWT.CHECK);
		checkBoxItalic.setText("Italic");
		
		checkBoxBolt= new Button(composite, SWT.CHECK);
		checkBoxBolt.setText("Bold");
		
		Label lblBackgroundColor = new Label(parent, SWT.NONE);
		lblBackgroundColor.setText("Background Color");
		
		Composite composite_2 = new Composite(parent, SWT.NONE);
		GridData gd_composite_2 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_composite_2.widthHint = 223;
		gd_composite_2.heightHint = 33;
		composite_2.setLayoutData(gd_composite_2);
		GridLayout gl_composite_2 = new GridLayout(2, false);
		gl_composite_2.marginWidth = 0;
		composite_2.setLayout(gl_composite_2);
		
		Label lblNewLabel_1 = new Label(composite_2, SWT.BORDER);
		lblNewLabel_1.setBackground(new Color(parent.getShell().getDisplay(), new RGB(255,255,255)));
		lblNewLabel_1.setText("                             ");
		
		Button btnNewButton_1 = new Button(composite_2, SWT.NONE);
		btnNewButton_1.setText("Set Color");
		btnNewButton_1.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColorDialog dlg = new ColorDialog(parent.getShell());
				dlg.setRGB(lblNewLabel_1.getBackground().getRGB());
				dlg.setText("Choose a Color");
				RGB rgb = dlg.open();
		        if (rgb != null) {
		        	colorBackground = rgb;
		        	lblNewLabel_1.setBackground(new Color(parent.getShell().getDisplay(), rgb));
		        }							
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
