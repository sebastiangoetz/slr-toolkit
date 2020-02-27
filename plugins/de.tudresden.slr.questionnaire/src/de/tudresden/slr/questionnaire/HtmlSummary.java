package de.tudresden.slr.questionnaire;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;

import de.tudresden.slr.questionnaire.model.ChoiceQuestion;
import de.tudresden.slr.questionnaire.model.FreeTextQuestion;
import de.tudresden.slr.questionnaire.model.Question;

/**
 * summarize a {@link Questionnaire}'s answers into a human readable HTML file
 */
public class HtmlSummary {

    private Questionnaire questionnaire;
    private StringBuilder str;

    public HtmlSummary(Questionnaire questionnaire) {
        super();
        this.questionnaire = questionnaire;
        this.str = new StringBuilder();
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
        str.setLength(0);

        str.append("<html>");
        generateHead();
        generateBody();
        str.append("</html>");
        return str.toString();
    }

    protected void generateHead() {
        str.append("<head>");
        str.append("<title>Questionnaire Summary</title>");
        addCssFile("/reset.css");
        addCssFile("/summary.css");
        str.append("</head>");
    }

    protected void generateBody() {
        str.append("<body>");
        String fullName = questionnaire.getName();
        String baseName = fullName.replace(".questionnaire", "");
        str.append("<h1>Summary: ").append(baseName).append("</h1>");
        str.append("This is a summary of the questionnaire <strong>").append(fullName).append("</strong>.");
        questionnaire.getQuestions().forEach(this::visit);
        str.append("</body>");
    }

    protected void visit(Question<?> question) {
        str.append("<div class='question'>");
        str.append("<h2>").append(question.getQuestionText()).append("</h2>");
        if (question instanceof FreeTextQuestion)
            visit((FreeTextQuestion) question);
        else if (question instanceof ChoiceQuestion)
            visit((ChoiceQuestion<?>) question);
        else
            throw new IllegalArgumentException("not implemented for " + question.getClass());
        str.append("</div>");
    }

    protected void visit(FreeTextQuestion question) {
        boolean notAnswered = true;
        for (Entry<String, String> entry : question.getAnswers().entrySet()) {
            notAnswered = false;
            final String document = entry.getKey();
            final String answer = entry.getValue();
            str.append("<h3>").append(document).append("</h3>");
            str.append("<p>").append(answer).append("</p>");
        }
        if (notAnswered) {
            str.append("<p class='error'>This question hasn't been answered for any documents.</p>");
        }
    }

    protected void visit(ChoiceQuestion<?> question) {
        str.append("<table>");
        str.append("<tr><th>Option</th><th>Count</th></tr>");
        for (String choice : question.getChoices()) {
            str.append("<tr>");
            str.append("<td>").append(choice).append("</td>");
            str.append("<td>").append(question.countOccurrencesOfChoiceInAnswers(choice)).append("</td>");
            str.append("</tr>");
        }
        str.append("</table>");
    }

    protected void addCssFile(String cssName) {
        InputStream stream = null;
        try {
            System.out.println("cssName: " + cssName);
            stream = HtmlSummary.class.getResourceAsStream(cssName);
            System.out.println("stream: " + stream);
            Scanner scanner = new Scanner(stream).useDelimiter("\\A");
            String content = scanner.hasNext() ? scanner.next() : "";
            System.out.println("content:" + content);
            str.append(String.format("<style>%s</style>", content));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                stream.close();
            } catch (Exception e) {
                // no-op
            }
        }
    }

    protected void addCssFile(File file) {
        try {
            String content = Files.lines(file.toPath()).collect(Collectors.joining());
            str.append("<style>").append(content).append("</style>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
