package com.uofc.roomfinder.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.uofc.roomfinder.entities.Building;
import com.uofc.roomfinder.entities.User;

public class UserDAOImplTest {

	UserDAO userDao;
	String userName;
	String friendName;

	@Before
	public void setUp() throws Exception {
		userDao = new UserDAOImpl();

		Random generator = new Random();
		int r = (int) (generator.nextDouble() * 100000);
		userName = "test" + r;
		friendName = "friend" + r;

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void saveTest() {

		// create new user
		User newUser = new User(userName);

		// save it
		boolean test = userDao.save(newUser);
		Assert.assertTrue(test);
	}

	@Test
	public void saveTest2() {

		// create new user
		User newUser = new User(userName);

		// save it
		boolean test = userDao.save(newUser);
		Assert.assertTrue(test);

		// should fail because it is already inserted
		test = userDao.save(newUser);
		Assert.assertFalse(test);
	}

	@Test
	public void findTest() {
		// create new user
		User newUser = new User(userName);

		// save it
		boolean test = userDao.save(newUser);
		Assert.assertTrue(test);

		// find user
		User foundUser = userDao.getUser(newUser.getName());
		Assert.assertEquals(newUser.getName(), foundUser.getName());
	}

	@Test
	public void addFriendTest() {
		// create new user
		User newUser = new User(userName);

		// save it
		boolean test = userDao.save(newUser);
		Assert.assertTrue(test);

		Assert.assertTrue(userDao.addFriend(userName, friendName));
	}

	@Test
	public void getFriendsTest() {
		// create new user
		User newUser = new User(userName);
		User newFriend = new User(friendName);

		// save it
		userDao.save(newUser);
		userDao.save(newFriend);
		Assert.assertTrue(userDao.addFriend(userName, friendName));
		List<User> friendsList = userDao.getFriends(userName);

		assertEquals(friendsList.size(), 1);
	}

}
