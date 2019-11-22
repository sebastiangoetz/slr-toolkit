package de.tudresden.slr.questionnaire;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map.Entry;

import de.tudresden.slr.questionnaire.model.ChoiceQuestion;
import de.tudresden.slr.questionnaire.model.FreeTextQuestion;
import de.tudresden.slr.questionnaire.model.Question;

/**
 * summarize a {@link Questionnaire}'s answers into a human readable HTML file
 */
public class HtmlSummary {

	private Questionnaire questionnaire;

	public HtmlSummary(Questionnaire questionnaire) {
		super();
		this.questionnaire = questionnaire;
	}
	
	public void showSummary() {
		try {
			File file = File.createTempFile("questionnaire-summary-", ".html");
			file.deleteOnExit();
			
			PrintWriter pw = new PrintWriter(file);
			pw.write(generateSummary());
			pw.close();
			
			Desktop.getDesktop().browse(file.toURI());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String generateSummary() {
		StringBuilder str = new StringBuilder();
		str.append("<html><body>");
		questionnaire.getQuestions().forEach(q -> visit(str, q));
		str.append("</body></html>");
		return str.toString();
	}

	private void visit(StringBuilder stringBuilder, Question<?> question) {
		stringBuilder.append("<h1>").append(question.getQuestionText())
				.append("</h1>");

		if (question instanceof FreeTextQuestion)
			visit(stringBuilder, (FreeTextQuestion) question);
		else if (question instanceof ChoiceQuestion)
			visit(stringBuilder, (ChoiceQuestion<?>) question);
		else
			throw new IllegalArgumentException(
					"not implemented for " + question.getClass());
	}

	private void visit(StringBuilder str, FreeTextQuestion question) {
		for (Entry<String, String> entry : question.getAnswers().entrySet()) {
			final String document = entry.getKey();
			final String answer = entry.getValue();
			str.append("<h2>").append(document).append("</h2>");
			str.append("<p>").append(answer).append("</p>");
		}
	}

	private void visit(StringBuilder str, ChoiceQuestion<?> question) {
		str.append("<table>");
		str.append("<tr><th>Choice</th><th>Count</th></tr>");
		for (String choice : question.getChoices()) {
			str.append("<tr>");
			str.append("<td>").append(choice).append("</td>");
			str.append("<td>")
					.append(question.countOccurrencesOfChoiceInAnswers(choice))
					.append("</td>");
			str.append("</tr>");
		}
		str.append("</table>");
	}
}
