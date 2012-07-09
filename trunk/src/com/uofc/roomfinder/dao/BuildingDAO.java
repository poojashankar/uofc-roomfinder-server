package com.uofc.roomfinder.dao;

import java.util.List;

import com.uofc.roomfinder.entities.Building;

/**
 * 
 * @author lauteb
 */
public interface BuildingDAO extends GenericDAO<Building, Long>  {

	public List<Building> findBuildingsByName(String name);

	public Building findByAbbreviation(String abbreviation);
	
}
