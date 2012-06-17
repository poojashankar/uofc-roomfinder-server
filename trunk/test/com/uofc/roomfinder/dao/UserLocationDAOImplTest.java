package com.uofc.roomfinder.dao;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.uofc.roomfinder.entities.Building;
import com.uofc.roomfinder.entities.Point3D;
import com.uofc.roomfinder.entities.User;
import com.uofc.roomfinder.entities.UserLocation;

public class UserLocationDAOImplTest {

	UserLocationDAO userLocationDao;
	String userName;

	@Before
	public void setUp() throws Exception {
		userLocationDao = new UserLocationDAOImpl();

		Random generator = new Random();
		int r = (int) (generator.nextDouble() * 100000);
		userName = "test" + r;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void saveTest() {
		Timestamp testDate = new Timestamp(new Date().getTime());

		// create new user
		UserLocation newUserLocation = new UserLocation();
		newUserLocation.setLocation(new Point3D(1, 2, 3));
		newUserLocation.setTimestamp(testDate);
		newUserLocation.setUser(new User(userName));

		// save it
		boolean test = userLocationDao.save(newUserLocation);
		Assert.assertTrue(test);
	}

	@Test
	public void getTest() {
		Timestamp testDate = new Timestamp(new Date().getTime());

		// create new user
		UserLocation newUserLocation = new UserLocation();
		newUserLocation.setLocation(new Point3D(1, 2, 3));
		newUserLocation.setTimestamp(testDate);
		newUserLocation.setUser(new User(userName));

		// save it
		boolean test = userLocationDao.save(newUserLocation);
		Assert.assertTrue(test);

		// get it
		UserLocation foundLocation = userLocationDao.getLastKnownPosition(userName);
		Assert.assertNotNull(foundLocation);
		Assert.assertEquals(foundLocation.getLocation().getX(), new Point3D(1, 2, 3).getX());
		Assert.assertEquals(foundLocation.getLocation().getY(), new Point3D(1, 2, 3).getY());
		Assert.assertEquals(foundLocation.getLocation().getZ(), new Point3D(1, 2, 3).getZ());
	}

}
