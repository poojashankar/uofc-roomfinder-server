package com.uofc.roomfinder.dao;

import com.uofc.roomfinder.entities.ContactList;

/**
 * 
 * @author lauteb
 */
public interface ContactDAO {

	public ContactList findContactsByName(String name);

	public ContactList findContactsBuildingAndRoom(String buildingAndRoom);
	
	public ContactList findContacts(String searchString);

}
