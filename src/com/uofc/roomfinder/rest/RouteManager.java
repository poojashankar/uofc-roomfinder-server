package com.uofc.roomfinder.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uofc.roomfinder.entities.routing.Route;
import com.uofc.roomfinder.entities.routing.RoutePoint;
import com.uofc.roomfinder.util.gson.RouteJsonSerializer;

@Path("/route")
public class RouteManager {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAnnotationById(@QueryParam("x1") double x1, @QueryParam("y1") double y1, @QueryParam("z1") double z1,@QueryParam("x2") double x2, @QueryParam("y2") double y2, @QueryParam("z2") double z2, @QueryParam("impedance") String impedance) {
					
		//test points
		//RoutePoint start = new RoutePoint(701192.8861, 5662659.7696, 16.0);
		//RoutePoint end = new RoutePoint(701012.8757, 5662665.3092, 16.0);
		
		//create points from params
		RoutePoint start = new RoutePoint(x1, y1, z1);
		RoutePoint end = new RoutePoint(x2, y2, z2);		
		
		//get route
		Route newRoute = Route.getSoapRoute(start, end, impedance);
		
		//convert it to JSON
		Gson gson = new GsonBuilder().registerTypeAdapter(Route.class, new RouteJsonSerializer()).setPrettyPrinting().serializeNulls().create();
		String json = gson.toJson(newRoute);

		//return JSON route
		return json;
	}
}