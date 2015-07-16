package de.tudresden.slr.wizards.pages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public abstract class WizardSetupPage extends WizardPage {

	protected boolean importFile;
	protected Text textField;
	protected Label label;
	protected String[] filterExtension;

	public WizardSetupPage(String pageName, String[] filterExtension) {
		super(pageName);
		this.filterExtension = filterExtension;
		importFile = false;
	}

	@Override
	public void createControl(Composite parent) {
		Composite control = new Composite(parent, SWT.NONE);
		control.setLayout(new GridLayout(3, false));

		label = new Label(control, SWT.NONE);

		textField = new Text(control, SWT.BORDER | SWT.SINGLE);
		textField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button button = new Button(control, SWT.PUSH);
		button.setText("Import ...");
		button.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(control.getShell(), SWT.OPEN);
				dialog.setFilterExtensions(filterExtension);
				String path = dialog.open();
				if (path != null) {
					textField.setText(path);
					importFile = true;
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		setControl(control);
	}

	public boolean hasFileImported() {
		return importFile;
	}

	public String getFilePath() {
		return textField.getText();
	}
}
