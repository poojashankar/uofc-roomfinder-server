package com.uofc.roomfinder.entities;

import java.sql.Timestamp;
import java.util.Date;

public class UserLocation {
	User user;
	Point3D location;
	Timestamp timestamp;

	// getter & setter
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Point3D getLocation() {
		return location;
	}

	public void setLocation(Point3D location) {
		this.location = location;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

}
