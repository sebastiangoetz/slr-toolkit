package de.tudresden.slr.questionnaire.wizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.dialogs.NewWizard;

import de.tudresden.slr.questionnaire.Questionnaire;
import de.tudresden.slr.questionnaire.QuestionnaireStorage;

public class NewQuestionnaireWizard extends NewWizard {

	private NewQuestionnaireWizardPage page;
	private IStructuredSelection selection;

	public NewQuestionnaireWizard() {
		setWindowTitle("New Questionnaire");
	}

	@Override
	public void init(IWorkbench aWorkbench, IStructuredSelection currentSelection) {
		selection = currentSelection;
	}

	@Override
	public void addPages() {
		page = new NewQuestionnaireWizardPage(selection);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		IFile f = page.createNewFile();
		if (f == null) {
			return false;
		}
		Questionnaire q = new Questionnaire(f.getName());
		QuestionnaireStorage.getInstance().persist(f, q);
		return true;
	}

}
