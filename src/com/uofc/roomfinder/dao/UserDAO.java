package com.uofc.roomfinder.dao;

import java.util.List;

import com.uofc.roomfinder.entities.User;

public interface UserDAO {

	public User getUser(String userName);

	public boolean save(User user);

	public boolean addFriend(String userName, String friendName);
	
	public List<User> getFriends(String userName);

}
