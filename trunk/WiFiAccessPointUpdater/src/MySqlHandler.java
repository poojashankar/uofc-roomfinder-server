import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbutils.DbUtils;

/**
 * this class handles all MySQL tasks
 * 
 * @author benjaminlautenschlaeger
 * 
 */
public class MySqlHandler {

	private final static int COL_NAME = 0;
	private final static int COL_BUILDING = 1;
	private final static int COL_MAC1 = 2;
	private final static int COL_CHANNEL1 = 3;
	private final static int COL_POWER1 = 4;
	private final static int COL_MAC2 = 5;
	private final static int COL_CHANNEL2 = 6;
	private final static int COL_POWER2 = 7;

	private final static String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";

	public static int upgradeDataset(Connection connection, String[] cols) {

		Status status = determineUpgradeStatus(cols);
		PreparedStatement prepStmt = null;

		if (status == MySqlHandler.Status.INSERT) {

			// insert sql
			String sql = "INSERT INTO tbl_wifi_access_points (name, folder, mac1, channel1, power1, mac2, channel2, power2)" + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

			// return generated keys -> to retrieve the autoincrement id
			try {
				prepStmt = connection.prepareStatement(sql);

				prepStmt.setString(1, cols[0]);
				prepStmt.setString(2, cols[1]);
				prepStmt.setString(3, cols[2]);
				prepStmt.setString(4, cols[3]);
				prepStmt.setString(5, cols[4]);
				prepStmt.setString(6, cols[5]);
				prepStmt.setString(7, cols[6]);
				prepStmt.setString(8, cols[7]);

				prepStmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
				return 0;
			} finally {
				DbUtils.closeQuietly(prepStmt);
			}
			return 1;
		} else if (status == MySqlHandler.Status.UPDATE) {

			return 1;
		} else {
			return 0;
		}

	}

	public static void updateNewDataSets(Connection connection) {
		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {

			String sql = "SELECT * FROM tbl_wifi_access_points WHERE latitude IS NULL";
			prepStmt = connection.prepareStatement(sql);

			// exec stmt
			rs = prepStmt.executeQuery();

			int i = 0;

			// fill DTO
			while (rs.next()) {
				Point tmp = GisServerUtil.getCenterCoordinateOfRoom(rs.getString(COL_NAME + 1));

				if (tmp == null) {
					i++;
				} else {
					System.out.println(rs.getString(COL_NAME + 1) + " - " + tmp);
					updateLocationInDb(connection, rs.getString(COL_MAC1 + 1), "" + tmp.x, "" + tmp.y, "" + tmp.z);
				}

			}
			System.out.println("not analyzed: " + i);

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(prepStmt);
		}

	}

	private static void updateLocationInDb(Connection connection, String primaryKeyMacAddress, String latitude, String longitude, String altitude) {
		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {
			// update sql
			String sql = "UPDATE tbl_wifi_access_points SET longitude = ?, latitude = ?, altitude = ?";
			sql += " WHERE mac1 = ?";

			// prepare stmt and fill variables
			prepStmt = connection.prepareStatement(sql);
			prepStmt.setString(1, longitude);
			prepStmt.setString(2, latitude);
			prepStmt.setString(3, altitude);

			// set where clause
			prepStmt.setString(4, primaryKeyMacAddress);

			// exec stmt
			int result = prepStmt.executeUpdate();

			if (result > 0)
				System.out.println("updated");

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(prepStmt);
		}
	}

	private static Status determineUpgradeStatus(String[] cols) {
		return Status.INSERT;
	}

	public enum Status {
		UPDATE, INSERT, UP_TO_DATE;
	}

	public static Connection getConnection(String db_host, String db_user, String db_pass) {

		Connection conn = null;

		try {
			Class.forName(DRIVER_CLASS_NAME).newInstance();
		} catch (Exception e) {
			System.err.println("mysql class driver not found");
		}

		try {
			conn = DriverManager.getConnection(db_host, db_user, db_pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return conn;

	}

}
