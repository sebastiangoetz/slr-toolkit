package de.tudresden.slr.questionnaire;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import de.tudresden.slr.questionnaire.model.Question;

public class IQuestionRenderer {
	private Composite parent;
	private Question<?> question;
	protected List<Control> answerControls = new LinkedList<>();
	
	public IQuestionRenderer(Composite parent, Question<?> question) {
		this.question = question;
		this.parent = new Composite(parent, 0);
	}
	
	public void render() {
		parent.setLayout(new RowLayout(SWT.VERTICAL));
		
		Composite header = new Composite(parent, 0);
		header.setLayout(new RowLayout(SWT.HORIZONTAL | SWT.WRAP));
		
		Label l = new Label(header, 0);
		l.setText(question.getQuestionText());
		
		Button b = new Button(header, 0);
		b.setText("Delete");
		
		Composite controls = new Composite(parent, 0);
	}
	
	
}
