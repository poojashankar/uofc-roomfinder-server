package com.uofc.roomfinder.entities.routing;

import static com.uofc.roomfinder.util.Constants.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

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
	Vector<Integer> waypointIndicesOfPath;
	int currentWaypoint;

	// constructor
	public Route() {
		routeName = "unnamed";
		routeFeatures = new LinkedList<RouteFeature>();
		path = new LinkedList<RoutePoint>();
		stops = new RouteStopList();
		waypointIndicesOfPath = new Vector<Integer>();
		currentWaypoint = 0;
	}

	public Route(RoutePoint start, RoutePoint destination) {
		this(start, destination, ROUTING_IMPEDANCE_SHORTEST_PATH);
	}

	public Route(RoutePoint start, RoutePoint destination, String impedanceAttribute) {
		this();
		// add stop features
		stops.getFeatures().add(new RouteStopFeature(start, new RouteStopAttributes("Start", this.routeName)));
		stops.getFeatures().add(new RouteStopFeature(destination, new RouteStopAttributes("Destination", this.routeName)));

		// get JSON object from server
		String jsonString = this.getJsonRouteFromServer(impedanceAttribute);

		// deserialze JSON String
		Gson gson = new GsonBuilder().registerTypeAdapter(Route.class, new RouteJsonDeserializer()).serializeNulls().create();
		Route newRoute = gson.fromJson(jsonString, Route.class);

		this.path = newRoute.getPath();
		this.routeFeatures = newRoute.getRouteFeatures();
		this.routeName = newRoute.getRouteName();
		this.length = newRoute.getLength();
		this.currentWaypoint = 0;
	}

	// getter & setter
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

	public Vector<Integer> getWaypointIndicesOfPath() {
		return waypointIndicesOfPath;
	}

	public void setWaypointIndicesOfPath(Vector<Integer> waypointIndicesOfPath) {
		this.waypointIndicesOfPath = waypointIndicesOfPath;
	}

	public int getCurrentWaypoint() {
		return currentWaypoint;
	}

	public void setCurrentWaypoint(int currentWaypoint) {
		this.currentWaypoint = currentWaypoint;
	}

	public void decreaseCurrentWaypoint() {
		if (this.currentWaypoint > 0) {
			this.currentWaypoint--;
		}
	}

	public void increaseCurrentWaypoint() {
		System.out.println(this.getWaypointIndicesOfPath().size());
		if (this.currentWaypoint < this.getWaypointIndicesOfPath().size() - 2) {
			this.currentWaypoint++;
		}
	}

	/**
	 * returns route from NAServer in JSON format
	 * 
	 * uses 'LengthCost' -> shortest way as default impedance!
	 * 
	 * @return JSON route
	 */
	private String getJsonRouteFromServer() {
		return getJsonRouteFromServer("Length");
	}

	private String getJsonRouteFromServer(String impedance) {
		String returnFormat = "pjson";

		String solve_url = "/solve?" + "barriers=&" + "polylineBarriers=&" + "polygonBarriers=&" + "outSR=26911&" + "ignoreInvalidLocations=true&"
				+ "accumulateAttributeNames=&" + "restrictionAttributeNames=RestrictedPath&" + "attributeParameterValues=&"
				+ "restrictUTurns=esriNFSBAllowBacktrack&" + "useHierarchy=false&" + "returnDirections=true&" + "returnRoutes=true&" + "returnStops=false&"
				+ "returnBarriers=false&" + "returnPolylineBarriers=false&" + "returnPolygonBarriers=false&" + "directionsLanguage=en-US&"
				+ "directionsStyleName=NA+Desktop&" + "outputLines=esriNAOutputLineTrueShape&" + "findBestSequence=false&" + "preserveFirstStop=true&"
				+ "preserveLastStop=true&" + "useTimeWindows=false&" + "startTime=&" + "outputGeometryPrecision=&" + "outputGeometryPrecisionUnits=esriMeters&"
				+ "directionsTimeAttributeName=IndoorCost&" + "directionsLengthUnits=esriNAUMeters" + "&f=" + returnFormat + "&impedanceAttributeName="
				+ impedance;

		String urlToRequest = Constants.NA_SERVER_URL + solve_url + "&stops=" + this.getStopsAsJsonString();
		
		System.out.println(urlToRequest);
		
		String result = UrlReader.readFromURL(urlToRequest);

		return result;
	}

}
