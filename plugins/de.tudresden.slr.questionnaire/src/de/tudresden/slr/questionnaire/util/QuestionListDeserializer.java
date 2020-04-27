package de.tudresden.slr.questionnaire.util;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import de.tudresden.slr.questionnaire.model.Question;

public class QuestionListDeserializer implements JsonDeserializer<List<Question<?>>> {

	public static final Type TYPE_TOKEN = new TypeToken<List<Question<?>>>() {
	}.getType();

	public List<Question<?>> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
		List<Question<?>> vals = new LinkedList<Question<?>>();
		System.out.println("using deser");
		if (json.isJsonArray()) {
			for (JsonElement e : json.getAsJsonArray()) {
				vals.add((Question<?>) ctx.deserialize(e, Question.class));
			}
		} else if (json.isJsonObject()) {
			vals.add((Question<?>) ctx.deserialize(json, Question.class));
		} else {
			throw new RuntimeException("Unexpected JSON type: " + json.getClass());
		}
		return vals;
	}
}
