package com.uofc.roomfinder.util.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.uofc.roomfinder.entities.routing.Route;
import com.uofc.roomfinder.entities.routing.RouteFeature;
import com.uofc.roomfinder.entities.routing.RoutePoint;

/**
 * custom deserializer for JSON Routes
 * 
 * @author blautens
 * 
 */
public class RouteJsonDeserializer implements JsonDeserializer<Route> {

	@Override
	public Route deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		JsonObject jsonObj = json.getAsJsonObject();

		Route newRoute = new Route();

		try {
			JsonObject directions = jsonObj.get("directions").getAsJsonArray().get(0).getAsJsonObject();
			JsonArray featureArr = directions.get("features").getAsJsonArray();
			newRoute.setRouteName(directions.get("routeName").getAsString());

			JsonObject attributes;

			// iterate each feature
			for (JsonElement feature : featureArr) {
				attributes = feature.getAsJsonObject().get("attributes").getAsJsonObject();
				newRoute.getRouteFeatures().add(new RouteFeature(attributes.get("length").getAsDouble(), attributes.get("text").getAsString()));
			}

			JsonArray pathPointArr = jsonObj.get("routes").getAsJsonObject().get("features").getAsJsonArray().get(0).getAsJsonObject().get("geometry")
					.getAsJsonObject().get("paths").getAsJsonArray().get(0).getAsJsonArray();

			// iterate each path points
			for (JsonElement point : pathPointArr) {

				// round z-coordinate to 2 digits
				double zCoordinate = point.getAsJsonArray().get(2).getAsDouble();
				long tmpZ = (int) Math.round(zCoordinate * 100); // truncates
				zCoordinate = tmpZ / 100.0;

				newRoute.getPath().add(new RoutePoint(point.getAsJsonArray().get(0).getAsDouble(), point.getAsJsonArray().get(1).getAsDouble(), zCoordinate));
			}
		} catch (Exception ex) {
			return newRoute;
		}
		return newRoute;
	}
}
