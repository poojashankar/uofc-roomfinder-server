package com.uofc.roomfinder.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uofc.roomfinder.dao.WifiAccessPointDAO;
import com.uofc.roomfinder.dao.WifiAccessPointDAOImpl;
import com.uofc.roomfinder.entities.Point3D;
import com.uofc.roomfinder.entities.WifiAccessPoint;
import com.uofc.roomfinder.util.Vector3d;

@Path("/location")
public class LocationManager {

	private static final double EARTH_RADIUS_IN_M = 6371000;
	private static final double SPEED_OF_LIGHT = 299792458; // speed of light in m/s
	private static final double FREE_SPACE_LOSS_EXPONENT = 4; // 2 for free air

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String determineLocationByWiFi(@QueryParam("macAddresses") String macAddresses, @QueryParam("powerLevels") String powerLevels,
			@QueryParam("frequencies") String frequencies) {

		System.out.println("macs: " + macAddresses + "\nlevels: " + powerLevels + "\nfreq: " + frequencies);

		String[] macAddressArr = macAddresses.split(",");
		String[] powerLevelArr = powerLevels.split(",");
		String[] frequenciesArr = frequencies.split(",");

		WifiAccessPointDAO wifiDao = new WifiAccessPointDAOImpl();
		WifiAccessPoint wifiAP = null;
		List<WifiAccessPoint> wifiApList = new ArrayList<WifiAccessPoint>();

		for (int i = 0; i < macAddressArr.length; i++) {
			wifiAP = wifiDao.findByMacAddress(macAddressArr[i]);

			if (wifiAP != null) {
				// set current power level of AP (got from REST call)
				try {
					// try to convert it to an int value
					int tmp = Integer.parseInt(powerLevelArr[i]);
					wifiAP.setCurrentPowerLevel(tmp);
				} catch (Exception e) {
				}

				// set current power level of AP (got from REST call)
				try {
					// try to convert it to an int value
					int tmp = Integer.parseInt(frequenciesArr[i]);
					wifiAP.setFrequency(tmp);
				} catch (Exception e) {
				}

				// add AP to the list of all known APs
				wifiApList.add(wifiAP);
			}
			System.out.println(macAddressArr[i] + ": " + wifiAP);

		}

		// get location and return it as JSON
		Point3D wifiLocation = getWifiLocation(wifiApList);
		Gson gson = new GsonBuilder().serializeNulls().create();
		String json = gson.toJson(wifiLocation);

		return json;
	}

	private Point3D getWifiLocation(List<WifiAccessPoint> wifiApList) {
		List<WifiAccessPoint> trilaterateAPs = new ArrayList<WifiAccessPoint>();

		// iterate each access point
		for (WifiAccessPoint wifiAP : wifiApList) {
			// compute distance to each access point
			getDistanceToAp(wifiAP);

			// add to list of APs for trilateration (only one ap with the same name (=location) allowed)
			if (!isNameAlreadyInList(trilaterateAPs, wifiAP.getName()) && wifiAP.getDistanceToAP() > 0) {
				trilaterateAPs.add(wifiAP);
			}
		}

		// compute x and y coordinate
		// ==========================
		String location = null;
		String lat = "";
		String lng = "";

		// if there are at least 3 access points in the trilateration list -> trialterate position
		if (trilaterateAPs.size() >= 3) {
			//location = trilaterateWifiAccessPointData(trilaterateAPs.get(0), trilaterateAPs.get(1), trilaterateAPs.get(2));
			
			if (location != null){
				lat = location.split(",")[0];
				lng = location.split(",")[1];	
			}else{
				location = trilaterateByAverage(trilaterateAPs.get(0), trilaterateAPs.get(1), trilaterateAPs.get(2));
				lat = location.split(",")[0];
				lng = location.split(",")[1];
			}
			System.out.println("loc: " +location);
		}

		// compute z coordinate
		// ====================
		double sumAltitude = 0;
		int altCounter = 0;

		// get average altitude of access points
		for (WifiAccessPoint wifiAP : wifiApList) {
			if (wifiAP.getAltitude() > -1000) {
				sumAltitude += wifiAP.getAltitude();
				altCounter++;
			}
		}
		double avgAltitude = sumAltitude / altCounter;

		// get nearest floor
		long floorLevel = Math.round(avgAltitude / 4);
		double alt = floorLevel * 4;

		// create route point
		Point3D result = new Point3D(Double.parseDouble(lng), Double.parseDouble(lat), alt);
		return result;
	}

	/**
	 * helper method
	 * 
	 * @param trilaterateAPs
	 *            list to be searched
	 * @param name
	 * @return true if is alreadz in list, false if not
	 */
	private boolean isNameAlreadyInList(List<WifiAccessPoint> trilaterateAPs, String name) {

		for (WifiAccessPoint accessPoint : trilaterateAPs) {
			if (accessPoint.getName().equals(name)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * calculated by the free space loss equation
	 * 
	 * http://en.wikipedia.org/wiki/Free-space_path_loss
	 * 
	 * @param powerTransmitter
	 *            power level in dbm of the transceiver
	 * @param powerReceiver
	 *            power level in dbm of the receiver
	 * @param frequence
	 *            frequency of the access point (e.g. 2400 MHz / 5000 MHz) in MHz
	 * @return
	 */
	private static double getDistanceToAp(WifiAccessPoint accessPoint) {

		// initial values
		double powerTransmitter = accessPoint.getOutgoingPowerLevel();
		double powerReceiver = accessPoint.getCurrentPowerLevel();
		double frequencyInMHz = accessPoint.getFrequency();

		// calc wavelength
		double lambda = SPEED_OF_LIGHT / (frequencyInMHz * 1000000);

		// calc constant
		double constant = 20 * Math.log10(lambda / (4 * Math.PI));

		// calculate path loss (transmitter power - receiver power)
		double fspl = powerTransmitter - powerReceiver;

		// compute the rest of the equation
		double result = (fspl + constant) * -1;
		result = result / (10 * FREE_SPACE_LOSS_EXPONENT);
		result = Math.pow(10, result);
		result = 1 / result;

		System.out.println("const: " + constant);
		System.out.println("fspl:  " + fspl);
		System.out.println("res:   " + result);
		System.out.println("");

		// set to object
		accessPoint.setDistanceToAP(result);

		return result;
	}

	private static String trilaterateByAverage(WifiAccessPoint wifiA, WifiAccessPoint wifiB, WifiAccessPoint wifiC) {
		double latApprox = (wifiA.getLatitude() + wifiB.getLatitude() + wifiC.getLatitude()) / 3;
		double lngApprox = (wifiA.getLongitude() + wifiB.getLongitude() + wifiC.getLongitude()) / 3;
		System.out.println(latApprox + ", " + lngApprox);
		return latApprox + "," + lngApprox;
	}

	private static String trilaterateWifiAccessPointData(WifiAccessPoint wifiA, WifiAccessPoint wifiB, WifiAccessPoint wifiC) {

		// point A
		double latitudeA = wifiA.getLatitude();
		double longitudeA = wifiA.getLongitude();
		double distanceToA = wifiA.getDistanceToAP();

		// point B
		double latitudeB = wifiB.getLatitude();
		double longitudeB = wifiB.getLongitude();
		double distanceToB = wifiB.getDistanceToAP();

		// point C
		double latitudeC = wifiC.getLatitude();
		double longitudeC = wifiC.getLongitude();
		double distanceToC = wifiC.getDistanceToAP();

		// convert lat, long to cartesian coordinates (ECEF)
		double xA = EARTH_RADIUS_IN_M * (Math.cos(Math.toRadians(latitudeA)) * Math.cos(Math.toRadians(longitudeA)));
		double yA = EARTH_RADIUS_IN_M * (Math.cos(Math.toRadians(latitudeA)) * Math.sin(Math.toRadians(longitudeA)));
		double zA = EARTH_RADIUS_IN_M * (Math.sin(Math.toRadians(latitudeA)));

		double xB = EARTH_RADIUS_IN_M * (Math.cos(Math.toRadians(latitudeB)) * Math.cos(Math.toRadians(longitudeB)));
		double yB = EARTH_RADIUS_IN_M * (Math.cos(Math.toRadians(latitudeB)) * Math.sin(Math.toRadians(longitudeB)));
		double zB = EARTH_RADIUS_IN_M * (Math.sin(Math.toRadians(latitudeB)));

		double xC = EARTH_RADIUS_IN_M * (Math.cos(Math.toRadians(latitudeC)) * Math.cos(Math.toRadians(longitudeC)));
		double yC = EARTH_RADIUS_IN_M * (Math.cos(Math.toRadians(latitudeC)) * Math.sin(Math.toRadians(longitudeC)));
		double zC = EARTH_RADIUS_IN_M * (Math.sin(Math.toRadians(latitudeC)));

		// create 3d vectors from x,y,z coordinates
		Vector3d vecP1 = new Vector3d(xA, yA, zA);
		Vector3d vecP2 = new Vector3d(xB, yB, zB);
		Vector3d vecP3 = new Vector3d(xC, yC, zC);

		// lets transform the vectors to get circle 1 to the origin (0,0)
		// and circle 2 on x axis
		Vector3d ex = Vector3d.div(Vector3d.sub(vecP2, vecP1), Vector3d.sub(vecP2, vecP1).magnitude());// (P2 - P1) / (numpy.linalg.norm(P2 - P1));
		double i = ex.dot(Vector3d.sub(vecP3, vecP1)); // dot(ex, P3 - P1);

		Vector3d helpEy = Vector3d.sub(Vector3d.sub(vecP3, vecP1), Vector3d.mult(ex, i));
		Vector3d ey = Vector3d.div(helpEy, helpEy.magnitude());// (P3 - P1 - i * ex) / (numpy.linalg.norm(P3 - P1 - i * ex));
		Vector3d ez = ex.cross(ey);
		double d = Vector3d.sub(vecP2, vecP1).magnitude();
		double j = ey.dot(Vector3d.sub(vecP3, vecP1));

		System.out.println(ex);
		System.out.println(ey);
		System.out.println(ez);

		// trilaterate the position of the intersection
		double x = (Math.pow(distanceToA, 2) - Math.pow(distanceToB, 2) + Math.pow(d, 2)) / (2 * d);
		double y = ((Math.pow(distanceToA, 2) - Math.pow(distanceToC, 2) + Math.pow(i, 2) + Math.pow(j, 2)) / (2 * j)) - ((i * x) / j);

		System.out.println("x: " + x);
		System.out.println("y: " + y);

		// only one case shown here
		double z = Math.sqrt(Math.pow(distanceToA, 2) - Math.pow(x, 2) - Math.pow(y, 2));
		System.out.println("z: " + z);

		System.out.println(Math.pow(distanceToA, 2));
		System.out.println(Math.pow(x, 2));
		System.out.println(Math.pow(y, 2));

		Vector3d trilateratedPoint = Vector3d.add(Vector3d.add(Vector3d.add(vecP1, Vector3d.mult(ex, x)), Vector3d.mult(ey, y)), Vector3d.mult(ez, z));

		System.out.println("tri: " + trilateratedPoint);

		// convert the ECEF point back to lat, long
		double lat = Math.toDegrees(Math.asin(trilateratedPoint.z / EARTH_RADIUS_IN_M));
		double lon = Math.toDegrees(Math.atan2(trilateratedPoint.y, trilateratedPoint.x));

		// if value is NaN
		if (lat != lat || lon != lon){
			return null;
		}
		
		return lat + "," + lon;
	}

}