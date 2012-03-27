package com.uofc.roomfinder.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.uofc.roomfinder.dao.ContactDAO;
import com.uofc.roomfinder.dao.ContactDAOImpl;
import com.uofc.roomfinder.entities.ContactList;

@Path("/contact")
public class ContactManager {

	ContactDAO contactDao = new ContactDAOImpl();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/name/{searchString}")
	public String getAnnotationById(@PathParam("searchString") String searchString) {
		
		// search directory for names and buildings
		ContactList contacts = contactDao.findContacts(searchString);
		 
		// get JSON representation of object
		return contacts.toJsonString();
	}

}
