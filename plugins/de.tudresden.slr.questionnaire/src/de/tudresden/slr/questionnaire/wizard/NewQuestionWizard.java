package de.tudresden.slr.questionnaire.wizard;

import org.eclipse.jface.wizard.Wizard;

import de.tudresden.slr.questionnaire.Questionnaire;
import de.tudresden.slr.questionnaire.QuestionnaireStorage;
import de.tudresden.slr.questionnaire.model.Question;

public class NewQuestionWizard extends Wizard {

	private NewQuestionWizardPage page;
	private QuestionnaireStorage storage;
	private Questionnaire questionnaire;

	public NewQuestionWizard(Questionnaire questionnaire) {
		super();
		setWindowTitle("New Question");
		this.storage = QuestionnaireStorage.getInstance();

		if (questionnaire == null)
			throw new NullPointerException();
		this.questionnaire = questionnaire;
	}

	@Override
	public void addPages() {
		page = new NewQuestionWizardPage("new question page 1");
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		try {
			Question<?> q = page.generateQuestion();
			questionnaire.addQuestion(q);
			questionnaire.setDirty(true);
			storage.persist(questionnaire);
			return true;
		} catch (BadUserInputException e) {
			page.setErrorMessage(e.getMessage());
		}
		return false;
	}

}
