package de.tudresden.slr.questionnaire.questionview;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.questionnaire.model.MultipleChoiceQuestion;
import de.tudresden.slr.questionnaire.model.Question;

public class MultipleChoiceQuestionView extends QuestionViewBase<MultipleChoiceQuestion> {

	public MultipleChoiceQuestionView(Composite parent, MultipleChoiceQuestion question,
			Supplier<Document> documentSupplier, Consumer<Question<?>> onQuestionChanged) {
		super(parent, question, documentSupplier, onQuestionChanged);
	}

	@Override
	protected List<Control> renderControls() {
		List<Control> controls = new LinkedList<>();
		
		List<String> existingAnswers = getAnswersNullSafe();
		for (String choice : question.getChoices()) {
			Button btn = new Button(root, SWT.CHECK);
			controls.add(btn);
			btn.setText(choice);
			btn.setSelection(existingAnswers.contains(choice));
			btn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					List<String> answers = getAnswersNullSafe();
					boolean checked = btn.getSelection();
					boolean contained = answers.contains(choice);
					if (checked && !contained)
						answers.add(choice);
					else if (!checked && contained)
						answers.remove(choice);
					else
						throw new IllegalStateException();
					question.addAnswer(getDocumentKey(), answers);
					onQuestionChanged.accept(question);
				}
			});
		}
		return controls;
	}

	private List<String> getAnswersNullSafe() {
		List<String> result = question.getAnswer(getDocumentKey());
		return (result != null) ? result : new LinkedList<>();
	}

}
