package com.uofc.roomfinder.entities.routing;

import static com.uofc.roomfinder.util.Constants.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uofc.roomfinder.util.UrlReader;
import com.uofc.roomfinder.util.gson.RouteJsonDeserializer;

public class Route {
	private RouteStopList stops;

	private String routeName;
	private double length;
	private List<RouteFeature> routeFeatures;
	private List<RouteSegment> routeSegments;
	private List<RoutePoint> path;
	private Vector<Integer> waypointIndicesOfPath;

	private int currentWaypoint;
	private RouteSegment currentSegment;

	private int currentSegmentIndex = 0;

	// constructor
	public Route() {
		routeName = "unnamed";
		routeFeatures = new LinkedList<RouteFeature>();
		routeSegments = new LinkedList<RouteSegment>();
		path = new LinkedList<RoutePoint>();
		stops = new RouteStopList();
		waypointIndicesOfPath = new Vector<Integer>();
		currentWaypoint = 0;
		length = 0.0;
	}

	public Route(RoutePoint start, RoutePoint destination) {
		this(start, destination, ROUTING_IMPEDANCE_SHORTEST_PATH);
	}

	public Route(RoutePoint start, RoutePoint destination, String impedanceAttribute) {
		this();
		// add stop features
		stops.getFeatures().add(new RouteStopFeature(start, new RouteStopAttributes("Start", this.routeName)));
		stops.getFeatures().add(new RouteStopFeature(destination, new RouteStopAttributes("Destination", this.routeName)));

		System.out.println(start.getX());
		System.out.println(destination.getX());

		// get JSON object from server
		// String jsonString = this.getJsonRouteFromServer(impedanceAttribute);
		String jsonString = this.getJsonRouteFromServer();

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
		System.out.println(this.waypointIndicesOfPath.size());
		if (this.currentWaypoint < this.waypointIndicesOfPath.size() - 2) {
			this.currentWaypoint++;
		}
	}

	public RouteSegment getCurrentSegment() {
		System.out.println(routeSegments.size() + "current seg: " + currentSegment);

		if (currentSegment == null && routeSegments.size() > 0) {
			currentSegment = routeSegments.get(0);
		}

		return currentSegment;
	}

	public void setCurrentSegment(RouteSegment currentSegment) {
		this.currentSegment = currentSegment;

		// TODO; set current segment index
	}

	public void decreaseCurrentCurrentSegment() {
		if (this.currentSegmentIndex > 0) {
			this.currentSegmentIndex--;
		}
		System.out.println(currentSegmentIndex);
		this.currentSegment = routeSegments.get(currentSegmentIndex);
	}

	public void increaseCurrentCurrentSegment() {
		if (this.currentSegmentIndex < this.routeSegments.size() - 1) {
			this.currentSegmentIndex++;
		}
		System.out.println(currentSegmentIndex);

		this.currentSegment = routeSegments.get(currentSegmentIndex);
	}

	public List<RouteSegment> getRouteSegments() {
		return routeSegments;
	}

	public RouteStopList getStops() {
		return stops;
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

		/*
		 * old REST service (ARCGIS ROUTING REST SERVICE) is not able to process 3D coordinates
		 * 
		 * 
		 * String returnFormat = "pjson";
		 * 
		 * String solve_url = "/solve?" + "barriers=&" + "polylineBarriers=&" + "polygonBarriers=&" + "outSR=26911&" + "ignoreInvalidLocations=true&" +
		 * "accumulateAttributeNames=&" + "restrictionAttributeNames=RestrictedPath&" + "attributeParameterValues=&" + "restrictUTurns=esriNFSBAllowBacktrack&"
		 * + "useHierarchy=false&" + "returnDirections=true&" + "returnRoutes=true&" + "returnStops=false&" + "returnBarriers=false&" +
		 * "returnPolylineBarriers=false&" + "returnPolygonBarriers=false&" + "directionsLanguage=en-US&" + "directionsStyleName=NA+Desktop&" +
		 * "outputLines=esriNAOutputLineTrueShape&" + "findBestSequence=false&" + "preserveFirstStop=true&" + "preserveLastStop=true&" + "useTimeWindows=false&"
		 * + "startTime=&" + "outputGeometryPrecision=&" + "outputGeometryPrecisionUnits=esriMeters&" + "directionsTimeAttributeName=IndoorCost&" +
		 * "directionsLengthUnits=esriNAUMeters" + "&f=" + returnFormat + "&impedanceAttributeName=" + impedance;
		 * 
		 * String urlToRequest = Constants.NA_SERVER_URL + solve_url + "&stops=" + this.getStopsAsJsonString();
		 * 
		 * System.out.println(urlToRequest);
		 */

		// http://192.168.1.106:8080

		String urlToRequest = "http://ec2-23-20-196-109.compute-1.amazonaws.com:8080/UofC_Roomfinder_Server/rest/route?x1="
				+ this.stops.features.get(0).geometry.getX() + "&y1=" + this.stops.features.get(0).geometry.getY() + "&z1="
				+ this.stops.features.get(0).geometry.getZ() + "&x2=" + this.stops.features.get(1).geometry.getX() + "&y2="
				+ this.stops.features.get(1).geometry.getY() + "&z2=" + this.stops.features.get(1).geometry.getZ() + "&impedance=" + impedance;
		String result = UrlReader.readFromURL(urlToRequest);

		return result;
	}

}
