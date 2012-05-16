import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GisServerUtil {

	public static Point getCenterCoordinateOfRoom(String buildingRoomString) {

		String[] buildingRommArr = getBuildingAndRoomOutOfString(buildingRoomString);

		if (buildingRommArr.length == 2) {
			String json = getJsonFromArcGisLayer(buildingRommArr[0], buildingRommArr[1]);
			Point center = getCoordFromJson(json);
			return center;
		}
		return null;

	}

	private static Point getCoordFromJson(String json) {

		JsonParser parser = new JsonParser();
		JsonObject jsonObj = (JsonObject) parser.parse(json);
		JsonArray featureArray = jsonObj.get("features").getAsJsonArray();

		double xSum = 0;
		double ySum = 0;
		double z = 0;

		for (int i = 0; i < featureArray.size(); i++) {
			JsonObject attributes = featureArray.get(i).getAsJsonObject().get("attributes").getAsJsonObject();
			JsonObject geometry = featureArray.get(i).getAsJsonObject().get("geometry").getAsJsonObject();
			JsonArray ringArr = geometry.get("rings").getAsJsonArray();
			JsonArray geometryElements = ringArr.get(0).getAsJsonArray();

			if (attributes.get("SDE.DBO.Building_Room.FLR_ID") != null) {
				z = getZCoordFromFloor(attributes.get("SDE.DBO.Building_Room.FLR_ID").getAsString());
			}

			// iterate over all points, add them to one envelope and get center
			for (int j = 0; j < geometryElements.size(); j++) {

				double tmpx = geometryElements.get(j).getAsJsonArray().get(0).getAsDouble();
				double tmpy = geometryElements.get(j).getAsJsonArray().get(1).getAsDouble();

				xSum += tmpx;
				ySum += tmpy;
			}

			xSum = xSum / geometryElements.size();
			ySum = ySum / geometryElements.size();
		}

		if (xSum == 0)
			return null;

		return new Point(xSum, ySum, z);

	}

	private static String[] getBuildingAndRoomOutOfString(String buildingRoomString) {

		String[] buildingRoomArr = buildingRoomString.split("_");
		// String[] returnArray;

		if (buildingRoomArr.length > 1) {
			String[] returnArray = { buildingRoomArr[0], buildingRoomArr[1] };
			return returnArray;
		}

		buildingRoomArr = buildingRoomString.split("-");

		if (buildingRoomArr.length > 1) {
			String[] returnArray = { buildingRoomArr[0], buildingRoomArr[1] };
			return returnArray;
		}

		buildingRoomArr = buildingRoomString.split(" ");

		if (buildingRoomArr.length > 1) {
			String[] returnArray = { buildingRoomArr[0], buildingRoomArr[1] };
			return returnArray;
		}

		String[] returnArray = { "" };
		return returnArray;
	}

	private static String getJsonFromArcGisLayer(String building, String room) {

		final String ARCGIS_ROOMS_MAPSERVER = "http://136.159.24.32/ArcGIS/rest/services/Rooms/Rooms/MapServer/";
		final String ARCGIS_QUERY_LAYER = "111";
		final String ARCGIS_ROOMS_RETURN_FIELDS = "SDE.DBO.Building_Room.FLR_ID";

		final String RETURN_GEOMETRY = "true";
		final String OUTPUT_SPATIAL_REF = "4326";

		String whereClause = "SDE.DBO.Building_Room.RM_ID='" + room + "' AND SDE.DBO.Building_Room.BLD_ID='" + building + "'";

		// build up URL
		String queryUrl = ARCGIS_ROOMS_MAPSERVER + ARCGIS_QUERY_LAYER + "/";
		queryUrl += "query?text=&geometry=&geometryType=esriGeometryPoint&inSR=&spatialRel=esriSpatialRelIntersects&relationParam=&objectIds=&time=&returnCountOnly=false&returnIdsOnly=false&returnGeometry="
				+ RETURN_GEOMETRY + "&maxAllowableOffset=" + "&outSR=" + OUTPUT_SPATIAL_REF;
		queryUrl += "&where=" + whereClause;
		queryUrl += "&outFields=" + ARCGIS_ROOMS_RETURN_FIELDS;
		queryUrl += "&f=pjson";

		return UrlReader.readFromURL(queryUrl);
	}

	/**
	 * determine the z-coordinate with the help of the floor_id assume each floor has the height of 4m (same assumption was made when designing the network data
	 * sets)
	 * 
	 * @param floor
	 * @return z-coordinate
	 */
	public static double getZCoordFromFloor(String floor) {

		int numFloor;

		// if the string is not a number follow the rules to determine z-coordinate
		if (!isNumeric(floor)) {

			if (floor.equals("B1")) {
				numFloor = -1;
			} else if (floor.equals("B2")) {
				numFloor = -2;
			} else if (floor.equals("P1")) {
				numFloor = 14; // probably wrong
			} else if (floor.equals("P2")) {
				numFloor = 14; // probably wrong
			} else if (floor.equals("M1")) {
				numFloor = 1;
			} else if (floor.equals("G1")) {
				numFloor = -1;
			} else if (floor.equals("G2")) {
				numFloor = -2;
			} else {
				numFloor = 1;
			}
		}
		// if it's a number convert it to int
		else {
			numFloor = Integer.parseInt(floor) - 1;
		}
		return numFloor * 4;
	}

	/**
	 * checks whether a string is numeric or not
	 * 
	 * @param str
	 *            potential number
	 * @return
	 */
	public static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

}
