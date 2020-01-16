package de.tudresden.slr.questionnaire.model;

import java.util.LinkedList;
import java.util.List;

public abstract class ChoiceQuestion<E> extends Question<E> {
	protected List<String> choices = new LinkedList<>();

	public ChoiceQuestion() {
		super();
	}

	public void addChoice(String choice) {
		if (choices.contains(choice))
			throw new IllegalArgumentException("duplicate choice");
		choices.add(choice);
	}

	public void removeChoice(String choice) {
		choices.remove(choice);
	}

	public List<String> getChoices() {
		return choices;
	}
	
	public abstract int countOccurrencesOfChoiceInAnswers(String choice);
}
