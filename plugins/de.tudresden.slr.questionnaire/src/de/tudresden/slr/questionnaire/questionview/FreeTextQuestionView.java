package de.tudresden.slr.questionnaire.questionview;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import de.tudresden.slr.questionnaire.Questionnaire;
import de.tudresden.slr.questionnaire.model.FreeTextQuestion;

public class FreeTextQuestionView extends QuestionViewBase<FreeTextQuestion> {

	public FreeTextQuestionView(Composite parent, FreeTextQuestion question, Questionnaire questionnaire,
            String document) {
        super(parent, question, questionnaire, document);
    }

    @Override
	protected List<Control> renderControls() {
		List<Control> controls = new LinkedList<>();

		Text text = new Text(root, SWT.MULTI | SWT.WRAP | SWT.BORDER);
		int textWidth = 300;
		int textHeight = text.getLineHeight();
		text.setSize(textWidth, textHeight);
		try {
			text.setText(question.getAnswer(document));
		} catch (NullPointerException | IllegalArgumentException e) {
			text.setText("");
		}
		GridData gd = new GridData(SWT.FILL, SWT.BEGINNING, false, true);
		gd.heightHint = text.getLineHeight() * 4;
		gd.widthHint = 300;
		text.setLayoutData(gd);

		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				question.addAnswer(document, text.getText());
				onQuestionChanged.accept(question);
			}
		});

		controls.add(text);
		return controls;
	}

}
