package com.uofc.roomfinder.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.uofc.roomfinder.entities.Building;

public class BuildingDAOImplTest {

	BuildingDAO buildingDao;

	@Before
	public void setUp() throws Exception {
		buildingDao = new BuildingDAOMySQL();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void updateTableTest() {
		new BuildingDAOMySQL().updateBuildingTable();

	}

	@Test
	public void findBuildingsByNameTest() {
		List<Building> result = buildingDao.findBuildingsByName("ICT");

		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getName(), "Information & Communication Technology");
		assertEquals(result.get(0).getAbbreviation(), "ICT");
	}

	@Test
	public void findBuildingsByNameTest1() {
		List<Building> result = buildingDao.findBuildingsByName("Information");

		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getName(), "Information & Communication Technology");
		assertEquals(result.get(0).getAbbreviation(), "ICT");
	}

	@Test
	public void findBuildingsByNameTest2() {
		List<Building> result = buildingDao.findBuildingsByName("information");

		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getName(), "Information & Communication Technology");
		assertEquals(result.get(0).getAbbreviation(), "ICT");
	}

	@Test
	public void findBuildingsByNameTest3() {
		List<Building> result = buildingDao.findBuildingsByName("information communication");

		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getName(), "Information & Communication Technology");
		assertEquals(result.get(0).getAbbreviation(), "ICT");
	}

	@Test
	public void findBuildingsByNameTest4() {
		List<Building> result = buildingDao.findBuildingsByName("informa commu");

		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getName(), "Information & Communication Technology");
		assertEquals(result.get(0).getAbbreviation(), "ICT");
	}

	@Test
	public void findBuildingsByNameTest5() {
		List<Building> result = buildingDao.findBuildingsByName("earth science");

		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getAbbreviation(), "ES");
	}

	@Test
	public void findBuildingsByNameTest6() {
		Building result = buildingDao.findByAbbreviation("ICT");

		assertEquals(result.getAbbreviation(), "ICT");
		assertEquals(result.getName(), "Information & Communication Technology");
	}

	@Test
	public void findBuildingsByNameTest7() {
		Building result = buildingDao.findByAbbreviation("XXX");

		assertEquals(result, null);
	}

	@Test
	public void createAndDeleteTest() {

		// create new building
		Building newBuilding = new Building();
		newBuilding.setAbbreviation("YYY");
		newBuilding.setName("JUNIT");
		newBuilding.setUse("JUNIT");

		// save it
		buildingDao.save(newBuilding);

		// find inserted building
		Building retrievedBuilding = buildingDao.findByAbbreviation("YYY");
		assertEquals(newBuilding.getAbbreviation(), retrievedBuilding.getAbbreviation());
		assertEquals(newBuilding.getName(), retrievedBuilding.getName());

		// delete
		Assert.assertTrue(buildingDao.delete(newBuilding));

		// look if it's still there
		retrievedBuilding = buildingDao.findByAbbreviation("YYY");
		assertEquals(retrievedBuilding, null);

	}
	
	@Test
	public void createAndDeleteTest2() {
		Building newBuilding = new Building();
		newBuilding.setAbbreviation("XQWSD");
		newBuilding.setName("JUNIT");
		newBuilding.setUse("JUNIT");

		//object is not in database, so it should be false
		Assert.assertFalse(buildingDao.delete(newBuilding));
	}

	@Test
	public void deleteTest() {
		Building newBuilding = new Building();
		newBuilding.setAbbreviation("XQWSE");
		newBuilding.setName("JUNIT");
		newBuilding.setUse("JUNIT");
		
		buildingDao.save(newBuilding);
		Assert.assertTrue(buildingDao.delete(newBuilding));
	}

	@Test
	public void updateTest() {

		// create new building
		Building newBuilding = new Building();
		newBuilding.setAbbreviation("ZZZ");
		newBuilding.setName("JUNIT");
		newBuilding.setUse("JUNIT");

		// save it
		buildingDao.save(newBuilding);

		// find inserted building
		Building retrievedBuilding = buildingDao.findByAbbreviation("ZZZ");
		assertEquals(newBuilding.getAbbreviation(), retrievedBuilding.getAbbreviation());
		assertEquals(newBuilding.getName(), retrievedBuilding.getName());

		// update it
		newBuilding.setName("updated");
		buildingDao.save(newBuilding);

		// find updated building
		retrievedBuilding = buildingDao.findByAbbreviation("ZZZ");
		assertEquals(newBuilding.getAbbreviation(), retrievedBuilding.getAbbreviation());
		assertEquals(newBuilding.getName(), retrievedBuilding.getName());

		// delete
		buildingDao.delete(newBuilding);

		// look if it's still there
		retrievedBuilding = buildingDao.findByAbbreviation("ZZZ");
		assertEquals(retrievedBuilding, null);

	}

}
