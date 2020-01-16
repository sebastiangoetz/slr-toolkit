package de.tudresden.slr.questionnaire.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.tudresden.slr.questionnaire.model.Question;

public final class GsonFactory {
	public static Gson makeGson() {
		return new GsonBuilder().setPrettyPrinting()
//				.registerTypeAdapter(QuestionListDeserializer.TYPE_TOKEN, new QuestionListDeserializer())
				.registerTypeAdapter(Question.class, new InheritanceDeserializer<Question<?>>())
				.create();
	}
}
