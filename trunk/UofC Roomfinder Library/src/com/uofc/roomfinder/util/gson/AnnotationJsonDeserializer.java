package com.uofc.roomfinder.util.gson;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.uofc.roomfinder.entities.Annotation;

/**
 * custom deserializer for JSON
 * 
 * this class handles the deserialization of a JSON object into a Java object default there is no correct deserialization for dates
 * 
 * @author blautens
 * 
 */
public class AnnotationJsonDeserializer implements JsonDeserializer<Annotation> {

	public static final SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");

	@Override
	public Annotation deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {

		JsonObject jsonObj = json.getAsJsonObject();

		try {
			return new Annotation(jsonObj.get("id").getAsLong(), jsonObj.get("lat").getAsString(), jsonObj.get("lng").getAsString(), jsonObj.get("elevation")
					.getAsString(), jsonObj.get("title").getAsString(), jsonObj.get("distance").getAsString(), jsonObj.get("has_detail_page").getAsInt(),
					jsonObj.get("webpage").getAsString(),

					// deserialize date correct
					(jsonObj.get("timestamp") == null) ? null : format.parse(jsonObj.get("timestamp").getAsString()), jsonObj.get("type").getAsString()

			);
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("exception while parsing date");
		}

		return null;

	}

}
