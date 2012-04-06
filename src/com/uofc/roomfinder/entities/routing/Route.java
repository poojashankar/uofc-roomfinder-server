package com.uofc.roomfinder.entities.routing;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uofc.roomfinder.util.Constants;
import com.uofc.roomfinder.util.UrlReader;
import com.uofc.roomfinder.util.gson.RouteJsonDeserializer;

public class Route {
	RouteStopList stops;
	
	String routeName;
	double length;
	List<RouteFeature> routeFeatures;
	List<RoutePoint> path;
		
	//constructor
	public Route(){
		routeName = "unnamed";
		routeFeatures = new LinkedList<RouteFeature>();
		path = new LinkedList<RoutePoint>();	
		stops = new RouteStopList();
	}
	
	public Route(RoutePoint start, RoutePoint destination){
		this();
		//add stop features
		stops.getFeatures().add(new RouteStopFeature(start, new RouteStopAttributes("Start", this.routeName)));
		stops.getFeatures().add(new RouteStopFeature(destination, new RouteStopAttributes("Destination", this.routeName)));
		
		//get JSON object from server
		String jsonString = this.getJsonRouteFromServer();
		
		// deserialze JSON String
		Gson gson = new GsonBuilder().registerTypeAdapter(Route.class, new RouteJsonDeserializer()).serializeNulls().create();
		Route newRoute = gson.fromJson(jsonString, Route.class);
		
		this.path = newRoute.getPath();
		this.routeFeatures = newRoute.getRouteFeatures();
		this.routeName = newRoute.getRouteName();
		this.length = newRoute.getLength();		
	}
	
	//getter & setter
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public List<RouteFeature> getRouteFeatures() {
		return routeFeatures;
	}
	public void setRouteFeatures(List<RouteFeature> routeFeatures) {
		this.routeFeatures = routeFeatures;
	}
	public List<RoutePoint> getPath() {
		return path;
	}
	public void setPath(List<RoutePoint> path) {
		this.path = path;
	}

	public String getStopsAsJsonString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		String json = gson.toJson(stops);

		return json;
	}
	
	private String getJsonRouteFromServer(){
		
		String returnFormat = "pjson";
		String solve_url = "/solve?" +
				"barriers=&" +
				"polylineBarriers=&" +
				"polygonBarriers=&" +
				"outSR=26911&" +
				"ignoreInvalidLocations=true&" +
				"accumulateAttributeNames=&" +
				"impedanceAttributeName=IndoorCost&" +
				"restrictionAttributeNames=RestrictedPath&" +
				"attributeParameterValues=&" +
				"restrictUTurns=esriNFSBAllowBacktrack&" +
				"useHierarchy=false&" +
				"returnDirections=true&" +
				"returnRoutes=true&" +
				"returnStops=false&" +
				"returnBarriers=false&" +
				"returnPolylineBarriers=false&" +
				"returnPolygonBarriers=false&" +
				"directionsLanguage=en-US&" +
				"directionsStyleName=NA+Desktop&" +
				"outputLines=esriNAOutputLineTrueShape&" +
				"findBestSequence=false&" +
				"preserveFirstStop=true&" +
				"preserveLastStop=true&" +
				"useTimeWindows=false&" +
				"startTime=&" +
				"outputGeometryPrecision=&" +
				"outputGeometryPrecisionUnits=esriMeters&" +
				"directionsTimeAttributeName=IndoorCost&" +
				"directionsLengthUnits=esriNAUMeters" +
				"&f=" + returnFormat;
		
		String urlToRequest = Constants.NA_SERVER_URL +  solve_url + "&stops=" + this.getStopsAsJsonString();
		String result = UrlReader.readFromURL(urlToRequest);
		
		return result;
	}
	
	
	
}
