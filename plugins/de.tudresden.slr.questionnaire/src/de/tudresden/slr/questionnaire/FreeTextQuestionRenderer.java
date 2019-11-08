package de.tudresden.slr.questionnaire;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import de.tudresden.slr.questionnaire.model.FreeTextQuestion;

public class FreeTextQuestionRenderer extends IQuestionRenderer {

	public FreeTextQuestionRenderer(Composite parent, FreeTextQuestion question) {
		super(parent, question);
	}


}
