package de.tudresden.slr.wizards.pages;

import org.eclipse.swt.widgets.Composite;

public class WizardSetupTaxonomyPage extends WizardSetupPage {

	public WizardSetupTaxonomyPage(String pageName) {
		super(pageName, new String[] { "*.taxonomy" });
		setTitle("Taxonomy setup");
		setDescription("Create new or import existing taxonomy file");
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		label.setText("Taxonomy file name");
		textField.setText("my_taxonomy.taxonomy");
	}

	@Override
	public String getFilePath() {
		String result = super.getFilePath();
		return result.isEmpty() ? "my_taxonomy.taxonomy" : result;
	}
}
