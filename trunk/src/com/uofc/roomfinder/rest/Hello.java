package com.uofc.roomfinder.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.uofc.roomfinder.entities.Annotation;

// POJO, no interface no extends

// The class registers its methods for the HTTP GET request using the @GET annotation. 
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML. 

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /hello
@Path("/hello")
public class Hello {

	// This method is called if TEXT_PLAIN is request
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "Hello Jersey";
	}

	// This method is called if XML is request
	@GET
	@Produces(MediaType.TEXT_XML)
	public String sayXMLHello() {
		return "<?xml version=\"1.0\"?>" + "<hello> Hello XML" + "</hello>";
	}

	// This method is called if HTML is request
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHtmlHello() {
		return "<html> " + "<title>" + "Hello World" + "</title>" + "<body><h1>" + "Hello HTML!" + "</body></h1>" + "</html> ";
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String sayJsonHello() {
		
		//curl t: application/json" http://localhost:9998/test/hello
		final String TEXT = "test_SAVE_TEST";
		final String LONGITUDE = "x";
		final String LATITUDE = "y";
		final String ALTITUDE = "z";
		
		Annotation newAnno = new Annotation();
		newAnno.setText(TEXT);
		newAnno.setLongitude(LONGITUDE);
		newAnno.setLatitude(LATITUDE);
		newAnno.setElevation(ALTITUDE);
		
		Gson gson = new Gson();
		String json = gson.toJson(newAnno);  
		
		return json;
	}

}