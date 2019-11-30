package de.tudresden.slr.questionnaire.questionview;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import de.tudresden.slr.questionnaire.Questionnaire;
import de.tudresden.slr.questionnaire.model.FreeTextQuestion;
import de.tudresden.slr.questionnaire.model.MultipleChoiceQuestion;
import de.tudresden.slr.questionnaire.model.Question;
import de.tudresden.slr.questionnaire.model.SingleChoiceQuestion;

public abstract class QuestionViewBase<T extends Question<?>> {

    /** Root {@link Composite} that all children are added to */
    protected Composite root;

    /** The {@link Question} associated with this view */
    protected T question;

    /** the document for which the question is rendered */
    protected String document;

    /** all active {@link Control}s that the user can interact with to modify the question */
    private List<Control> controls = new LinkedList<>();

    /** function that is called when the {@link Question} object is modified */
    protected Consumer<Question<?>> onQuestionChanged;

    /** called when the questionnaire itself is modified, e.g. when questions are re-ordered */
    protected Consumer<Questionnaire> onQuestionnaireChanged;

    /** the {@link Questionnaire} that owns this question */
    private Questionnaire questionnaire;

    public QuestionViewBase(Composite parent, T question, Questionnaire questionnaire, String document) {
        this.root = new Composite(parent, SWT.BORDER);
        root.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));

        this.question = question;
        this.questionnaire = questionnaire;

        if (!questionnaire.getQuestions().contains(question))
            throw new IllegalArgumentException();

        renderBase();
        controls = renderControls();
    }

    private void renderBase() {
        root.setLayout(new GridLayout(1, false));
        setupContextMenu();

        Font font = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);
        Label label = new Label(root, SWT.NONE);
        label.setText(question.getQuestionText());
        label.setFont(font);
    }

    private void setupContextMenu() {
        Menu menu = new Menu(root);
        root.setMenu(menu);

        MenuItem moveUp = new MenuItem(menu, SWT.NONE);
        moveUp.setText("Move Up");
        moveUp.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (questionnaire.moveUp(question)) {
                    onQuestionChanged.accept(question);
                    onQuestionnaireChanged.accept(questionnaire);
                }
            }
        });

        MenuItem moveDown = new MenuItem(menu, SWT.NONE);
        moveDown.setText("Move Down");
        moveDown.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (questionnaire.moveDown(question)) {
                    onQuestionChanged.accept(question);
                    onQuestionnaireChanged.accept(questionnaire);
                }
            }
        });

        new MenuItem(menu, SWT.BAR);

        MenuItem edit = new MenuItem(menu, SWT.NONE);
        edit.setText("Edit (not implemented)");
        edit.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // TODO implement, also change the display text once done
            }
        });

        MenuItem delete = new MenuItem(menu, SWT.NONE);
        delete.setText("Delete");
        delete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (questionnaire.remove(question)) {
                    onQuestionChanged.accept(question);
                    onQuestionnaireChanged.accept(questionnaire);
                }
            }
        });
    }

    protected abstract List<Control> renderControls();

    public final void enableControls(boolean shouldEnable) {
        controls.forEach(control -> control.setEnabled(shouldEnable));
    }

    public static QuestionViewBase<?> qvFor(Composite parent, Question<?> question, Questionnaire questionnaire,
            String document) {
        if (question instanceof FreeTextQuestion)
            return new FreeTextQuestionView(parent, (FreeTextQuestion) question, questionnaire, document);
        if (question instanceof SingleChoiceQuestion)
            return new SingleChoiceQuestionView(parent, (SingleChoiceQuestion) question, questionnaire, document);
        if (question instanceof MultipleChoiceQuestion)
            return new MultipleChoiceQuestionView(parent, (MultipleChoiceQuestion) question, questionnaire, document);

        throw new IllegalArgumentException("no implementation for " + question.getClass());
    }

    public Consumer<Questionnaire> getOnQuestionnaireChanged() {
        return onQuestionnaireChanged;
    }

    public void setOnQuestionnaireChanged(Consumer<Questionnaire> onQuestionnaireChanged) {
        this.onQuestionnaireChanged = onQuestionnaireChanged;
    }

    public Consumer<Question<?>> getOnQuestionChanged() {
        return onQuestionChanged;
    }

    public void setOnQuestionChanged(Consumer<Question<?>> onQuestionChanged) {
        this.onQuestionChanged = onQuestionChanged;
    }
}
