package com.uofc.roomfinder.entities;

import java.util.Date;

import junit.framework.Assert;

import org.codehaus.jettison.json.JSONString;
import org.junit.Test;

public class AnnotationTest {

	private final String JSON_STRING = "{" + "\"id\": \"2821\"," + "\"lat\": \"46.49396\"," + "\"lng\": \"11.2088\"," + "\"elevation\": \"1865\","
			+ "\"title\": \"Gantkofel\"," + "\"distance\": \"9.771\"," + "\"has_detail_page\": \"0\"," + "\"webpage\": \"\"" + "}";

	private final Long ID = 2821L;
	private final String TEXT = "Gantkofel";
	private final String LONGITUDE = "11.2088";
	private final String LATITUDE = "46.49396";
	private final String ALTITUDE = "1865";
	private final String DISTANCE = "9.771";
	private final int HAS_DETAIL_PAGE = 0;
	private final String WEBPAGE = "";

	
	
	@Test
	public void testGetJSON() {
		Annotation newAnno = new Annotation(ID, LATITUDE, LONGITUDE, ALTITUDE, TEXT, DISTANCE, HAS_DETAIL_PAGE, WEBPAGE, new Date());
		Assert.assertEquals(JSON_STRING.replace(" ", ""), newAnno.toJsonString().replace("\n", "").replace(" ", ""));
	}

	@Test
	public void testJsonConstructor() {
		Annotation newAnno = new Annotation(JSON_STRING);
		Assert.assertEquals(TEXT, newAnno.getText());
		Assert.assertEquals(LATITUDE, newAnno.getLatitude());
		Assert.assertEquals(ID, newAnno.getId());
	}

}
