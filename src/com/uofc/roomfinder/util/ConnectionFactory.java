package com.uofc.roomfinder.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * this class handles the mysql connections
 * 
 * @author lauteb
 * 
 */
public class ConnectionFactory {
	

	final String USER_NAME = "root"; 
	final String PASSWORD = ""; 
	//final String URL = "jdbc:mysql://localhost:3306/roomfinder"; 
	final String URL = "jdbc:mysql://10.11.27.137:3306/roomfinder";
	final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";

	private static ConnectionFactory connectionFactory = null;

	/**
	 * constructor
	 */
	private ConnectionFactory() {
		// lookup for mysql connection definition in context.xml
		
		
		
//		try {
//
//			// JNDI does not work. you cannot run connect via JNDI, when youre
//			// outside the tomcat container (e.g. junit tests are outside the
//			// tomcat container)
//			// InitialContext ic = new InitialContext();
//			// Context xmlContext = (Context)
//			// ic.lookup("java:comp/env/jdbc/roomfinder");
//			// this.ds = (DataSource) ic.lookup("jdbc/roomfinder");
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * returns a new mysql connection
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		Connection conn = null;
		
		try {
			Class.forName (DRIVER_CLASS_NAME).newInstance ();
		} catch (Exception e) {
			System.err.println("mysql class driver not found");
		} 
		
		conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD); 
		
		return conn;
	}

	/**
	 * 
	 * @return
	 */
	public static ConnectionFactory getInstance() {
		if (connectionFactory == null) {
			connectionFactory = new ConnectionFactory();
		}
		return connectionFactory;
	}
}