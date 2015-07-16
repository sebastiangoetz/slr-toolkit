package de.tudresden.slr.wizards.pages;

import org.eclipse.swt.widgets.Composite;

public class WizardSetupBibtexPage extends WizardSetupPage {

	public WizardSetupBibtexPage(String pageName) {
		super(pageName, new String[] { "*.bib" });
		setTitle("Bibtex setup");
		setDescription("Create new or import existing BibTeX file");
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		label.setText("BibTeX file name");
		textField.setText("my_bibtex.bib");
	}
}
