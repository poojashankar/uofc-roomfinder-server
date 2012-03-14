package com.uofc.roomfinder.rest;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.uofc.roomfinder.entities.Annotation;

public class HelloWorldTest extends JerseyTest {

	public HelloWorldTest() throws Exception {
		super(new WebAppDescriptor.Builder("com.uofc.roomfinder.rest").contextPath("test").build());
	}

	/**
	 * Test that the expected response is sent back.
	 * 
	 * @throws java.lang.Exception
	 */
	@Test
	public void testHelloWorldXML() throws Exception {
		WebResource webResource = resource();

		String responseMsg = webResource.path("hello").accept("text/xml").get(String.class);
		System.out.println(responseMsg);
		Assert.assertTrue(responseMsg.contains("Hello XML"));
	}

	@Test
	public void testHelloWorldHTML() throws Exception {
		WebResource webResource = resource();

		String responseMsg = webResource.path("hello").accept("text/html").get(String.class);
		System.out.println(responseMsg);
		Assert.assertTrue(responseMsg.contains("Hello HTML"));
	}

	@Test
	public void testHelloWorldJson() throws Exception {
		final String TEXT = "test_SAVE_TEST";
		final String LONGITUDE = "x";
		final String LATITUDE = "y";
		final String ALTITUDE = "z";

		WebResource webResource = resource();

		String responseMsg = webResource.path("hello").accept(MediaType.APPLICATION_JSON).get(String.class);
		System.out.println(responseMsg);
		Annotation anno = new Gson().fromJson(responseMsg, Annotation.class);

		Assert.assertEquals(TEXT, anno.getText());
		Assert.assertEquals(LONGITUDE, anno.getLongitude());
		Assert.assertEquals(LATITUDE, anno.getLatitude());
		Assert.assertEquals(ALTITUDE, anno.getElevation());
	}

}
