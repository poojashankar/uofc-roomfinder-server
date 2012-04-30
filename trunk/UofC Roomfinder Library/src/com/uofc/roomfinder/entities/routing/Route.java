package com.uofc.roomfinder.entities.routing;

import static com.uofc.roomfinder.util.Constants.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.esri.arcgisws.EsriNAServerLayerType;
import com.esri.arcgisws.NAServerBindingStub;
import com.esri.arcgisws.NAServerLocations;
import com.esri.arcgisws.NAServerPropertySets;
import com.esri.arcgisws.NAServerRouteParams;
import com.esri.arcgisws.NAServerRouteResults;
import com.esri.arcgisws.NAServerSolverParams;
import com.esri.arcgisws.NAStreetDirection;
import com.esri.arcgisws.Point;
import com.esri.arcgisws.PointN;
import com.esri.arcgisws.PolylineN;
import com.esri.arcgisws.PropertySet;
import com.esri.arcgisws.PropertySetProperty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.uofc.roomfinder.util.Constants;
import com.uofc.roomfinder.util.UrlReader;
import com.uofc.roomfinder.util.gson.RouteJsonDeserializer;




public class Route {
	private RouteStopList stops;

	private String routeName;
	private double length;
	private List<RouteFeature> routeFeatures;
	private List<RoutePoint> path;
	private Vector<Integer> waypointIndicesOfPath;
	private Vector<Integer> segmentIndicesOfPath;
	
	
	private int currentWaypoint;
	private int currentSegment;

	// constructor
	public Route() {
		routeName = "unnamed";
		routeFeatures = new LinkedList<RouteFeature>();
		path = new LinkedList<RoutePoint>();
		stops = new RouteStopList();
		waypointIndicesOfPath = new Vector<Integer>();
		segmentIndicesOfPath = new Vector<Integer>();
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

	/**
	 * this method accesses the ArcGIS SOAP web service for routing (NAServer)
	 * 
	 * @param start
	 * @param destination
	 * @param impedanceAttribute
	 * @return
	 */
	public static Route getSoapRoute(RoutePoint start, RoutePoint destination, String impedanceAttribute) {

		Route newRoute = new Route();

		// connect to NA SOAP service
		NAServerBindingStub naServer = new NAServerBindingStub(Constants.NA_SERVER_SOAP_URL);
		String routeLayerName = naServer.getNALayerNames(EsriNAServerLayerType.esriNAServerRouteLayer)[0];

		System.out.println(routeLayerName);
		// System.out.println(naServer.getNALayerNames(EsriNAServerLayerType.esriNAServerRouteLayer).length + "<br>");

		// get default routing params from server
		NAServerSolverParams solverParams = naServer.getSolverParameters(routeLayerName);
		NAServerRouteParams routeParams = new NAServerRouteParams();

		// edit route params
		routeParams = (NAServerRouteParams) solverParams;
		routeParams.setReturnRouteGeometries(true);
		routeParams.setReturnDirections(true);
		routeParams.setImpedanceAttributeName(impedanceAttribute);

		// add stop features
		newRoute.stops.getFeatures().add(new RouteStopFeature(start, new RouteStopAttributes("Start", newRoute.routeName)));
		newRoute.stops.getFeatures().add(new RouteStopFeature(destination, new RouteStopAttributes("Destination", newRoute.routeName)));

		System.out.println(newRoute.stops.getFeatures().size());
		
		// add stops to route params
		routeParams.setStops(newRoute.getStopsAsPropSet());

		// SOLVE route
		NAServerRouteResults routeResult = (NAServerRouteResults) naServer.solve(routeParams);

		// get path geometries
		PolylineN resultLine = (PolylineN) routeResult.getRouteGeometries()[0];

		// iterate all path points
		for (Point point : resultLine.getPathArray()[0].getPointArray()) {
			PointN pointN = (PointN) point;

			newRoute.getPath().add(new RoutePoint(pointN.getX(), pointN.getY(), pointN.getZ()));
			// System.out.println("x: " + pointN.getX() + ", y: " + pointN.getY() + ", z: " + pointN.getZ() + "<br>");
		}

		// get path directions
		// text messages and segment lengths
		NAStreetDirection[] directions = routeResult.getDirections()[0].getDirections();
		double cumulativeLength = 0.0;

		// each directions
		for (NAStreetDirection direction : directions) {
			RouteFeature currentRF = new RouteFeature();

			// each text per direction
			for (int i = 0; i < direction.getStrings().length; i++) {
				if (direction.getStringTypes()[i].toString().equals("esriDSTLength")) {
					// segment length

					// try to convert string to double
					try {
						double tmp = Double.parseDouble(direction.getStrings()[i].split(" ")[0]);
						currentRF.setLength(tmp);
					} catch (NumberFormatException nfe) {
						// do nothing, default is 0
					}
				} else if (direction.getStringTypes()[i].toString().equals("esriDSTGeneral")) {
					// routing text info
					currentRF.setText(direction.getStrings()[i]);
				}
			}

			// add feature to list
			newRoute.routeFeatures.add(currentRF);

			// sum up length
			cumulativeLength += currentRF.length;
		}
		newRoute.length = cumulativeLength;

		return newRoute;
	}

	@SuppressWarnings("deprecation")
	private NAServerLocations getStopsAsPropSet() {
		// add stops
		PropertySet[] propSets = new PropertySet[2];

		// start point
		PropertySetProperty prop11 = new PropertySetProperty("x", stops.features.get(0).geometry.x);// "701192.8861");
		PropertySetProperty prop12 = new PropertySetProperty("y", stops.features.get(0).geometry.y);// "5662659.7696");
		PropertySetProperty prop13 = new PropertySetProperty("z", stops.features.get(0).geometry.z);// "0");

		PropertySetProperty[] propArr1 = { prop11, prop12, prop13 };
		propSets[0] = new PropertySet();
		propSets[0].setPropertyArray(propArr1);

		// end point
		PropertySetProperty prop21 = new PropertySetProperty("x", stops.features.get(1).geometry.x);// "701012.8757");
		PropertySetProperty prop22 = new PropertySetProperty("y", stops.features.get(1).geometry.y);// "5662665.3092");
		PropertySetProperty prop23 = new PropertySetProperty("z", stops.features.get(1).geometry.z);// "16");

		PropertySetProperty[] propArr2 = { prop21, prop22, prop23 };
		propSets[1] = new PropertySet();
		propSets[1].setPropertyArray(propArr2);

		// add props to prop set
		NAServerPropertySets stopsPropSets = new NAServerPropertySets();
		stopsPropSets.setPropertySets(propSets);
		return stopsPropSets;
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
	
	public Vector<Integer> getSegmentIndicesOfPath() {
		return segmentIndicesOfPath;
	}

	public void setSegmentIndicesOfPath(Vector<Integer> segmentIndicesOfPath) {
		this.segmentIndicesOfPath = segmentIndicesOfPath;
	}
	
	public int getCurrentSegment() {
		return currentSegment;
	}

	public void setCurrentSegment(int currentSegment) {
		this.currentSegment = currentSegment;
	}

	public void decreaseCurrentCurrentSegment() {
		if (this.currentSegment > 0) {
			this.currentSegment--;
		}
	}

	public void increaseCurrentCurrentSegment() {
		System.out.println(this.segmentIndicesOfPath.size());
		if (this.currentSegment < this.segmentIndicesOfPath.size() - 2) {
			this.currentSegment++;
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
		
		//http://192.168.1.106:8080

		String urlToRequest = "http://ec2-23-20-196-109.compute-1.amazonaws.com:8080/UofC_Roomfinder_Server/rest/route?x1=" + this.stops.features.get(0).geometry.getX() + "&y1="
				+ this.stops.features.get(0).geometry.getY() + "&z1=" + this.stops.features.get(0).geometry.getZ() + "&x2="
				+ this.stops.features.get(1).geometry.getX() + "&y2=" + this.stops.features.get(1).geometry.getY() + "&z2="
				+ this.stops.features.get(1).geometry.getZ() + "&impedance=" + impedance;
		String result = UrlReader.readFromURL(urlToRequest);

		return result;
	}

}
