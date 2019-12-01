package de.tudresden.slr.questionnaire.wizard;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import de.tudresden.slr.questionnaire.Questionnaire;
import de.tudresden.slr.questionnaire.QuestionnaireStorage;
import de.tudresden.slr.questionnaire.model.Question;

public class NewQuestionWizard extends Wizard {

    private NewQuestionWizardPage page;
    private QuestionnaireStorage storage;
    private Questionnaire questionnaire;
    private Question<?> initialQuestion;

    /**
     * @param questionnaire
     *            The {@link Questionnaire} into which the new question will be inserted
     * @param initialQuestion
     *            Initialize the wizard's page such that it would create an equivalent {@link Question}. If it is
     *            {@code null}, no initialization will take place.
     */
    public NewQuestionWizard(Questionnaire questionnaire, Question<?> initialQuestion) {
        super();
        setWindowTitle("New Question");
        if (questionnaire == null)
            throw new NullPointerException();
        this.questionnaire = questionnaire;
        this.storage = QuestionnaireStorage.getInstance();
        this.initialQuestion = initialQuestion;
        page = new NewQuestionWizardPage("new question page 1", initialQuestion);
    }

    @Override
    public void addPages() {
        addPage(page);
    }

    @Override
    public boolean performFinish() {
        try {
            Question<?> q = page.generateQuestion();
            if (initialQuestion == null) {
                questionnaire.addQuestion(q);
            } else {
                if (!showEditWarning())
                    return false;
                questionnaire.replaceQuestion(initialQuestion, q);
            }
            questionnaire.setDirty(true);
            storage.persist(questionnaire);
            return true;
        } catch (BadUserInputException e) {
            page.setErrorMessage(e.getMessage());
            return false;
        }
    }

    private boolean showEditWarning() {
        MessageBox dialog = new MessageBox(getShell(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
        dialog.setText("Editing Question");
        dialog.setMessage("Editing a question will reset all existing answers it used to have. Are you sure?");
        return dialog.open() == SWT.OK;
    }

}
