package com.uofc.roomfinder.dao;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.uofc.roomfinder.entities.Annotation;
import com.uofc.roomfinder.entities.AnnotationList;
import com.uofc.roomfinder.entities.Building;
import com.uofc.roomfinder.util.ConnectionFactory;
import com.uofc.roomfinder.util.UrlReader;
import com.uofc.roomfinder.util.gson.BuildingListJsonDeserializer;

/*
 import com.esri.arcgis.interop.AutomationException;
 import com.esri.arcgis.server.IServerContext;
 import com.esri.arcgis.server.IServerObjectManager;
 import com.esri.arcgis.server.ServerConnection;
 import com.uofc.roomfinder.entities.Building;
 import com.uofc.roomfinder.util.ConnectionFactory;
 */

/**
 * 
 * @author lauteb
 */
public class BuildingDAOImpl extends GenericDAOImpl<Building, Long> implements BuildingDAO {

	public static final String BUILDING_NAME_COL = "SDE.DBO.Building_Info.BLDG_NAME";
	public static final String BUILDING_ADDRESS_COL = "SDE.DBO.Building_Info.ADDRESS1";
	public static final String BUILDING_USE_COL = "SDE.DBO.Building_Info.BLDG_USE";
	public static final String BUILDING_ID_COL = "SDE.DBO.Building_Footprint.BLD_ID";

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

	/**
	 * loads all buildings out of the ArcGIS MapServer building layer and copies them into the mysql database
	 */
	@Override
	public int updateBuildingTable() {
		String whereClause = "SDE.DBO.Building_Info.BLDG_ID like 'ICT'"; // TODO: set to %
		String result = getJsonFromArcGisLayer(whereClause);
		// System.out.println(result);

		Type listType = new TypeToken<List<Building>>() {
		}.getType();
		Gson gson = new GsonBuilder().registerTypeAdapter(listType, new BuildingListJsonDeserializer()).serializeNulls().create();
		List<Building> buildingList = gson.fromJson(result, listType);

		for (Building building : buildingList) {
			System.out.println(building.getName() + " - " + building.getName_2() + " - " + building.getAbbreviation());
			this.save(building);
		}

		// tem.out.println("size: " + buildingList.size());
		// System.out.println(buildingList.get(1).getName_2());

		return -1;
	}

	/**
	 * helper method for accessing the REST service of the ArcGIS building MapServer
	 * 
	 * @param whereClause
	 * @return
	 */
	private String getJsonFromArcGisLayer(String whereClause) {

		final String ARCGIS_BUILDING_MAPSERVER = "http://136.159.24.32/ArcGIS/rest/services/Buildings/MapServer/";
		final String ARCGIS_BUILDING_LAYER = "0";
		final String ARCGIS_BUILDING_RETURN_FIELDS = "SDE.DBO.Building_Info.BLDG_NAME,+SDE.DBO.Building_Info.ADDRESS1,+SDE.DBO.Building_Info.BLDG_USE,+ SDE.DBO.Building_Footprint.BLD_ID";

		String returnGeometry = "true";

		// build up URL
		String queryUrl = ARCGIS_BUILDING_MAPSERVER + ARCGIS_BUILDING_LAYER + "/";
		queryUrl += "query?text=&geometry=&geometryType=esriGeometryPoint&inSR=&spatialRel=esriSpatialRelIntersects&relationParam=&objectIds=&time=&returnCountOnly=false&returnIdsOnly=false&returnGeometry="
				+ returnGeometry + "&maxAllowableOffset=&outSR=";
		queryUrl += "&where=" + whereClause;
		queryUrl += "&outFields=" + ARCGIS_BUILDING_RETURN_FIELDS;
		queryUrl += "&f=pjson";

		return UrlReader.readFromURL(queryUrl);
	}

	@Override
	public boolean save(Building building) {

		// if a dataset exists with abbreviation update, else insert an new
		// dataset
		if (this.findByAbbreviation(building.getAbbreviation()) == null) {
			return this.insert(building);
		} else {
			return this.update(building);
		}
	}

	/**
	 * build and execute update statement
	 * 
	 * @param building
	 * @return
	 */
	private boolean update(Building building) {

		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();

			// update sql
			String sql = "UPDATE tbl_buildings SET abbreviation = abbreviation";

			if (building.getName() != null)
				sql += " ,building_name = ?";

			if (building.getName_2() != null)
				sql += " ,building_name2 = ? ";

			if (building.getAddress() != null)
				sql += " ,address = ? ";

			if (building.getUse() != null)
				sql += " ,building_use = ? ";

			if (building.getCenterX() != null)
				sql += " ,centerX = ? ";

			if (building.getCenterY() != null)
				sql += " ,centerY = ? ";

			sql += " WHERE abbreviation = ?";

			// prepare stmt and fill variables
			int i = 1;
			prepStmt = conn.prepareStatement(sql);

			// every field
			if (building.getName() != null)
				prepStmt.setString(i++, building.getName());

			if (building.getName_2() != null)
				prepStmt.setString(i++, building.getName_2());

			if (building.getAddress() != null)
				prepStmt.setString(i++, building.getAddress());

			if (building.getUse() != null)
				prepStmt.setString(i++, building.getUse());

			if (building.getCenterX() != null)
				prepStmt.setString(i++, building.getCenterX());

			if (building.getCenterY() != null)
				prepStmt.setString(i++, building.getCenterY());

			// set where clause
			prepStmt.setString(i++, building.getAbbreviation());

			// exec stmt
			int result = prepStmt.executeUpdate();

			if (result > 0) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(prepStmt);
			DbUtils.closeQuietly(conn);
		}
		return false;
	}

	/**
	 * inserts a new dataset into the database table tbl_buildings
	 * 
	 * @param building
	 * @return
	 */
	private boolean insert(Building building) {

		// building_name in database is a not_null column
		if (building.getName() == null)
			return false;

		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();

			// insert sql
			String sql = "INSERT INTO tbl_buildings (building_name, building_name2, abbreviation, address, building_use, centerX, centerY)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?)";

			// System.out.println(sql);

			prepStmt = conn.prepareStatement(sql);

			// every field
			prepStmt.setString(1, building.getName());
			prepStmt.setString(2, building.getName_2());
			prepStmt.setString(3, building.getAbbreviation());
			prepStmt.setString(4, building.getAddress());
			prepStmt.setString(5, building.getUse());
			prepStmt.setString(6, building.getCenterX());
			prepStmt.setString(7, building.getCenterY());

			// exec stmt
			int result = prepStmt.executeUpdate();

			if (result > 0) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(prepStmt);
			DbUtils.closeQuietly(conn);
		}
		return false;
	}

	/**
	 * deletes the object in the database (searches for abbreviation)
	 */
	@Override
	public boolean delete(Building building) {
		Connection conn = null;
		PreparedStatement prepStmt = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();

			// insert sql
			String sql = "DELETE FROM tbl_buildings WHERE abbreviation = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, building.getAbbreviation());

			// exec stmt
			int result = prepStmt.executeUpdate();

			if (result > 0) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtils.closeQuietly(prepStmt);
			DbUtils.closeQuietly(conn);
		}
		return false;
	}

	/**
	 * returns a single Building by given abbreviation
	 */
	@Override
	public Building findByAbbreviation(String abbreviation) {

		Building building = null;

		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();

			// insert sql
			String sql = "SELECT * FROM tbl_buildings WHERE abbreviation = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, abbreviation);

			// exec stmt
			rs = prepStmt.executeQuery();

			// fill DTO
			if (rs.next()) {
				building = new Building(rs);
				return building;
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(prepStmt);
			DbUtils.closeQuietly(conn);
		}
		return null;

	}

	public AnnotationList getAllBuildingsAsAnnotationList() {
		Annotation annotation = null;
		AnnotationList annotationPackage = new AnnotationList();

		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();
			String sql = "SELECT * FROM tbl_buildings WHERE centerX IS NOT NULL";
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();

			// fill DTO
			while (rs.next()) {
				annotation = new Annotation();
				annotation.setLatitude(rs.getString("CENTERX"));
				annotation.setLongitude(rs.getString("CENTERY"));
				annotation.setText(rs.getString("BUILDING_NAME"));
				annotation.setDistance("0");
				annotation.setElevation("0");
				annotation.setHas_detail_page(0);
				annotation.setWebpage("");
				annotation.setTimestamp(new Date());

				annotationPackage.addAnnotation(annotation);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(prepStmt);
			DbUtils.closeQuietly(conn);
		}

		return annotationPackage;
	}
}
