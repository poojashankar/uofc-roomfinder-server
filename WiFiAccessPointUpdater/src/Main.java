import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;

import org.apache.commons.dbutils.DbUtils;

import com.esri.arcgisws.Envelope;
import com.esri.arcgisws.PointB;

public class Main {

	/**
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		Envelope env = null;
		PointB pt = new PointB();

		String db_host = "jdbc:mysql://ec2-23-20-196-109.compute-1.amazonaws.com:3306/roomfinder";
		String db_user = "root";
		String db_pass = "roomfinder123!X";
		String csvFileName = "/Users/benjaminlautenschlaeger/Dropbox/ASE/_csv access points/AP report.csv";

		Connection mysqlConnection = MySqlHandler.getConnection(db_host, db_user, db_pass);

		// int upodatetRows = loadCsvDataIntoDb(csvFileName, mysqlConnection);
		// System.out.println("updated: " + upodatetRows);
		try {
			MySqlHandler.updateNewDataSets(mysqlConnection);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(mysqlConnection);
		}

	}

	private static int loadCsvDataIntoDb(String filePath, Connection mysqlConnection) {

		String line = null;
		int updatedRows = 0;

		try {
			BufferedReader bufRdr = new BufferedReader(new FileReader(filePath));

			// first line is the title line
			bufRdr.readLine();

			// read each line of csv file
			while ((line = bufRdr.readLine()) != null) {
				String[] cols = line.split(",");

				// insert or update or nothing
				System.out.println(cols[0]);
				updatedRows += MySqlHandler.upgradeDataset(mysqlConnection, cols);
			}

			// close the file
			bufRdr.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return updatedRows;
	}

}
