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
import com.uofc.roomfinder.entities.User;
import com.uofc.roomfinder.util.ConnectionFactory;
import com.uofc.roomfinder.util.UrlReader;
import com.uofc.roomfinder.util.gson.BuildingListJsonDeserializer;

/**
 * 
 * @author lauteb
 */
public class UserDAOImpl implements UserDAO {

	@Override
	public User getUser(String userName) {
		User user = null;

		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();

			// insert sql
			String sql = "SELECT * FROM tbl_users WHERE name = ?";
			System.out.println(sql + userName);
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, userName);

			// exec stmt
			rs = prepStmt.executeQuery();

			// fill DTO
			if (rs.next()) {
				System.out.println("sssss: " + rs.getString(1));
				user = new User(rs.getString(1));
				return user;
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
	public boolean addFriend(String userName, String friendName) {

		// name should not be null
		if (userName == null || friendName == null)
			return false;

		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();

			// insert sql
			String sql = "INSERT INTO tbl_users_friends (user_name, friends_name) VALUES (?, ?)";

			prepStmt = conn.prepareStatement(sql);

			// every field
			prepStmt.setString(1, userName);
			prepStmt.setString(2, friendName);

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

	@Override
	public boolean save(User user) {

		// name should not be null
		if (user.getName() == null)
			return false;

		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();

			// insert sql
			String sql = "INSERT INTO tbl_users (name) VALUES (?)";
			prepStmt = conn.prepareStatement(sql);

			// every field
			prepStmt.setString(1, user.getName());

			// exec stmt
			int result = prepStmt.executeUpdate();
			System.out.println("res: " + result + "name: " + user.getName());
			if (result > 0) {
				return true;
			}

		} catch (SQLException e) {
			

		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(prepStmt);
			DbUtils.closeQuietly(conn);
		}
		return false;
	}

	@Override
	public List<User> getFriends(String userName) {

		User user = null;

		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();

			// insert sql
			String sql = "SELECT friends_name FROM tbl_users_friends WHERE user_name = ?";
			prepStmt = conn.prepareStatement(sql);
			System.out.println(sql + userName);
			prepStmt.setString(1, userName);

			// exec stmt
			rs = prepStmt.executeQuery();

			// fill list
			List<User> resultList = new ArrayList<User>();
			while (rs.next()) {
				user = new User(rs.getString(1));
				resultList.add(user);
			}
			return resultList;

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(prepStmt);
			DbUtils.closeQuietly(conn);
		}
		return null;
	}

}
