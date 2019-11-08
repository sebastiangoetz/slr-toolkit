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
}
