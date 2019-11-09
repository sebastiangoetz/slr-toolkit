package de.tudresden.slr.questionnaire.questionview;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.questionnaire.model.FreeTextQuestion;
import de.tudresden.slr.questionnaire.model.MultipleChoiceQuestion;
import de.tudresden.slr.questionnaire.model.Question;
import de.tudresden.slr.questionnaire.model.SingleChoiceQuestion;

public abstract class QuestionViewBase<T extends Question<?>> {

	/** Root {@link Composite} that all children are added to */
	protected Composite root;

	/** The {@link Question} associated with this view */
	protected T question;

	/**
	 * all active {@link Control}s that the user can interact with to modify the
	 * question
	 */
	private List<Control> controls = new LinkedList<>();

	/** function that returns the currently selected document */
	protected Supplier<Document> documentSupplier;

	/** function that is called when the {@link Question} object is modified */
	protected Consumer<Question<?>> onQuestionChanged;

	public QuestionViewBase(Composite parent, T question, Supplier<Document> documentSupplier,
			Consumer<Question<?>> onQuestionChanged) {
		this.root = new Composite(parent, SWT.BORDER);
		root.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));

		this.question = question;
		this.documentSupplier = documentSupplier;
		this.onQuestionChanged = onQuestionChanged;

		renderBase();
		controls = renderControls();
	}

	private void renderBase() {
		root.setLayout(new GridLayout(1, false));

		Font font = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);
		Label label = new Label(root, SWT.NONE);
		label.setText(question.getQuestionText());
		label.setFont(font);
	}

	protected abstract List<Control> renderControls();

	public final void enableControls(boolean shouldEnable) {
		controls.forEach(control -> control.setEnabled(shouldEnable));
	}

	public static QuestionViewBase<?> qvFor(Composite parent, Question<?> question, Supplier<Document> documentSupplier,
			Consumer<Question<?>> onQuestionChanged) {
		if (question instanceof FreeTextQuestion)
			return new FreeTextQuestionView(parent, (FreeTextQuestion) question, documentSupplier, onQuestionChanged);
		if (question instanceof SingleChoiceQuestion)
			return new SingleChoiceQuestionView(parent, (SingleChoiceQuestion) question, documentSupplier,
					onQuestionChanged);
		if (question instanceof MultipleChoiceQuestion)
			return new MultipleChoiceQuestionView(parent, (MultipleChoiceQuestion) question, documentSupplier,
					onQuestionChanged);

		throw new IllegalArgumentException("no implementation for " + question.getClass());
	}

	protected String getDocumentKey() {
		Document document = documentSupplier.get();
		if (document == null)
			return null;
		return document.getKey();
	}
}
