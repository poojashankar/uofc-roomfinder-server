package com.uofc.roomfinder.dao;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.uofc.roomfinder.entities.Contact;

public class ContactDAOImplTest {

	ContactDAO contactDao;

	@Before
	public void setUp() throws Exception {
		contactDao = new ContactDAOImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void findContactByNameTest() {
		List<Contact> result = contactDao.findContactsByName("Frank Maurer");

		assertEquals(result.get(0).getPreName(), "Frank");
		assertEquals(result.get(0).getSurName(), "Maurer");
		assertEquals(result.get(0).getRoomNumber().get(0), "ICT550");
	}

	@Test
	public void findContactByNameTest1() {
		List<Contact> result = contactDao.findContactsByName("Fra Mau");

		assertEquals(result.get(0).getPreName(), "Frank");
		assertEquals(result.get(0).getSurName(), "Maurer");
		assertEquals(result.get(0).getRoomNumber().get(0), "ICT550");
	}

	@Test
	public void findContactByNameTest2() {
		List<Contact> result = contactDao.findContactsByName("Maurer Frank");

		assertEquals(result.get(0).getPreName(), "Frank");
		assertEquals(result.get(0).getSurName(), "Maurer");
		assertEquals(result.get(0).getRoomNumber().get(0), "ICT550");
	}

	@Test
	public void findContactByNameTest3() {
		List<Contact> result = contactDao.findContactsByName("Mau Frank");

		assertEquals(result.get(0).getPreName(), "Frank");
		assertEquals(result.get(0).getSurName(), "Maurer");
		assertEquals(result.get(0).getRoomNumber().get(0), "ICT550");
	}

	@Test
	public void findContactByBuildingAndRoom() {
		List<Contact> result = contactDao.findContactsBuildingAndRoom("ICT550");

		assertEquals(result.get(0).getPreName(), "Frank");
		assertEquals(result.get(0).getSurName(), "Maurer");
		assertEquals(result.get(0).getRoomNumber().get(0), "ICT550");
	}

	@Test
	public void findContactByBuildingAndRoom1() {
		List<Contact> result = contactDao.findContactsBuildingAndRoom("ICT 550");

		assertEquals(result.get(0).getPreName(), "Frank");
		assertEquals(result.get(0).getSurName(), "Maurer");
		assertEquals(result.get(0).getRoomNumber().get(0), "ICT550");
	}

	@Test
	public void findContactByBuildingAndRoom2() {
		List<Contact> result = contactDao.findContactsBuildingAndRoom("550 ICT");

		assertEquals(result.get(0).getPreName(), "Frank");
		assertEquals(result.get(0).getSurName(), "Maurer");
		assertEquals(result.get(0).getRoomNumber().get(0), "ICT550");
	}
	
	@Test
	public void findContactByBuildingAndRoom3() {
		List<Contact> result = contactDao.findContactsBuildingAndRoom("Information & Communication Technology 550 ");

		assertEquals(result.get(0).getPreName(), "Frank");
		assertEquals(result.get(0).getSurName(), "Maurer");
		assertEquals(result.get(0).getRoomNumber().get(0), "ICT550");
	}
	
	@Test
	public void findContactByBuildingAndRoom4() {
		List<Contact> result = contactDao.findContactsBuildingAndRoom("information communication 550 ");

		assertEquals(result.get(0).getPreName(), "Frank");
		assertEquals(result.get(0).getSurName(), "Maurer");
		assertEquals(result.get(0).getRoomNumber().get(0), "ICT550");
	}
	
	@Test
	public void findContactByBuildingAndRoom5() {
		List<Contact> result = contactDao.findContactsBuildingAndRoom("earth science 550 ");

		assertEquals(result.get(0).getRoomNumber().get(0), "ES550");
	}
	
	@Test
	public void findContactByBuildingAndRoom6() {
		List<Contact> result = contactDao.findContactsBuildingAndRoom("math 680");

		assertEquals(result.get(0).getRoomNumber().get(0), "MS680");
	}
	
	@Test
	public void findContactByBuildingAndRoom7() {
		//also rooms without contact information should be found (search on ArcGIS server)
		List<Contact> result = contactDao.findContacts("ict117");

		assertEquals(result.get(0).getRoomNumber().get(0), "ICT117");
	}
	
	@Test
	public void findContactByBuildingAndRoom8() {
		//also rooms without contact information should be found (search on ArcGIS server)
		List<Contact> result = contactDao.findContacts("ict 117");

		assertEquals(result.get(0).getRoomNumber().get(0), "ICT117");
	}
	
	@Test
	public void findContactByBuildingAndRoom9() {
		List<Contact> result = contactDao.findContacts("ict 1178");

		assertTrue(result.size() == 0);
	}

}
