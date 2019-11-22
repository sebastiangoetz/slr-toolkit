package de.tudresden.slr.questionnaire.model;

import java.util.List;

public class MultipleChoiceQuestion extends ChoiceQuestion<List<String>> {
	@Override
	public void addAnswer(String documentKey, List<String> answer) {
		for (String element : answer)
			if (!choices.contains(element))
				throw new IllegalArgumentException("invalid choice");
		super.addAnswer(documentKey, answer);
	}

	@Override
	public int countOccurrencesOfChoiceInAnswers(String choice) {
		return (int) answers.entrySet().stream()
				.filter(entry -> entry.getValue().contains(choice)).count();
	}
}
