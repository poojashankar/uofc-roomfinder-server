package com.uofc.roomfinder.util;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConnectionFactoryTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetFactory() {
		ConnectionFactory factory = ConnectionFactory.getInstance();
		assertNotNull(factory);
	}

	@Test
	public void testGetConnection() {
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();

			assertFalse(conn.isClosed());
			conn.close();
			assertTrue(conn.isClosed());

		} catch (SQLException e) {
			fail("cannot open sql connection");
		}
	}

}
