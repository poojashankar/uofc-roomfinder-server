package com.uofc.roomfinder.entities.routing;

public class RouteFeature {
	double length;
	String text;
	
	//constructor
	public RouteFeature() {
		super();
		this.length = 0;
		this.text = "";
	}
	
	public RouteFeature(double length, String text) {
		super();
		this.length = length;
		this.text = text;
	}
	
	//getter&setter
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	//TODO: geometry
}
