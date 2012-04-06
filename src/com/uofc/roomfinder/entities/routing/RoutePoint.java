package com.uofc.roomfinder.entities.routing;

public class RoutePoint {
	double x;
	double y;
	
	//constructor
	public RoutePoint(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	//getter & setter
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
	
}
