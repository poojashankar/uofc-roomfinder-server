package com.uofc.roomfinder.util.gson;

import java.lang.reflect.Type;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.uofc.roomfinder.entities.Point3D;
import com.uofc.roomfinder.entities.routing.Route;
import com.uofc.roomfinder.entities.routing.RouteFeature;

/**
 * custom serializer for JSON
 * 
 * this class handles the serialization of a route object into json
 *  
 * @author blautens
 *
 */
public class RouteJsonSerializer implements JsonSerializer<Route> {


	@Override
	public JsonElement serialize(Route src, Type typeOfSrc, JsonSerializationContext context) {

		JsonObject jsonObject = new JsonObject();
		
		//add directions (only the first direction is needed)
		JsonArray directionArr = new JsonArray();
		
		JsonObject dir1 = new JsonObject();
		dir1.addProperty( "routeName", "unnamed");
		//tmp.addProperty( "routeName", "unnamed");
		
		
		//add all direction features (text and length)
		JsonArray features = new JsonArray();
		JsonObject feature = null;
		
		for (RouteFeature rf : src.getRouteFeatures()){
			feature = new JsonObject();
			
			JsonObject attributes = new JsonObject();
			attributes.addProperty("length", rf.getLength());
			attributes.addProperty("text", rf.getText());
			
			feature.add("attributes", attributes);
			features.add(feature);
		}
		
		
		//add all points to path
		JsonObject routes = new JsonObject();
		JsonArray routeFeatures = new JsonArray();
		JsonObject routeFeature = new JsonObject();
		JsonObject geometry = new JsonObject();
		JsonArray paths = new JsonArray();
		JsonArray path = new JsonArray();
		
		for(Point3D point : src.getPath()){
			JsonArray pointArr = new JsonArray();
			
			JsonPrimitive prim = new JsonPrimitive(point.getX());
			pointArr.add(prim);
			prim = new JsonPrimitive(point.getY());
			pointArr.add(prim);
			prim = new JsonPrimitive(point.getZ());
			pointArr.add(prim);
			
			path.add(pointArr);
		}
		
		paths.add(path);
		geometry.add("paths", paths);
		routeFeature.add("geometry", geometry);
		routeFeatures.add(routeFeature);
		routes.add("features", routeFeatures);		
		dir1.add("features", features);

		directionArr.add(dir1);
		jsonObject.add("directions", directionArr);
		jsonObject.add("routes", routes);
		
		return jsonObject;
	}
}