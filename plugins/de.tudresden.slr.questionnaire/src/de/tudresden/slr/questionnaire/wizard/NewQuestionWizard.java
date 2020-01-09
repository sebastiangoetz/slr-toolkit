package de.tudresden.slr.questionnaire.wizard;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import de.tudresden.slr.questionnaire.Questionnaire;
import de.tudresden.slr.questionnaire.QuestionnaireStorage;
import de.tudresden.slr.questionnaire.model.ChoiceQuestion;
import de.tudresden.slr.questionnaire.model.FreeTextQuestion;
import de.tudresden.slr.questionnaire.model.MultipleChoiceQuestion;
import de.tudresden.slr.questionnaire.model.Question;
import de.tudresden.slr.questionnaire.model.SingleChoiceQuestion;

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

    /**
     * Asks user for confirmation
     * 
     * @return {@code true} if user accepted
     */
    private boolean showEditWarning() {
        MessageBox dialog = new MessageBox(getShell(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
        dialog.setText("Editing Question");
        dialog.setMessage("Editing a question will reset all existing answers it used to have. Are you sure?");
        return dialog.open() == SWT.OK;
    }

}

class NewQuestionWizardPage extends WizardPage {

    private Text textQuestionDescription;
    private Text textQuestionChoices;
    private Button buttonMultipleChoice;

    private Question<?> initialQuestion;

    protected NewQuestionWizardPage(String pageName, Question<?> initialQuestion) {
        super(pageName);
        setTitle("New Question");
        setDescription("Create a new question");
        this.initialQuestion = initialQuestion;
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, 0);
        container.setLayout(new RowLayout(SWT.VERTICAL));
        setControl(container);

        Label l1 = new Label(container, 0);
        l1.setText("Question Text");
        textQuestionDescription = new Text(container, SWT.BORDER);
        textQuestionDescription.setLayoutData(new RowData(500, SWT.DEFAULT));

        Group g = new Group(container, 0);
        g.setText("Presets");
        g.setLayout(new RowLayout(SWT.HORIZONTAL));
        createButtonPresetLikert4(g);
        createButtonPresetLikert5(g);

        Label l2 = new Label(container, 0);
        l2.setText("Choices, each line is one possible choice.\nLeave blank if you want free text answers instead.");

        textQuestionChoices = new Text(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
        textQuestionChoices.setLayoutData(new RowData(500, textQuestionChoices.getLineHeight() * 6));

        buttonMultipleChoice = new Button(container, SWT.CHECK);
        buttonMultipleChoice.setText("Allow multiple-choice");

        initFromQuestion(initialQuestion);
    }

    public Question<?> generateQuestion() throws BadUserInputException {
        String description = textQuestionDescription.getText();
        if (description.equals(""))
            throw new BadUserInputException("Description can't be empty");

        List<String> choices = Arrays.stream(textQuestionChoices.getText().split("\n")).map(x -> x.trim())
                .filter(x -> !x.isEmpty()).collect(Collectors.toList());

        if (choices.size() == 1)
            throw new BadUserInputException("Must offer more than one choice, or none at all");

        if (choices.size() == 0) {
            FreeTextQuestion q = new FreeTextQuestion();
            q.setQuestionText(description);
            return q;
        } else {
            boolean multiChoice = buttonMultipleChoice.getSelection();
            ChoiceQuestion<?> q = multiChoice ? new MultipleChoiceQuestion() : new SingleChoiceQuestion();
            q.setQuestionText(description);
            choices.forEach(q::addChoice);
            return q;
        }
    }

    private Button createButtonPresetLikert4(Composite parent) {
        String[] choices = new String[] { "Strongly disagree", "Disagree", "Agree", "Strongly agree" };
        return createButtonPreset(parent, "Likert 4", choices);
    }

    private Button createButtonPresetLikert5(Composite parent) {
        String[] choices = new String[] { "Strongly disagree", "Disagree", "Neutral", "Agree", "Strongly agree" };
        return createButtonPreset(parent, "Likert 5", choices);
    }

    private Button createButtonPreset(Composite parent, String buttonName, String[] choices) {
        Button b = new Button(parent, SWT.PUSH);
        b.setText(buttonName);
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                String text = String.join("\n", choices);
                textQuestionChoices.setText(text);
            }
        });
        return b;
    }

    private void initFromQuestion(Question<?> question) {
        if (question == null)
            return;
        textQuestionDescription.setText(question.getQuestionText());
        buttonMultipleChoice.setSelection(question instanceof MultipleChoiceQuestion);
        if (question instanceof ChoiceQuestion) {
            List<String> options = ((ChoiceQuestion<?>) question).getChoices();
            String text = String.join("\n", options);
            textQuestionChoices.setText(text);
        }
    }

}
