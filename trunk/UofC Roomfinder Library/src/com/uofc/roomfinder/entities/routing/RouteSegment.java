package com.uofc.roomfinder.entities.routing;

public class RouteSegment {
	int startPathPoint;
	int endPathPoint;
	String description;
	Gradient gradient;

	
	//constructors
	public RouteSegment(int startPathPoint, int endPathPoint, String description, Gradient gradient) {
		super();
		this.startPathPoint = startPathPoint;
		this.endPathPoint = endPathPoint;
		this.description = description;
		this.gradient = gradient;
	}

	// getter & setter
	public String getDescription() {
		return description;
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
