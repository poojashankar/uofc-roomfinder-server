package com.uofc.roomfinder.dao;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.uofc.roomfinder.entities.WifiAccessPoint;

public class WifiAccessPointDAOImplTest {

	WifiAccessPointDAO wifiDAO;

	@Before
	public void setUp() throws Exception {
		wifiDAO = new WifiAccessPointDAOImpl();
	}

	@Test
	public void findByMac() {

		final String MAC_ADDRESS = "00:0B:86:D6:AC:00";
		final String NAME = "ICT_518A";
		final String CHANNEL = "11";
		final int POWER_LEVEL = 37;
		final double LATITUDE = -114.13038524345497;
		final double LONGITUDE = 51.080127625000095;
		final double ALTITUDE = 16.0;

		WifiAccessPoint ap = wifiDAO.findByMacAddress(MAC_ADDRESS);

		assertEquals(ap.getAltitude(), ALTITUDE, 0);
		assertEquals(ap.getChannel(), CHANNEL);
		assertEquals(ap.getLatitude(), LATITUDE, 0);
		assertEquals(ap.getLongitude(), LONGITUDE, 0);
		assertEquals(ap.getMacAddress(), MAC_ADDRESS);
		assertEquals(ap.getName(), NAME);
		assertEquals(ap.getOutgoingPowerLevel(), POWER_LEVEL);

	}

	@Test
	public void findByMacNoResult() {

		final String MAC_ADDRESS = "00:0B:86:D6:AC:00XXX";

		// invalid mac -> there shoud be no search result
		WifiAccessPoint ap = wifiDAO.findByMacAddress(MAC_ADDRESS);

		assertNull(ap);

	}
}
