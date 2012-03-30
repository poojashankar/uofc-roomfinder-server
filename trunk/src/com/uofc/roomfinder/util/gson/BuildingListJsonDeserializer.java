package com.uofc.roomfinder.util.gson;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.uofc.roomfinder.dao.BuildingDAOImpl;
import com.uofc.roomfinder.entities.Building;

/**
 * custom deserializer for JSON
 * 
 * this class handles the deserialization of a JSON object into a Java object default there is no correct deserialization for dates
 * 
 * @author blautens
 * 
 */
public class BuildingListJsonDeserializer implements JsonDeserializer<List<Building>> {

	@Override
	public List<Building> deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		JsonObject jsonObj = json.getAsJsonObject();

		List<Building> list = new LinkedList<Building>();
		Building newBuilding = new Building();

		System.out.println(jsonObj.get("features"));

		JsonArray featureArray = jsonObj.get("features").getAsJsonArray();

		// System.out.println(featureArray);

		for (int i = 0; i < featureArray.size(); i++) {

			newBuilding = new Building();

			JsonObject attributes = featureArray.get(i).getAsJsonObject().get("attributes").getAsJsonObject();
			//JsonObject geometry = featureArray.get(i).getAsJsonObject().get("geometry").getAsJsonObject();

			if (attributes.get(BuildingDAOImpl.BUILDING_NAME_COL) != null)
				newBuilding.setName_2(attributes.get(BuildingDAOImpl.BUILDING_NAME_COL).getAsString());

			if (!attributes.get(BuildingDAOImpl.BUILDING_ADDRESS_COL).isJsonNull())
				newBuilding.setAddress(attributes.get(BuildingDAOImpl.BUILDING_ADDRESS_COL).getAsString());

			if (!attributes.get(BuildingDAOImpl.BUILDING_USE_COL).isJsonNull())
				newBuilding.setUse(attributes.get(BuildingDAOImpl.BUILDING_USE_COL).getAsString());

			if (!attributes.get(BuildingDAOImpl.BUILDING_ID_COL).isJsonNull())
				newBuilding.setAbbreviation(attributes.get(BuildingDAOImpl.BUILDING_ID_COL).getAsString());

			// TODO: calculate center X & Y pos

			list.add(newBuilding);
		}

		return list;

	}

}
