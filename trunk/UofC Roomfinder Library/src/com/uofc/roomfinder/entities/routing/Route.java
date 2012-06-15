package com.uofc.roomfinder.entities.routing;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uofc.roomfinder.entities.Point3D;

public class Route {
	private RouteStopList stops;

	private String routeName;
	private double length;
	private List<RouteFeature> routeFeatures;
	private List<RouteSegment> routeSegments;
	private List<Point3D> path;
	private Vector<Integer> waypointIndicesOfPath;

	private int currentWaypoint;
	private RouteSegment currentSegment;

	private int currentSegmentIndex = 0;

	// constructor
	public Route() {
		routeName = "unnamed";
		routeFeatures = new LinkedList<RouteFeature>();
		routeSegments = new LinkedList<RouteSegment>();
		path = new LinkedList<Point3D>();
		stops = new RouteStopList();
		waypointIndicesOfPath = new Vector<Integer>();
		currentWaypoint = 0;
		length = 0.0;
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

	public List<Point3D> getPath() {
		return path;
	}

	public void setPath(List<Point3D> path) {
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

	

}
