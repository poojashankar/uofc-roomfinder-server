package com.uofc.roomfinder.entities.routing;

import java.util.LinkedList;
import java.util.List;

public class RouteStopList {
	List<RouteStopFeature> features;
		
	//constructor
	public RouteStopList(){
		features = new LinkedList<RouteStopFeature>();
	}
	
	//getter&setter
	public List<RouteStopFeature> getFeatures() {
		return features;
	}

	public void setFeatures(List<RouteStopFeature> features) {
		this.features = features;
	}
	
}
