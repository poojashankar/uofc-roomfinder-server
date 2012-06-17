package com.uofc.roomfinder.dao;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.uofc.roomfinder.entities.Point3D;
import com.uofc.roomfinder.entities.User;
import com.uofc.roomfinder.entities.UserLocation;
import com.uofc.roomfinder.util.ConnectionFactory;
import com.uofc.roomfinder.util.UrlReader;
import com.uofc.roomfinder.util.gson.BuildingListJsonDeserializer;

/**
 * 
 * @author lauteb
 */
public class UserLocationDAOImpl implements UserLocationDAO {

	@Override
	public UserLocation getLastKnownPosition(String userName) {

		UserLocation userLoc = null;

		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();

			// insert sql
			String sql = "SELECT user, lat, lng, alt, timestamp FROM tbl_user_locations WHERE user = ? ORDER BY timestamp DESC";
			prepStmt = conn.prepareStatement(sql);
			System.out.println(sql + userName);
			prepStmt.setString(1, userName);			

			// exec stmt
			rs = prepStmt.executeQuery();

			// fill DTO
			if (rs.next()) {
				userLoc = new UserLocation();
				userLoc.setLocation(new Point3D(rs.getDouble("LNG"), rs.getDouble("LAT"), rs.getDouble("ALT")));
				userLoc.setTimestamp(rs.getTimestamp("TIMESTAMP"));
				userLoc.setUser(new User(userName));
				return userLoc;
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

	@Override
	public boolean save(UserLocation location) {

		// name should not be null
		if (location.getUser() == null || location.getTimestamp() == null)
			return false;

		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();

			// insert sql
			String sql = "INSERT INTO tbl_user_locations (user, lat, lng, alt, timestamp) VALUES (?,?,?,?,?)";
			prepStmt = conn.prepareStatement(sql);

			// each field
			prepStmt.setString(1, location.getUser().getName());
			prepStmt.setString(2, "" + location.getLocation().getY());
			prepStmt.setString(3, "" + location.getLocation().getX());
			prepStmt.setString(4, "" + location.getLocation().getZ());
			prepStmt.setTimestamp(5, location.getTimestamp());

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
}
