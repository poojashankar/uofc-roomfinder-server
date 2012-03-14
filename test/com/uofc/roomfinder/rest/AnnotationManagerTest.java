package com.uofc.roomfinder.rest;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.uofc.roomfinder.entities.Annotation;
import com.uofc.roomfinder.entities.AnnotationPackage;

public class AnnotationManagerTest extends JerseyTest {

	public AnnotationManagerTest() throws Exception {
		super(new WebAppDescriptor.Builder("com.uofc.roomfinder.rest").contextPath("").build());
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void getJsonById() {
		final long ID = 55;
		final String TEXT = "test_SAVE_TEST";
		final String LONGITUDE = "x";
		final String LATITUDE = "y";
		final String ALTITUDE = "z";

		WebResource webResource = resource();

		String responseMsg = webResource.path("annotation/id/" + ID).accept(MediaType.APPLICATION_JSON).get(String.class);
		System.out.println(responseMsg);
		Annotation anno = new Gson().fromJson(responseMsg, Annotation.class);

		// Assert.assertEquals(5,anno.getId());
		Assert.assertEquals(TEXT, anno.getText());
		Assert.assertEquals(LONGITUDE, anno.getLongitude());
		Assert.assertEquals(LATITUDE, anno.getLatitude());
		Assert.assertEquals(ALTITUDE, anno.getElevation());

	}

	@Test
	public void getJsonByIdsByOneId() throws IOException {
		final long ID = 55;
		final String TEXT = "test_SAVE_TEST";
		final String LONGITUDE = "x";
		final String LATITUDE = "y";
		final String ALTITUDE = "z";

		WebResource webResource = resource();
		//System.in.read();
		
		String responseMsg = webResource.path("annotation/ids/" + ID).accept(MediaType.APPLICATION_JSON).get(String.class);
		System.out.println(responseMsg);
		AnnotationPackage annoPackage = new Gson().fromJson(responseMsg, AnnotationPackage.class);

		// Assert.assertEquals(5,anno.getId());
		Assert.assertEquals(TEXT, annoPackage.getResults().getFirst().getText());
		Assert.assertEquals(LONGITUDE, annoPackage.getResults().getFirst().getLongitude());
		Assert.assertEquals(LATITUDE, annoPackage.getResults().getFirst().getLatitude());
		Assert.assertEquals(ALTITUDE, annoPackage.getResults().getFirst().getElevation());

	}

}
