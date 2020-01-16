package de.tudresden.slr.questionnaire.util;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class InheritanceDeserializer<T> implements JsonDeserializer<T>, JsonSerializer<T> {
	
	public static final String TYPE_FIELD = "$$ type $$";

	@Override
	public T deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		JsonObject json = arg0.getAsJsonObject();
		String type = json.get(TYPE_FIELD).getAsString();
		try {
			Class<?> klass = Class.forName(type);
			return arg2.deserialize(json, klass);
		} catch (ClassNotFoundException e) {
			throw new JsonParseException(e);
		}
	}
	
	@Override
	public JsonElement serialize(T obj, Type type, JsonSerializationContext context) {
		return context.serialize(obj, obj.getClass());
	}
	
}
