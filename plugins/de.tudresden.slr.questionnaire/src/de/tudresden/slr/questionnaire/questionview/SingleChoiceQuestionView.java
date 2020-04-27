package de.tudresden.slr.questionnaire.questionview;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.tudresden.slr.questionnaire.Questionnaire;
import de.tudresden.slr.questionnaire.model.SingleChoiceQuestion;

public class SingleChoiceQuestionView extends QuestionViewBase<SingleChoiceQuestion> {

    public SingleChoiceQuestionView(Composite parent, SingleChoiceQuestion question, Questionnaire questionnaire,
            String document) {
        super(parent, question, questionnaire, document);
    }

    @Override
    protected List<Control> renderControls() {
        List<Control> controls = new LinkedList<>();
        for (String choice : question.getChoices()) {
            Button btn = new Button(root, SWT.RADIO);
            btn.setText(choice);
            btn.setSelection(choice.equals(question.getAnswer(document)));
            btn.addListener(SWT.Selection, new Listener() {
    			@Override
    			public void handleEvent(Event event) {
                    question.addAnswer(document, choice);
                    onQuestionChanged.accept(question);
                }
            });
            controls.add(btn);
        }
        return controls;
    }

}
