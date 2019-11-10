package de.tudresden.slr.questionnaire.questionview;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.questionnaire.model.FreeTextQuestion;
import de.tudresden.slr.questionnaire.model.Question;

public class FreeTextQuestionView extends QuestionViewBase<FreeTextQuestion> {

	public FreeTextQuestionView(Composite parent, FreeTextQuestion question, Supplier<Document> documentSupplier,
			Consumer<Question<?>> onQuestionChanged) {
		super(parent, question, documentSupplier, onQuestionChanged);
	}

	@Override
	protected List<Control> renderControls() {
		List<Control> controls = new LinkedList<>();

		Text text = new Text(root, SWT.MULTI | SWT.WRAP | SWT.BORDER);
		try {
			text.setText(question.getAnswer(getDocumentKey()));
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
				question.addAnswer(getDocumentKey(), text.getText());
				onQuestionChanged.accept(question);
			}
		});

		controls.add(text);
		return controls;
	}

}
