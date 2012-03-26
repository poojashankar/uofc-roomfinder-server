package com.uofc.roomfinder.dao;

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

}
