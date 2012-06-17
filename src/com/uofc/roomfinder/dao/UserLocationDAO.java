package com.uofc.roomfinder.dao;

import com.uofc.roomfinder.entities.UserLocation;

public interface UserLocationDAO {

	public UserLocation getLastKnownPosition(String userName);

	public boolean save(UserLocation location);

}
