package com.uofc.roomfinder.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.dbutils.DbUtils;

import com.uofc.roomfinder.entities.WifiAccessPoint;
import com.uofc.roomfinder.util.ConnectionFactory;

/**
 * 
 * @author lauteb
 */
public class WifiAccessPointDAOImpl implements WifiAccessPointDAO {

	/**
	 * returns a single access point by his mac address
	 */
	@Override
	public WifiAccessPoint findByMacAddress(String macAddress) {

		WifiAccessPoint wifiAP = null;

		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();

			// query sql
			String sql = "SELECT * FROM (";
			sql += " SELECT name, mac1 mac, channel1 channel, power1 power, latitude, longitude, altitude FROM tbl_wifi_access_points";
			sql += " UNION";
			sql += " SELECT name, mac2 mac, channel2 channel, power2 power, latitude, longitude, altitude FROM tbl_wifi_access_points";
			sql += " ) wifi_aps";
			sql += " WHERE mac = ?";

			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, macAddress);

			// exec stmt
			rs = prepStmt.executeQuery();

			// fill DTO
			if (rs.next()) {
				wifiAP = new WifiAccessPoint(rs.getString("NAME"), rs.getString("MAC"), rs.getString("CHANNEL"), rs.getInt("POWER"), rs.getDouble("LATITUDE"),
						rs.getDouble("LONGITUDE"), rs.getDouble("ALTITUDE"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;

		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(prepStmt);
			DbUtils.closeQuietly(conn);
		}

		return wifiAP;
	}
}
