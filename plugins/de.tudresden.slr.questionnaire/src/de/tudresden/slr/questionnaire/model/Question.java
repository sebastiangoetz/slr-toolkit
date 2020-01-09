package de.tudresden.slr.questionnaire.model;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

import de.tudresden.slr.questionnaire.util.InheritanceDeserializer;

public abstract class Question<E> {

	@SerializedName(InheritanceDeserializer.TYPE_FIELD)
	private String type;

	private String questionText;
	protected Map<String, E> answers = new HashMap<>();

	public Question() {
		type = getClass().getName();
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public void addAnswer(String documentKey, E answer) {
		if (documentKey == null) throw new NullPointerException("documentKey must not be null");
		if (answer == null) throw new NullPointerException("answer must not be null");
		answers.put(documentKey, answer);
	}

	public void removeAnswer(String documentKey) {
		answers.remove(documentKey);
	}

	public E getAnswer(String documentKey) {
		if (documentKey == null)
			return null;
		return answers.get(documentKey);
	}

	public void clearAnswers() {
		answers.clear();
	}

	public boolean hasAnswerFor(String key) {
		return getAnswer(key) != null;
	}
	
	public Map<String, E> getAnswers() {
		return answers;
	}

}
