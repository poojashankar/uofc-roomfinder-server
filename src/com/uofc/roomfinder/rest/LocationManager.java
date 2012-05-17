package com.uofc.roomfinder.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/location")
public class LocationManager {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String determineLocationByWiFi(@QueryParam("macAddresses") String macAddresses, @QueryParam("powerLevels") String powerLevels) {

		System.out.println("macs: " + macAddresses + "\nlevels: " + powerLevels);
		
		String[] macAddressArr = macAddresses.split(",");
		String[] powerLevelArr = powerLevels.split(",");
		
		
		
		return null;
	}

}