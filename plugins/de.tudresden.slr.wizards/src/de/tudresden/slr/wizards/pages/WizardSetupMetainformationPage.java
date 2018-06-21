package de.tudresden.slr.wizards.pages;

import org.eclipse.swt.widgets.Composite;

public class WizardSetupMetainformationPage extends WizardSetupPage {

	public WizardSetupMetainformationPage(String pageName) {
		super(pageName, new String[] { "*.slrproject" });
		setTitle("Metainformation setup");
		setDescription("Create new or import existing metainformation file for the SLR project");
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		label.setText("Metainformation file name");
		textField.setText("my_metainformation.slrproject");
	}

	@Override
	public String getFilePath() {
		String result = super.getFilePath();
		return result.isEmpty() ? "my_metainformation.slrproject" : result;
	}
}
