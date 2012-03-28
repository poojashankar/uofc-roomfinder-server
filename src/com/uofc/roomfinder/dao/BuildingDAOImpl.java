package com.uofc.roomfinder.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import com.uofc.roomfinder.entities.Building;
import com.uofc.roomfinder.util.ConnectionFactory;

/**
 * 
 * @author lauteb
 */
public class BuildingDAOImpl implements BuildingDAO {

	@Override
	public List<Building> findBuildingsByName(String name) {
		List<Building> buildings = new LinkedList<Building>();

		// normal search for name
		buildings.addAll(searchDB4Buildings(name));

		// if normal search fails, try modifications
		if (buildings.size() == 0 && name.split(" ").length > 0) {

			// split name and fill with wild cards
			StringBuilder searchStringbuilder = new StringBuilder("%");
			String[] splittedSearchString = name.split(" ");

			for (String singleSearchString : splittedSearchString) {
				searchStringbuilder.append(singleSearchString + "%");
			}
			buildings.addAll(searchDB4Buildings(searchStringbuilder.toString()));
		}

		return buildings;
	}

	/**
	 * @param name
	 * @param buildings
	 */
	private List<Building> searchDB4Buildings(String name) {
		List<Building> buildings = new LinkedList<Building>();

		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();

			String sql = "SELECT * FROM tbl_buildings WHERE building_name like ? or abbreviation like ?";
			prepStmt = conn.prepareStatement(sql);

			prepStmt.setString(1, "%" + name + "%");
			prepStmt.setString(2, "%" + name + "%");

			// exec stmt
			rs = prepStmt.executeQuery();

			// fill DTO
			while (rs.next()) {
				Building building = new Building(rs);
				buildings.add(building);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(prepStmt);
			DbUtils.closeQuietly(conn);
		}

		return buildings;
	}

	@Override
	public int updateBuildingTable() {
		
		
		
		
		
		return -1;
	}
}
