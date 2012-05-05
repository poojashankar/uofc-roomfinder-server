package com.uofc.roomfinder.rest;

import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

public class RouteTest extends JerseyTest {

	final Long ID = 2l;
	final static String TEXT = "test";
	final static String X = "1.0";
	final static String Y = "2.0";
	final static String Z = "3.0";
	
	public RouteTest() throws Exception {
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
	public void getJsonByParameters() {
		WebResource webResource = resource();

		String responseMsg = webResource.path("annotation/withdata").queryParam("x", X).queryParam("y", Y).queryParam("z", Z).queryParam("text", TEXT).accept(MediaType.APPLICATION_JSON).get(String.class); //?x=" + X + "&y=" + Y + "&z=" + Z + "&text=" + TEXT).accept(MediaType.APPLICATION_JSON).get(String.class);
	
		assertTrue(responseMsg.contains(X));
		assertTrue(responseMsg.contains(Y));
		assertTrue(responseMsg.contains(Z));
		assertTrue(responseMsg.contains(TEXT));	

	}
}
