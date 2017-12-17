package de.tudresden.slr.ui.chart.settings;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;

public class ffffff extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text textTitel;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ffffff(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
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

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(680, 570);
		shell.setText(getText());
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite parent = new Composite(shell, SWT.NONE);
		parent.setLayout(new GridLayout(2, false));
		
		Label labelBackgroundColor = new Label(parent, SWT.NONE);
		GridData gd_labelBackgroundColor = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelBackgroundColor.widthHint = 120;
		labelBackgroundColor.setLayoutData(gd_labelBackgroundColor);
		labelBackgroundColor.setText("Background Color");
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label labelColor = new Label(composite, SWT.BORDER);
		labelColor.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelColor.setText("                             ");
		
		Button buttonSetColor = new Button(composite, SWT.NONE);
		buttonSetColor.setText("Set Color");
		
		Label lblOutline = new Label(parent, SWT.NONE);
		lblOutline.setText("Outline");
		
		Combo comboOutline = new Combo(parent, SWT.READ_ONLY);
		comboOutline.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboOutline.add("None");
		comboOutline.add("Dash Dotted");
		comboOutline.add("Dashed");
		comboOutline.add("Dotted");
		comboOutline.add("Solid");
		comboOutline.select(0);
		
		Label lblSeparator = new Label(parent, SWT.NONE);
		lblSeparator.setText("Separator");
		
		Combo comboSeparator = new Combo(parent, SWT.READ_ONLY);
		comboSeparator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboSeparator.add("None");
		comboSeparator.add("Dash Dotted");
		comboSeparator.add("Dashed");
		comboSeparator.add("Dotted");
		comboSeparator.add("Solid");
		comboSeparator.select(0);
		
		Label lblMaxPercent = new Label(parent, SWT.NONE);
		lblMaxPercent.setText("Max. Percent");
		
		Scale scalePercent = new Scale(parent, SWT.NONE);
		scalePercent.setPageIncrement(5);
		scalePercent.setMaximum(50);
		scalePercent.setMinimum(10);
		
		Label lblPosition = new Label(parent, SWT.NONE);
		lblPosition.setText("Position");
		
		Combo comboPosition = new Combo(parent, SWT.READ_ONLY);
		comboPosition.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboPosition.add("Right");
		comboPosition.add("Left");
		comboPosition.add("Below");
		comboPosition.add("Top");
		
		Label lblTitel = new Label(parent, SWT.NONE);
		lblTitel.setText("Titel");
		
		textTitel = new Text(parent, SWT.BORDER);
		textTitel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

	}
}
