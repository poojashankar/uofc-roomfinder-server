package com.uofc.roomfinder.dao;

import static org.junit.Assert.*;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.uofc.roomfinder.entities.Contact;

public class ContactDAOImplTest {

	ContactDAO contactDaoMysql;
	ContactDAO contactDaoGis;

	@Before
	public void setUp() throws Exception {
		contactDaoMysql = new ContactDaoLdap();
		contactDaoGis = new ContactDaoGis();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void findContactByNameTest() {
		List<Contact> result = contactDaoMysql.findContactsByName("Frank Maurer");

		assertEquals(result.get(0).getPreName(), "Frank");
		assertEquals(result.get(0).getSurName(), "Maurer");
		assertEquals(result.get(0).getRoomNumber().get(0), "ICT550");
	}

	@Test
	public void findContactByNameTest1() {
		List<Contact> result = contactDaoMysql.findContactsByName("Fra Mau");

		assertEquals(result.get(0).getPreName(), "Frank");
		assertEquals(result.get(0).getSurName(), "Maurer");
		assertEquals(result.get(0).getRoomNumber().get(0), "ICT550");
	}

	@Test
	public void findContactByNameTest2() {
		List<Contact> result = contactDaoMysql.findContactsByName("Maurer Frank");

		assertEquals(result.get(0).getPreName(), "Frank");
		assertEquals(result.get(0).getSurName(), "Maurer");
		assertEquals(result.get(0).getRoomNumber().get(0), "ICT550");
	}

	@Test
	public void findContactByNameTest3() {
		List<Contact> result = contactDaoMysql.findContactsByName("Mau Frank");

		assertEquals(result.get(0).getPreName(), "Frank");
		assertEquals(result.get(0).getSurName(), "Maurer");
		assertEquals(result.get(0).getRoomNumber().get(0), "ICT550");
	}

	@Test
	public void findContactByBuildingAndRoom() {
		List<Contact> result = contactDaoMysql.findContactsBuildingAndRoom("ICT550");

		assertEquals(result.get(0).getPreName(), "Frank");
		assertEquals(result.get(0).getSurName(), "Maurer");
		assertEquals(result.get(0).getRoomNumber().get(0), "ICT550");
	}

	@Test
	public void findContactByBuildingAndRoom1() {
		List<Contact> result = contactDaoMysql.findContactsBuildingAndRoom("ICT 550");

		assertEquals(result.get(0).getPreName(), "Frank");
		assertEquals(result.get(0).getSurName(), "Maurer");
		assertEquals(result.get(0).getRoomNumber().get(0), "ICT550");
	}

	@Test
	public void findContactByBuildingAndRoom2() {
		List<Contact> result = contactDaoMysql.findContactsBuildingAndRoom("550 ICT");

		assertEquals(result.get(0).getPreName(), "Frank");
		assertEquals(result.get(0).getSurName(), "Maurer");
		assertEquals(result.get(0).getRoomNumber().get(0), "ICT550");
	}

	@Test
	public void findContactByBuildingAndRoom3() {
		List<Contact> result = contactDaoMysql.findContactsBuildingAndRoom("Information & Communication Technology 550 ");

		assertEquals(result.get(0).getPreName(), "Frank");
		assertEquals(result.get(0).getSurName(), "Maurer");
		assertEquals(result.get(0).getRoomNumber().get(0), "ICT550");
	}

	@Test
	public void findContactByBuildingAndRoom4() {
		List<Contact> result = contactDaoMysql.findContactsBuildingAndRoom("information communication 550 ");

		assertEquals(result.get(0).getPreName(), "Frank");
		assertEquals(result.get(0).getSurName(), "Maurer");
		assertEquals(result.get(0).getRoomNumber().get(0), "ICT550");
	}

	@Test
	public void findContactByBuildingAndRoom5() {
		List<Contact> result = contactDaoMysql.findContactsBuildingAndRoom("earth science 550 ");

		assertEquals(result.get(0).getRoomNumber().get(0), "ES550");
	}

	@Test
	public void findContactByBuildingAndRoom6() {
		List<Contact> result = contactDaoMysql.findContactsBuildingAndRoom("math 680");

		assertEquals(result.get(0).getRoomNumber().get(0), "MS680");
	}

	@Test
	public void findContactByBuildingAndRoom7() {
		// also rooms without contact information should be found (search on ArcGIS server)
		List<Contact> result = contactDaoMysql.findContacts("ict117");
		result.addAll(contactDaoGis.findContacts("ict117"));

		assertEquals(result.get(0).getRoomNumber().get(0), "ICT117");
	}

	@Test
	public void findContactByBuildingAndRoom8() {
		// also rooms without contact information should be found (search on ArcGIS server)
		List<Contact> result = contactDaoMysql.findContacts("ict 117");
		result.addAll(contactDaoGis.findContacts("ict 117"));

		assertEquals(result.get(0).getRoomNumber().get(0), "ICT117");
	}

	@Test
	public void findContactByBuildingAndRoom9() {
		List<Contact> result = contactDaoMysql.findContacts("ict 1178");
		result.addAll(contactDaoGis.findContacts("ict 1178"));
		assertTrue(result.size() == 0);
	}

	// room number with leading letter
	@Test
	public void findContactByBuildingAndRoom10() {
		List<Contact> result = contactDaoMysql.findContacts("hs g344");
		result.addAll(contactDaoGis.findContacts("hs g344"));

		assertEquals(result.get(0).getRoomNumber().get(0), "HSG344");
	}

	@Test
	public void findContactByBuildingAndRoom11() {
		List<Contact> result = contactDaoMysql.findContacts("hs 344");
		result.addAll(contactDaoGis.findContacts("hs 344"));

		assertTrue(result.get(0).getRoomNumber().get(0).contains("HS"));
		assertTrue(result.get(0).getRoomNumber().get(0).contains("344"));
	}

	@Test
	public void findContactByBuildingAndRoom12() {
		List<Contact> result = contactDaoMysql.findContacts("edt03");
		result.addAll(contactDaoGis.findContacts("edt03"));

		boolean roomFound = false;
		for (Contact contact : result) {
			if (contact.getRoomNumber().get(0).contains("EDT03"))
				roomFound = true;

		}

		assertTrue(roomFound);

	}

}
