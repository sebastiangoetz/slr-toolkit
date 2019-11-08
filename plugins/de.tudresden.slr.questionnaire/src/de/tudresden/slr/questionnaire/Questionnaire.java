package de.tudresden.slr.questionnaire;

import java.util.LinkedList;
import java.util.List;

import de.tudresden.slr.questionnaire.model.Question;

public class Questionnaire {
	
	private String name;
	private List<Question<?>> questions = new LinkedList<>();
	
	public Questionnaire(String name) {
		this.name = name;
	}
	
	public void addQuestion(Question<?> question) {
		questions.add(question);
	}
	
	public List<Question<?>> getQuestions() {
		return questions;
	}
	
	public String getName() {
		return name;
	}	
}
