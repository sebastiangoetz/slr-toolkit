package de.tudresden.slr.questionnaire.questionview;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

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
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseDown(MouseEvent e) {
                    question.addAnswer(document, choice);
                    onQuestionChanged.accept(question);
                }
            });
            controls.add(btn);
        }
        return controls;
    }

}
