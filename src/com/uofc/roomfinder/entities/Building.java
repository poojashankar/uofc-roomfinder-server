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
}
