package de.tudresden.slr.questionnaire;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.questionnaire.model.FreeTextQuestion;
import de.tudresden.slr.questionnaire.model.MultipleChoiceQuestion;
import de.tudresden.slr.questionnaire.model.Question;
import de.tudresden.slr.questionnaire.model.SingleChoiceQuestion;

public class QuestionView {

	private Composite parent;
	private Composite root;
	private Question<?> question;

	private Supplier<Document> documentSupplier;
	private Consumer<Question<?>> questionEdited;

	private Collection<Control> answerControls = new LinkedList<>();

	public QuestionView(Composite parent, Question<?> question, Supplier<Document> documentSupplier,
			Consumer<Question<?>> questionEdited) {
		this.parent = parent;
		this.question = question;
		this.documentSupplier = documentSupplier;
		this.questionEdited = questionEdited;
	}

	public void render() {
		renderBase();
		if (question instanceof FreeTextQuestion)
			renderFreeText();
		else if (question instanceof MultipleChoiceQuestion)
			renderMultipleChoice();
		else if (question instanceof SingleChoiceQuestion)
			renderSingleChoice();
		else
			throw new RuntimeException(new ClassNotFoundException(question.getClass()
					.getName()));

	}

	private void renderBase() {
		this.root = new Composite(parent, 0);
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.fill = true;
		root.setLayout(layout);
		Label l = new Label(root, 0);
		l.setText(question.getQuestionText());
	}

	private void renderFreeText() {
		Text t = new Text(root, SWT.MULTI | SWT.V_SCROLL);
		RowData d = new RowData();
		d.width = 300;
		d.height = t.getLineHeight() * 3;
		t.setLayoutData(d);
		answerControls.add(t);

		final FreeTextQuestion question = (FreeTextQuestion) this.question;

		t.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				Document d = documentSupplier.get();
				question.addAnswer(d.getKey(), t.getText());
				questionEdited.accept(question);
			}
		});

		try {
			Document doc = documentSupplier.get();
			String existingAnswer = question.getAnswer(doc.getKey());
			if (existingAnswer != null)
				t.setText(existingAnswer);
		} catch (NullPointerException npe) {
			/* ignore npe */
		}
	}

	private void renderMultipleChoice() {
		MultipleChoiceQuestion question = (MultipleChoiceQuestion) this.question;
		for (String choice : question.getChoices()) {
			Button b = new Button(root, SWT.CHECK);
			b.setText(choice);
			answerControls.add(b);
		}
	}

	private void renderSingleChoice() {
		SingleChoiceQuestion question = (SingleChoiceQuestion) this.question;
		for (String choice : question.getChoices()) {
			Button b = new Button(root, SWT.RADIO);
			b.setText(choice);
			answerControls.add(b);
		}
	}

	public void enableAnswering(boolean shouldEnable) {
		answerControls.forEach(it -> it.setEnabled(shouldEnable));
	}
}
