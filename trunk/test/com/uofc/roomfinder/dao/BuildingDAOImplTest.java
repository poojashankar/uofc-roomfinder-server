package com.uofc.roomfinder.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.uofc.roomfinder.entities.Building;

public class BuildingDAOImplTest {

	BuildingDAO buildingDao;

	@Before
	public void setUp() throws Exception {
		buildingDao = new BuildingDAOImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void findContactByNameTest() {
		List<Building> result = buildingDao.findBuildingsByName("ICT");

		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getName(), "Information & Communication Technology");
		assertEquals(result.get(0).getAbbreviation(), "ICT");
	}

	@Test
	public void findContactByNameTest1() {
		List<Building> result = buildingDao.findBuildingsByName("Information");

		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getName(), "Information & Communication Technology");
		assertEquals(result.get(0).getAbbreviation(), "ICT");
	}

	@Test
	public void findContactByNameTest2() {
		List<Building> result = buildingDao.findBuildingsByName("information");

		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getName(), "Information & Communication Technology");
		assertEquals(result.get(0).getAbbreviation(), "ICT");
	}

	@Test
	public void findContactByNameTest3() {
		List<Building> result = buildingDao.findBuildingsByName("information communication");

		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getName(), "Information & Communication Technology");
		assertEquals(result.get(0).getAbbreviation(), "ICT");
	}

	@Test
	public void findContactByNameTest4() {
		List<Building> result = buildingDao.findBuildingsByName("informa commu");

		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getName(), "Information & Communication Technology");
		assertEquals(result.get(0).getAbbreviation(), "ICT");
	}

	@Test
	public void findContactByNameTest5() {
		List<Building> result = buildingDao.findBuildingsByName("earth science");

		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getAbbreviation(), "ES");
	}
}
