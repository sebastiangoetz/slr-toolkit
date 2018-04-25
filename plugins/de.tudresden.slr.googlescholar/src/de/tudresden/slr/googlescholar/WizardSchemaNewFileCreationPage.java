package de.tudresden.slr.googlescholar;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class WizardSchemaNewFileCreationPage extends WizardNewFileCreationPage {

	public WizardSchemaNewFileCreationPage(IStructuredSelection selection) {
		super("Import from Google Scholar", selection);
		
		setTitle("Google Scholar Import Wizard");
		setDescription("Define the Destination of your Google Scholar Import");
		setFileExtension("bib");
	}

}
