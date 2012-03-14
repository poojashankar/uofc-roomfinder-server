package com.uofc.roomfinder.rest.location;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Context;

@Path("/map")
public class ArcGISMap {
	@Context ServletContext context;
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getMap(){
		
		StringBuilder html = new StringBuilder();

		html.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
		html.append("<html>");
		html.append("<head>");
		html.append("<title>Roomfinder</title>");
		html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
		html.append("<meta name=\"viewport\" content=\"initial-scale=1.0, maximum-scale=1.0, user-scalable=no\">");
		html.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"http://serverapi.arcgisonline.com/jsapi/arcgis/1.6/js/dojo/dijit/themes/tundra/tundra.css\"></link>");
		html.append("<link rel=\"stylesheet\" type=\"text/css\" src=\"" + context.getContextPath() + "/ressources/css/style.css\"></link>");
		html.append("<script type=\"text/javascript\" src=\"http://serverapi.arcgisonline.com/jsapi/arcgis/?v=2.7compact\"></script>");
		html.append("<script type=\"text/javascript\" src=\"" + context.getContextPath() + "/ressources/js/proj4js-combined.js\"></script>");
		html.append("<script type=\"text/javascript\" src=\"" + context.getContextPath() + "/ressources/js/arcgis.js\"></script>");
		html.append("</head>");
		html.append("<body onorientationchange=\"orientationChanged();\">");
		html.append("<div id=\"mapDiv\" style=\"width: 100%; height: 100%;\"></div>");
		html.append("</body>");
		html.append("</html>");

		return html.toString();		 
	}
	
}
