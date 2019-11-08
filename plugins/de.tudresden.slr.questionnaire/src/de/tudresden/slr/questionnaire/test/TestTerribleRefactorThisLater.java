package de.tudresden.slr.questionnaire.test;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.tudresden.slr.questionnaire.Questionnaire;
import de.tudresden.slr.questionnaire.model.FreeTextQuestion;
import de.tudresden.slr.questionnaire.model.MultipleChoiceQuestion;
import de.tudresden.slr.questionnaire.model.Question;
import de.tudresden.slr.questionnaire.model.SingleChoiceQuestion;
import de.tudresden.slr.questionnaire.util.GsonFactory;

// TODO turn this into an actual unit test ...
public class TestTerribleRefactorThisLater {
	public static void main(String[] args) {
		Gson gson = GsonFactory.makeGson();
		
		Question<?> q1 = testFreeText(gson);
		Question<?> q2 = testSingleChoice(gson);
		Question<?> q3 = testMultipleChoice(gson);
		
		List<Question<?>> questions = new LinkedList<>();
		questions.add(q1);
		questions.add(q2);
		questions.add(q3);
		testList(gson, questions);
		

		System.out.println("no exceptions thrown");
	}

	private static Question<?> testFreeText(Gson gson) {
		FreeTextQuestion question = new FreeTextQuestion();
		question.setQuestionText("Is this a free text question?");
		question.addAnswer("hspn", "great paper 5/7");

		String s1 = gson.toJson(question);
		Question<?> reconstructed = gson.fromJson(s1, Question.class);
		String s2 = gson.toJson(reconstructed);

		if (!s1.equals(s2))
			throw new RuntimeException("bad implementation");

		return question;
	}

	private static Question testSingleChoice(Gson gson) {
		SingleChoiceQuestion question = new SingleChoiceQuestion();
		question.setQuestionText("Is this a yes or no question?");
		question.addChoice("yes");
		question.addChoice("no");
		question.addAnswer("hspn", "yes");

		String s1 = gson.toJson((Question) question);
		SingleChoiceQuestion reconstructed = (SingleChoiceQuestion) gson.fromJson(s1, Question.class);
		String s2 = gson.toJson(reconstructed);

		if (!s1.equals(s2))
			throw new RuntimeException("bad implementation");
		
		if (!reconstructed.getChoices().equals(question.getChoices()))
			throw new RuntimeException("bad implementation");

		return question;
	}

	private static Question testMultipleChoice(Gson gson) {
		List<String> choices = new LinkedList<>();
		choices.add("yes");
		choices.add("definitely");

		MultipleChoiceQuestion question = new MultipleChoiceQuestion();
		question.setQuestionText("Can I select all of the choices?");
		question.addChoice("yes");
		question.addChoice("definitely");
		question.addAnswer("hspn", choices);

		String s1 = gson.toJson(question);
		String s2 = gson.toJson(gson.fromJson(s1, Question.class));

		if (!s1.equals(s2))
			throw new RuntimeException("bad implementation");

		return question;
	}
	
	private static void testList(Gson gson, List<Question<?>> questions) {
		Questionnaire q = new Questionnaire("asdf");
		questions.forEach(q::addQuestion);
		String s1 = gson.toJson(q);
		System.out.println(s1);
	}
}
