package com.uofc.roomfinder.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.uofc.roomfinder.dao.ContactDAO;
import com.uofc.roomfinder.dao.ContactDaoGis;
import com.uofc.roomfinder.dao.ContactDaoLdap;
import com.uofc.roomfinder.entities.ContactList;

@Path("/contact")
public class ContactManager {

	ContactDAO contactDaoLdap = new ContactDaoLdap();
	ContactDAO contactDaoGis = new ContactDaoGis();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/name/{searchString}")
	public String getAnnotationById(@PathParam("searchString") String searchString) {
		
		// search directory for names and buildings
		ContactList contacts = contactDaoLdap.findContacts(searchString);
		contacts.addAll(contactDaoGis.findContacts(searchString));
		 
		// get JSON representation of object
		return contacts.toJsonString();
	}

}
