package com.uofc.roomfinder.entities;

public class WifiAccessPoint {

	String name;
	String macAddress; // primary key in table
	String channel;
	int outgoingPowerLevel;
	double latitude;
	double longitude;
	double altitude;

	// not out of the db, set with current value
	int currentPowerLevel;

	// constructor
	public WifiAccessPoint() {
		super();
	}

	public WifiAccessPoint(String name, String macAddress, String channel, int outgoingPowerLevel) {
		this();
		this.name = name;
		this.macAddress = macAddress;
		this.channel = channel;
		this.outgoingPowerLevel = outgoingPowerLevel;
	}

	public WifiAccessPoint(String name, String macAddress, String channel, int outgoingPowerLevel, double latitude, double longitude, double altitude) {
		this();
		this.name = name;
		this.macAddress = macAddress;
		this.channel = channel;
		this.outgoingPowerLevel = outgoingPowerLevel;
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
	}

	// getter & setter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public int getOutgoingPowerLevel() {
		return outgoingPowerLevel;
	}

	public void setOutgoingPowerLevel(int outgoingPowerLevel) {
		this.outgoingPowerLevel = outgoingPowerLevel;
	}

	public int getCurrentPowerLevel() {
		return currentPowerLevel;
	}

	public void setCurrentPowerLevel(int currentPowerLevel) {
		this.currentPowerLevel = currentPowerLevel;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

}
