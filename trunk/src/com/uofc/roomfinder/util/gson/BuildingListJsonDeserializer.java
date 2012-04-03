package com.uofc.roomfinder.util.gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
//import com.esri.arcgis.geometry.*;
//import com.esri.arcgis.geometry.ISpatialReference;
//import com.esri.arcgis.system.*;
import org.codehaus.jettison.json.JSONArray;

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
 * this class handles the deserialization of a JSON object into a Java object
 * default there is no correct deserialization for dates
 * 
 * @author blautens
 * 
 */
public class BuildingListJsonDeserializer implements
		JsonDeserializer<List<Building>> {

	@Override
	public List<Building> deserialize(JsonElement json, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		JsonObject jsonObj = json.getAsJsonObject();

		List<Building> list = new LinkedList<Building>();
		Building newBuilding = new Building();

		System.out.println(jsonObj.get("features"));

		JsonArray featureArray = jsonObj.get("features").getAsJsonArray();

		System.out.println(featureArray);

		for (int i = 0; i < featureArray.size(); i++) {

			newBuilding = new Building();

			JsonObject attributes = featureArray.get(i).getAsJsonObject()
					.get("attributes").getAsJsonObject();

			if (attributes.get(BuildingDAOImpl.BUILDING_NAME_COL) != null)
				newBuilding.setName_2(attributes.get(
						BuildingDAOImpl.BUILDING_NAME_COL).getAsString());

			if (!attributes.get(BuildingDAOImpl.BUILDING_ADDRESS_COL)
					.isJsonNull())
				newBuilding.setAddress(attributes.get(
						BuildingDAOImpl.BUILDING_ADDRESS_COL).getAsString());

			if (!attributes.get(BuildingDAOImpl.BUILDING_USE_COL).isJsonNull())
				newBuilding.setUse(attributes.get(
						BuildingDAOImpl.BUILDING_USE_COL).getAsString());

			if (!attributes.get(BuildingDAOImpl.BUILDING_ID_COL).isJsonNull())
				newBuilding.setAbbreviation(attributes.get(
						BuildingDAOImpl.BUILDING_ID_COL).getAsString());

			// TODO: calculate center X & Y pos
			JsonObject geometry = featureArray.get(i).getAsJsonObject()
					.get("geometry").getAsJsonObject();
			JsonArray ringArr = geometry.get("rings").getAsJsonArray();
			JsonArray geometryElements = ringArr.get(0).getAsJsonArray();
			System.out.println(geometryElements);

			double lat = 0;
			double lng = 0;
//			Envelope env;
			
			//TODO: geographische Mitte berechnen
			lat = geometryElements.get(0).getAsJsonArray().get(0)
					.getAsDouble();
			lng = geometryElements.get(0).getAsJsonArray().get(1)
					.getAsDouble();

			
			
		      // Create a point with Geographic coordinates...
//		      Point point;
//			try {
//				point = new Point();
//			
//		      point.putCoords(-100.0, 40.0);
//
//		      System.out.println("Original coordinates: " + point.getX() + "," + point.getY());
//
//		      // Create the SpatialReferenceEnvironment...
//		      SpatialReferenceEnvironment spatialReferenceEnvironment = new SpatialReferenceEnvironment();
//
//		      // Apply the initial spatial reference...
//		      ISpatialReference geographicCoordinateSystem = spatialReferenceEnvironment
//		          .createGeographicCoordinateSystem(esriSRGeoCSType.esriSRGeoCS_NAD1927);
//		      point.setSpatialReferenceByRef(geographicCoordinateSystem);
//
//		      // Create the output projected coordinate system...
//		      ISpatialReference projectedCoordinateSystem = spatialReferenceEnvironment
//		          .createProjectedCoordinateSystem(esriSRProjCSType.esriSRProjCS_NAD1983UTM_13N);
//
//		      // Create the GeoTransformation...
//		      IGeoTransformation iGeoTransformation = (IGeoTransformation) spatialReferenceEnvironment
//		          .createGeoTransformation(esriSRGeoTransformationType.esriSRGeoTransformation_NAD1927_To_WGS1984_5);
//
//		      // Project the point...
//		      point.projectEx(projectedCoordinateSystem, esriTransformDirection.esriTransformForward, iGeoTransformation,
//		          false, 0.0, 0.0);
//		      System.out.println("Projected coordinates: " + point.getX() + " , " + point.getY());
//			
//			
//			} catch (UnknownHostException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
			
			
			
			
			
			newBuilding.setCenterX(String.valueOf(lat));
			newBuilding.setCenterY(String.valueOf(lng));
			
			
			
			
			
			// iterate over all points, add them to one envelope and get center
			// position
			for (int j = 0; j < geometryElements.size(); j++) {
				

//				com.esri.arcgis.geometry.Point point;
//				try {
//					Point[] wksPoints = new Point[10];
//				    for (int i = 0; i < wksPoints.length; i++){
//				        wksPoints[i].x = (xFactor + i);
//				        wksPoints[i].y = (yFactor + i);
//				    }
//					point = new com.esri.arcgis.geometry.;
//
//					point.setX(lat);
//					point.setY(lng);
//					point.setZ(0.0);
//
//					com.esri.arcgis.geometry.Polygon poly = new com.esri.arcgis.geometry.Polygon();
//
//					poly.addPoints(j, point);
//					env = (Envelope) poly.getEnvelope();
//
//					System.out.println(env.getCentroid().getX() + " - "
//							+ env.getCentroid().getY());
//
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//				System.out.println(geometryElements.get(j).getAsJsonArray()
//						.get(0));
			}

			list.add(newBuilding);
		}

		return list;

	}

}
