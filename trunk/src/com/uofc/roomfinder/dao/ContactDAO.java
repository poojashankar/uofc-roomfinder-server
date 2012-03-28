package com.uofc.roomfinder.dao;

import java.util.List;

import com.uofc.roomfinder.entities.Contact;
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
