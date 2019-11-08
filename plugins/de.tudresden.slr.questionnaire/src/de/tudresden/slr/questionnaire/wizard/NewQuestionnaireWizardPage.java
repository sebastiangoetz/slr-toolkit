package de.tudresden.slr.questionnaire.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class NewQuestionnaireWizardPage extends WizardNewFileCreationPage {

	public NewQuestionnaireWizardPage(IStructuredSelection selection) {
		super("NeqQuestionnaireWizardPage", selection);
		setTitle("New Questionnaire");
		setDescription("Create a new questionnaire");
		setFileExtension("questionnaire");
	}
}
