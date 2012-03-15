package com.uofc.roomfinder.util.gson;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.uofc.roomfinder.entities.Annotation;

/**
 * custom serializer for JSON
 * 
 * this class handles the serialization of an annotation object into json
 *  
 * @author blautens
 *
 */
public class AnnotationJsonSerialzer implements JsonSerializer<Annotation> {

	public static final SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");

	public JsonElement serialize(Annotation src, Type typeOfSrc, JsonSerializationContext context) {

		JsonObject obj = new JsonObject();
		obj.addProperty("id", "" + src.getId());
		obj.addProperty("lat", src.getLatitude());
		obj.addProperty("lng", src.getLongitude());
		obj.addProperty("elevation", src.getElevation());
		obj.addProperty("title", src.getText());
		//obj.addProperty("distance", src.getDistance());
		obj.addProperty("has_detail_page", "" + src.getHas_detail_page());
		obj.addProperty("webpage", src.getWebpage());
		
		//format date
		//obj.addProperty("timestamp", format.format(src.getTimestamp()));

		return obj;
	}
}