package com.uofc.roomfinder.entities.routing;

public class RouteSegment {
	int startPathPoint;
	int endPathPoint;
	String description;
	Gradient gradient;
	double length;

	// constructors
	public RouteSegment(int startPathPoint, int endPathPoint, String description, Gradient gradient, double length) {
		super();
		this.startPathPoint = startPathPoint;
		this.endPathPoint = endPathPoint;
		this.description = description;
		this.gradient = gradient;
		this.length = length;
	}

	// getter & setter
	public String getDescription() {
		return description;
	}

	public double getLength() {
		return length;
	}
	

	public void setLength(double length) {
		this.length = length;
	}
	
	public void addLength(double length) {
		this.length += length;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Gradient getGradient() {
		return gradient;
	}

	public void setGradient(Gradient gradient) {
		this.gradient = gradient;
	}

	public int getStartPathPoint() {
		return startPathPoint;
	}

	public void setStartPathPoint(int startPathPoint) {
		this.startPathPoint = startPathPoint;
	}

	public int getEndPathPoint() {
		return endPathPoint;
	}

	public void setEndPathPoint(int endPathPoint) {
		this.endPathPoint = endPathPoint;
	}

}
