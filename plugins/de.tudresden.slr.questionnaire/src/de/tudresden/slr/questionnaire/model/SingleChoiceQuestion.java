package de.tudresden.slr.questionnaire.model;

public class SingleChoiceQuestion extends ChoiceQuestion<String> {
	@Override
	public void addAnswer(String documentKey, String answer) {
		if (!choices.contains(answer))
			throw new IllegalArgumentException("invalid choice");
		super.addAnswer(documentKey, answer);
	}
}
