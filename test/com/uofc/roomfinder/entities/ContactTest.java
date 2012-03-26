package com.uofc.roomfinder.entities;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.uofc.roomfinder.dao.ContactDAO;
import com.uofc.roomfinder.dao.ContactDAOImpl;

public class ContactTest {

	final String JSON_STRING = "{" + "  \"preName\": \"Frank\"," + "  \"surName\": \"Maurer\"," + "  \"commonName\": \"Frank Maurer\"," + "  \"emails\": ["
			+ "    \"maurer@cpsc.ucalgary.ca\"," + "    \"fmaurer@ucalgary.ca\"" + "  ]," + "  \"telephoneNumbers\": [" + "    \"+1 (403) 220-3531\","
			+ "    \"+1 (403) 220-7016\"" + "  ]," + "  \"roomNumber\": [" + "    \"ICT550\"," + "    \"A100\"" + "  ]," + "  \"departments\": ["
			+ "    \"Computer Science, Department\"," + "    \"EXECUTIVE SUITE\"" + "  ]" + "}";

	final String PRENAME = "Frank";
	final String SURNAME = "Maurer";
	final String BUILDING = "ICT550";

	@Test
	public void testGetJSON() {
		ContactDAO contactDao = new ContactDAOImpl();
		List<Contact> result = contactDao.findContactsByName("Frank Maurer");

		assertTrue(result.get(0).toJsonString().contains(PRENAME));
		assertTrue(result.get(0).toJsonString().contains(SURNAME));
		assertTrue(result.get(0).toJsonString().contains(BUILDING));

		System.out.println(result.get(0).toJsonString());
	}

	@Test
	public void testJsonConstructor() {
		Contact newContact = new Contact(JSON_STRING);
		Assert.assertEquals(PRENAME, newContact.getPreName());
		Assert.assertEquals(SURNAME, newContact.getSurName());
		Assert.assertEquals(BUILDING, newContact.getRoomNumber().get(0));
		
		assertEquals(newContact.toJsonString().replace("\n", "").replace(" ", ""), JSON_STRING.replace("\n", "").replace(" ", ""));
	}

}
