package com.uofc.roomfinder.entities.routing;

import com.uofc.roomfinder.entities.Point3D;

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
	Point3D geometry;	
	RouteStopAttributes attributes;
	
	//constructor
	public RouteStopFeature(Point3D geometry, RouteStopAttributes attributes) {
		super();
		this.geometry = geometry;
		this.attributes = attributes;
	}

	public Point3D getGeometry() {
		return geometry;
	}

	public RouteStopAttributes getAttributes() {
		return attributes;
	}
	
	
}
