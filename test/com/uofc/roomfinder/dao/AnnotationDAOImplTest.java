package com.uofc.roomfinder.dao;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.uofc.roomfinder.entities.Annotation;
import com.uofc.roomfinder.entities.AnnotationPackage;

public class AnnotationDAOImplTest {

	AnnotationDAO annotationDAO;
	
	@Before
	public void setUp() throws Exception {
		annotationDAO = new AnnotationDAOImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSaveAnnotation() {
		final String TEXT = "test_SAVE_TEST";
		final String LONGITUDE = "x";
		final String LATITUDE = "y";
		final String ALTITUDE = "z";
		final String DISTANCE = "1.59";
		final int HAS_DETAIL_PAGE = 1;
		final String WEBPAGE = "www.google.de";
		
		Annotation newAnno = new Annotation();
		newAnno.setText(TEXT);
		newAnno.setLongitude(LONGITUDE);
		newAnno.setLatitude(LATITUDE);
		newAnno.setElevation(ALTITUDE);
		newAnno.setDistance(DISTANCE);
		newAnno.setHas_detail_page(HAS_DETAIL_PAGE);
		newAnno.setWebpage(WEBPAGE);
		
		//save it
		annotationDAO.save(newAnno);
		
		//method has to set an ID (auto increment by DB)
		assertNotNull(newAnno.getId());
		assertTrue(newAnno.getId() > 0);		
	}
	
	@Test
	public void testSaveAnnotationAdv() {
		final String TEXT = "test_SAVE_TEST";
		final String LONGITUDE = "x";
		final String LATITUDE = "y";
		final String ALTITUDE = "z";
		
		Annotation newAnno = new Annotation();
		newAnno.setText(TEXT);
		newAnno.setLongitude(LONGITUDE);
		newAnno.setLatitude(LATITUDE);
		newAnno.setElevation(ALTITUDE);
		
		//save it
		annotationDAO.save(newAnno);
		
		//method has to set an ID (auto increment by DB)
		assertNotNull(newAnno.getId());
		assertTrue(newAnno.getId() > 0);		
	}
	
		
	@Test
	public void testSaveAnnotationExact400CharsText() {
		final String TEXT = "test_ThisTextHasExactly400CharsThisTextHasExactly400CharsThisTextHasExactly400CharsThisTextHasExactly400CharsThisTextHasExactly400CharsThisTextHasExactly400CharsThisTextHasExactly400CharsThisTextHasExactly400CharsThisTextHasExactly400CharsThisTextHasExactly400CharsThisTextHasExactly400CharsThisTextHasExactly400CharsThisTextHasExactly400CharsThisTextHasExactly400CharsThisTextHasExactly400CharsThisT";
		final String LONGITUDE = "x";
		final String LATITUDE = "y";
		final String ALTITUDE = "z";
		
		Annotation newAnno = new Annotation();
		newAnno.setText(TEXT);
		newAnno.setLongitude(LONGITUDE);
		newAnno.setLatitude(LATITUDE);
		newAnno.setElevation(ALTITUDE);
		
		//save it
		annotationDAO.save(newAnno);
		
		//method has to set an ID (auto increment by DB)
		assertNotNull(newAnno.getId());
		assertTrue(newAnno.getId() > 0);	
		
		//check if inserted text is correct
		Annotation idTestAnno = null;
		idTestAnno = annotationDAO.findByID(newAnno.getId());
		assertEquals(idTestAnno.getText(), TEXT);
	}
	
	@Test
	public void testSaveAnnotationToLongText() {
		final String TEXT = "test_ThisTextHasExactly401CharsThisTextHasExactly401CharsThisTextHasExactly401CharsThisTextHasExactly401CharsThisTextHasExactly401CharsThisTextHasExactly401CharsThisTextHasExactly401CharsThisTextHasExactly401CharsThisTextHasExactly401CharsThisTextHasExactly401CharsThisTextHasExactly401CharsThisTextHasExactly401CharsThisTextHasExactly401CharsThisTextHasExactly401CharsThisTextHasExactly401CharsThisTe";
		final String LONGITUDE = "x";
		final String LATITUDE = "y";
		final String ALTITUDE = "z";
		
		Annotation newAnno = new Annotation();
		newAnno.setText(TEXT);
		newAnno.setLongitude(LONGITUDE);
		newAnno.setLatitude(LATITUDE);
		newAnno.setElevation(ALTITUDE);
		
		//save it
		annotationDAO.save(newAnno);
		
		//method has to set an ID (auto increment by DB)
		assertNotNull(newAnno.getId());
		assertTrue(newAnno.getId() > 0);
		
		// check if inserted text is correct
		Annotation idTestAnno = null;
		idTestAnno = annotationDAO.findByID(newAnno.getId());
		assertEquals(idTestAnno.getText(), TEXT.substring(0, 400));
	}
	
	
	
	@Test
	public void testFindByID(){
		final String TEXT = "test_ID_TEST";
		final String LONGITUDE = "x";
		final String LATITUDE = "y";
		final String ALTITUDE = "z";
		
		Annotation newAnno = new Annotation();
		newAnno.setText(TEXT);
		newAnno.setLongitude(LONGITUDE);
		newAnno.setLatitude(LATITUDE);
		newAnno.setElevation(ALTITUDE);
		
		//save it
		annotationDAO.save(newAnno);
		assertNotNull(newAnno.getId());
		
		//should find the inserted anno by ID
		Annotation idTestAnno = null;
		idTestAnno = annotationDAO.findByID(newAnno.getId());
		assertNotNull(idTestAnno);
		
		//same object -> both IDs have to be the same
		assertEquals(idTestAnno.getId(), newAnno.getId());
		assertEquals(TEXT, idTestAnno.getText());	
		assertEquals(LATITUDE, idTestAnno.getLatitude());	
		assertEquals(LONGITUDE, idTestAnno.getLongitude());	
		assertEquals(ALTITUDE, idTestAnno.getElevation());
//		assertNotNull(idTestAnno.getTimestamp());
	}
	
	@Test
	public void testFindByIds(){
		final String TEXT1 = "test_ID_TEST1";
		final String TEXT2 = "test_ID_TEST2";
		final String TEXT3 = "test_ID_TEST3";
		final String LONGITUDE = "x";
		final String LATITUDE = "y";
		final String ALTITUDE = "z";
		
		//save and get IDs
		long annoId1 = saveAnno(TEXT1, LONGITUDE, LATITUDE, ALTITUDE);
		long annoId2 = saveAnno(TEXT2, LONGITUDE, LATITUDE, ALTITUDE);
		long annoId3 = saveAnno(TEXT3, LONGITUDE, LATITUDE, ALTITUDE);
		
		//call findByIds
		AnnotationPackage annotationPackage = annotationDAO.findByIds(annoId1, annoId2, annoId3);				
		
		//check if every text is in annotation package
		for (Annotation anno : annotationPackage.getResults()){
			assertTrue(anno.getText().equals(TEXT1) || anno.getText().equals(TEXT2) || anno.getText().equals(TEXT3));
		}
	}
	
	
	

	@Test
	public void testDelete(){
		final String TEXT = "test_DELETE_TEST";
		final String LONGITUDE = "x";
		final String LATITUDE = "y";
		final String ALTITUDE = "z";
		
		//get ID
		long annoIDToDelete = saveAnno(TEXT, LONGITUDE, LATITUDE, ALTITUDE);
		assertTrue(annoIDToDelete > 0);
		
		//delete it
		annotationDAO.delete(annotationDAO.findByID(annoIDToDelete));
		
		//find deleted anno should return null
		assertNull(annotationDAO.findByID(annoIDToDelete));		
	}
	
	
	
	private long saveAnno(String text, String longitude, String latitude, String altitude){
		Annotation newAnno = new Annotation();
		newAnno.setText(text);
		newAnno.setLongitude(longitude);
		newAnno.setLatitude(latitude);
		newAnno.setElevation(altitude);
		
		//save it
		annotationDAO.save(newAnno);
		return newAnno.getId();
	}

}
