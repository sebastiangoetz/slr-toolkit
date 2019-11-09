package de.tudresden.slr.questionnaire.questionview;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.questionnaire.model.Question;
import de.tudresden.slr.questionnaire.model.SingleChoiceQuestion;

public class SingleChoiceQuestionView extends QuestionViewBase<SingleChoiceQuestion> {

	public SingleChoiceQuestionView(Composite parent, SingleChoiceQuestion question,
			Supplier<Document> documentSupplier, Consumer<Question<?>> onQuestionChanged) {
		super(parent, question, documentSupplier, onQuestionChanged);
	}

	@Override
	protected List<Control> renderControls() {
		List<Control> controls = new LinkedList<>();
		for (String choice : question.getChoices()) {
			Button btn = new Button(root, SWT.RADIO);
			btn.setText(choice);
			btn.setSelection(choice.equals(question.getAnswer(getDocumentKey())));
			btn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					question.addAnswer(getDocumentKey(), choice);
					onQuestionChanged.accept(question);
				}
			});
			controls.add(btn);
		}
		return controls;
	}

}
