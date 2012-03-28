package com.uofc.roomfinder.dao;

import java.util.List;

import com.uofc.roomfinder.entities.Building;

/**
 * 
 * @author lauteb
 */
public interface BuildingDAO {

	public List<Building> findBuildingsByName(String name);

	public int updateBuildingTable();
	
	
}
