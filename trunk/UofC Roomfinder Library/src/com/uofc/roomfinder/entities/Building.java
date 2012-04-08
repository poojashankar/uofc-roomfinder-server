package com.uofc.roomfinder.entities;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * represents a data set of the table tbl_buildings
 * 
 * @author benjaminlautenschlaeger
 */
public class Building {
	private String name;
	private String abbreviation;

	private String name_2;
	private String address;
	private String use;

	private String centerX;
	private String centerY;
	
	// constructors
	public Building() {

	}

	/**
	 * initialize attributes with a resultset
	 * 
	 * @param rs
	 * @throws SQLException
	 */
	public Building(ResultSet rs) throws SQLException {
		this();

		this.name = rs.getString("BUILDING_NAME");
		this.abbreviation = rs.getString("ABBREVIATION");
	}

	// getter&setter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getName_2() {
		return name_2;
	}

	public void setName_2(String name_2) {
		this.name_2 = name_2;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}
	
	public String getCenterX() {
		return centerX;
	}

	public void setCenterX(String centerX) {
		this.centerX = centerX;
	}

	public String getCenterY() {
		return centerY;
	}

	public void setCenterY(String centerY) {
		this.centerY = centerY;
	}

	public void getGeometry() {
		// TODO: implement
	}
}
