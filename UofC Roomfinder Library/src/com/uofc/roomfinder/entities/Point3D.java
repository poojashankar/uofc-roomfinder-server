package com.uofc.roomfinder.entities;

public class Point3D {
	double x;
	double y;
	double z;

	// constructor
	public Point3D(double x, double y) {
		this(x, y, 0.0);
	}

	// constructor
	public Point3D(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	// getter & setter
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

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public String getJsonUrl() {
		String result;

		String serverUrl = "http://ec2-23-20-196-109.compute-1.amazonaws.com:8080";
		//String serverUrl = "http://192.168.1.106:8080";
		
		result = serverUrl + "/UofC_Roomfinder_Server/rest/annotation/withdata?x=" + this.getX() + "&y=" + this.getY()
				+ "&z=" + this.getZ() + "&text=";

		return result;

	}
}
