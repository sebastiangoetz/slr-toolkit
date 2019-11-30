package de.tudresden.slr.questionnaire;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

import de.tudresden.slr.questionnaire.model.Question;
import de.tudresden.slr.questionnaire.util.CollectionUtils;
import de.tudresden.slr.questionnaire.util.GsonFactory;

public class Questionnaire {

	private String name;
	private List<Question<?>> questions = new LinkedList<>();

	private transient boolean dirty = false;

	public Questionnaire(String name) {
		this.name = name;
	}

	public void addQuestion(Question<?> question) {
		questions.add(question);
	}

	public List<Question<?>> getQuestions() {
		return Collections.unmodifiableList(questions);
	}
	
	public boolean remove(Question<?> question) {
		return questions.remove(question);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null)
			throw new NullPointerException();
		this.name = name;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public Questionnaire deepCopy() {
		Gson gson = GsonFactory.makeGson();
		String json = gson.toJson(this);
		return gson.fromJson(json, Questionnaire.class);
	}

	public void clearAnswers() {
		questions.forEach(q -> q.clearAnswers());
	}

	public boolean moveUp(Question<?> question) {
		return CollectionUtils.moveUp(questions, question);
	}
	
	public boolean moveDown(Question<?> question) {
		return CollectionUtils.moveDown(questions, question);
	}
}
