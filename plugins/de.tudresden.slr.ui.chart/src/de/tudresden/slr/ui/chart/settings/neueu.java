package de.tudresden.slr.ui.chart.settings;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;

public class neueu extends Composite {
	private Text titleEdit;
	Label colorLabel;

	
	public neueu(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginBottom = 5;
		gridLayout.marginRight = 5;
		gridLayout.marginLeft = 5;
		gridLayout.marginTop = 5;
		setLayout(gridLayout);
		
		Label labelTitle = new Label(this, SWT.NONE);
		GridData gd_labelTitle = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelTitle.widthHint = 150;
		labelTitle.setLayoutData(gd_labelTitle);
		labelTitle.setText("Title Text");
		
		titleEdit = new Text(this, SWT.BORDER);
		titleEdit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFontSize = new Label(this, SWT.NONE);
		lblFontSize.setText("Font Size");
		
		Combo combo = new Combo(this, SWT.READ_ONLY);
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_combo.widthHint = 50;
		combo.setLayoutData(gd_combo);
		combo.add("12");
		combo.add("14");
		combo.add("16");
		combo.add("18");
		combo.add("20");
		combo.add("22");
		combo.add("24");
		combo.add("26");
		combo.add("28");
		combo.add("36");
		combo.add("48");
		combo.add("72");
		
		Label lblColor = new Label(this, SWT.NONE);
		lblColor.setText("Color");
		
		Composite composite_1 = new Composite(this, SWT.NONE);
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_composite_1.widthHint = 173;
		gd_composite_1.heightHint = 35;
		composite_1.setLayoutData(gd_composite_1);
		GridLayout gl_composite_1 = new GridLayout(2, false);
		gl_composite_1.marginWidth = 0;
		composite_1.setLayout(gl_composite_1);
		
		Label lblNewLabel = new Label(composite_1, SWT.BORDER);
		lblNewLabel.setBackground(new Color(parent.getShell().getDisplay(), new RGB(255,255,255)));
		lblNewLabel.setText("                             ");
		
		Button btnNewButton = new Button(composite_1, SWT.NONE);
		btnNewButton.setText("Set Color");
		
		Label lblOther = new Label(this, SWT.NONE);
		lblOther.setText("Other");
		
		Composite composite = new Composite(this, SWT.NONE);
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_composite.widthHint = 342;
		composite.setLayoutData(gd_composite);
		GridLayout gl_composite = new GridLayout(4, false);
		gl_composite.marginWidth = 0;
		composite.setLayout(gl_composite);
		
		Button btnCheckButton = new Button(composite, SWT.CHECK);
		btnCheckButton.setText("Strike Through");
		
		Button btnCheckButton_1 = new Button(composite, SWT.CHECK);
		btnCheckButton_1.setText("Underline");
		
		Button btnCheckButton_2 = new Button(composite, SWT.CHECK);
		btnCheckButton_2.setText("Italic");
		
		Button btnCheckButton_3 = new Button(composite, SWT.CHECK);
		btnCheckButton_3.setText("Bold");
		
		Label lblBackgroundColor = new Label(this, SWT.NONE);
		lblBackgroundColor.setText("Background Color");
		
		Composite composite_2 = new Composite(this, SWT.NONE);
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

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
