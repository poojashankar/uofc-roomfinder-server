package com.uofc.roomfinder.entities.routing;

/**
 * class for Route querying
 * 
 * 
 * JSON format has to be like this: 
 * 
 * { 
 * "features"  : [
 * {
 *   "geometry" : {"x" : 700326.68338, "y" : 5662241.3256},
 *   "attributes" : {"Name" : "From", "RouteName" : "Route A"}
 * },
 * {
 *   "geometry" : {"x" : 701586.12106, "y" : 5662819.3331},
 *   "attributes" : {"Name" : "To", "RouteName" : "Route A"}
 * }
 * ]
 * }

 * 
 * 
 * @author benjaminlautenschlaeger
 *
 */
public class RouteStopFeature {
	RoutePoint geometry;	
	RouteStopAttributes attributes;
	
	//constructor
	public RouteStopFeature(RoutePoint geometry, RouteStopAttributes attributes) {
		super();
		this.geometry = geometry;
		this.attributes = attributes;
	}
}
