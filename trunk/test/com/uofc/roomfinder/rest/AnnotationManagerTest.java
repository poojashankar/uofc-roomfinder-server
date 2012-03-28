package com.uofc.roomfinder.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.uofc.roomfinder.entities.Annotation;
import com.uofc.roomfinder.entities.AnnotationList;
import com.uofc.roomfinder.util.gson.AnnotationJsonDeserializer;

public class AnnotationManagerTest extends JerseyTest {

	final Long ID = 2l;
	
	public AnnotationManagerTest() throws Exception {
		super(new WebAppDescriptor.Builder("com.uofc.roomfinder.rest").contextPath("").build());
	}

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Override
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void getJsonById() {
		final String TEXT = "test_SAVE_TEST";
		final String LONGITUDE = "x";
		final String LATITUDE = "y";
		final String ALTITUDE = "z";

		WebResource webResource = resource();

		String responseMsg = webResource.path("annotation/id/" + ID).accept(MediaType.APPLICATION_JSON).get(String.class);
		Annotation anno = new Annotation(responseMsg);

		// Assert.assertEquals(5,anno.getId());
		assertEquals(TEXT, anno.getText());
		assertEquals(LONGITUDE, anno.getLongitude());
		assertEquals(LATITUDE, anno.getLatitude());
		assertEquals(ALTITUDE, anno.getElevation());

	}

	@Test
	public void getJsonByIdsByOneId() throws IOException {
		
		final String TEXT = "test_SAVE_TEST";
		final String LONGITUDE = "x";
		final String LATITUDE = "y";
		final String ALTITUDE = "z";

		WebResource webResource = resource();

		String responseMsg = webResource.path("annotation/ids/" + ID).accept(MediaType.APPLICATION_JSON).get(String.class);
		// System.out.println(responseMsg);
		AnnotationList annoPackage = new AnnotationList(responseMsg);

		assertEquals(ID, annoPackage.getResults().getFirst().getId());
		assertEquals(TEXT, annoPackage.getResults().getFirst().getText());
		assertEquals(LONGITUDE, annoPackage.getResults().getFirst().getLongitude());
		assertEquals(LATITUDE, annoPackage.getResults().getFirst().getLatitude());
		assertEquals(ALTITUDE, annoPackage.getResults().getFirst().getElevation());

	}

	@Test
	public void testAnnotationPackageDeserializer() {
		final String JSON_TEST = "{ " + "  \"status\": \"OK\", " + "  \"num_results\": 2, " + "  \"results\": [ " + "    { " + "      \"id\": \"165\", "
				+ "      \"lat\": \"y\", " + "      \"lng\": \"x\", " + "      \"elevation\": \"z\", " + "      \"title\": \"test_SAVE_TEST\", "
				+ "      \"distance\": \"0\", " + "      \"has_detail_page\": \"0\", " + "      \"webpage\": \"www.google.de\", "
				+ "      \"timestamp\": \"14/Mar/2012 00:00:00\" " + "    }, " + "    { " + "      \"id\": \"166\", " + "      \"lat\": \"y\", "
				+ "      \"lng\": \"x\", " + "      \"elevation\": \"z\", " + "      \"title\": \"test_ID_TEST1\", " + "      \"distance\": \"0\", "
				+ "      \"has_detail_page\": \"0\", " + "      \"webpage\": \"www.google.de\", " + "      \"timestamp\": \"14/Mar/2012 00:00:00\" " + "    } "
				+ "  ] " + "}";

		Gson gson = new GsonBuilder().registerTypeAdapter(Annotation.class, new AnnotationJsonDeserializer()).serializeNulls().create();
		AnnotationList annoPackage = gson.fromJson(JSON_TEST, AnnotationList.class);
		assertEquals(2, annoPackage.getNum_results());
		assertEquals("test_ID_TEST1", annoPackage.getResults().get(1).getText());
		assertEquals("0", annoPackage.getResults().get(0).getDistance());
	}
	
	@Test
	public void getAnnoPackageByCategory() throws IOException {
		WebResource webResource = resource();

		String responseMsg = webResource.path("annotation/cat/buildings").accept(MediaType.APPLICATION_JSON).get(String.class);

		assertTrue(responseMsg.contains("MacEwan Hall"));
		//assertTrue(responseMsg.contains("Earth Science"));
	}
	

}
