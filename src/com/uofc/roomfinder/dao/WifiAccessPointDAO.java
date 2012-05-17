package com.uofc.roomfinder.dao;

import com.uofc.roomfinder.entities.WifiAccessPoint;

/**
 * 
 * @author lauteb
 */
public interface WifiAccessPointDAO {

	public WifiAccessPoint findByMacAddress(String macAddress);
}
