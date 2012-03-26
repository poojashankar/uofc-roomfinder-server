package com.uofc.roomfinder.dao;

import java.util.List;

import com.uofc.roomfinder.entities.Contact;


/**
 * 
 * @author lauteb
 */
public interface ContactDAO {

	public List<Contact> findContactsByName(String name);
	public List<Contact> findContactsBuildingAndRoom(String buildingAndRoom);

}
