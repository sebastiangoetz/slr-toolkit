package de.tudresden.slr.questionnaire.wizard;

@SuppressWarnings("serial")
public class BadUserInputException extends Exception {
	public BadUserInputException(String message) {
		super(message);
	}
}
