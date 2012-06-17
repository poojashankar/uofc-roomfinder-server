package com.uofc.roomfinder.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.esri.arcgisws.EsriNAServerLayerType;
import com.esri.arcgisws.NAServerBindingStub;
import com.esri.arcgisws.NAServerLocations;
import com.esri.arcgisws.NAServerPropertySets;
import com.esri.arcgisws.NAServerRouteParams;
import com.esri.arcgisws.NAServerRouteResults;
import com.esri.arcgisws.NAServerSolverParams;
import com.esri.arcgisws.NAStreetDirection;
import com.esri.arcgisws.Point;
import com.esri.arcgisws.PointN;
import com.esri.arcgisws.PolylineN;
import com.esri.arcgisws.PropertySet;
import com.esri.arcgisws.PropertySetProperty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uofc.roomfinder.entities.Point3D;
import com.uofc.roomfinder.entities.routing.Route;
import com.uofc.roomfinder.entities.routing.RouteFeature;
import com.uofc.roomfinder.entities.routing.RouteStopAttributes;
import com.uofc.roomfinder.entities.routing.RouteStopFeature;
import com.uofc.roomfinder.util.Constants;
import com.uofc.roomfinder.util.gson.RouteJsonSerializer;

@Path("/route")
public class RouteManager {

	private static final int MAX_ROUTE_MODIFICATIONS = 10;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAnnotationById(@QueryParam("x1") double x1, @QueryParam("y1") double y1, @QueryParam("z1") double z1, @QueryParam("x2") double x2,
			@QueryParam("y2") double y2, @QueryParam("z2") double z2, @QueryParam("impedance") String impedance) {

		// test points
		// RoutePoint start = new RoutePoint(701192.8861, 5662659.7696, 16.0);
		// RoutePoint end = new RoutePoint(701012.8757, 5662665.3092, 16.0);

		// create points from params
		Point3D start = new Point3D(x1, y1, z1);
		Point3D end = new Point3D(x2, y2, z2);

		// get route

		// workaround to get the route always starting at the correct layer
		Route newRoute = getSoapRoute(start, end, impedance);

		double zToleranceUp = start.getZ() + 1;
		double zToleranceDown = start.getZ() - 1;
		int routeCounter = 1;

		// if the z coordinate is not in the tolerance -> modify x,y and try again
		while ((newRoute.getPath().get(0).getZ() > zToleranceUp || newRoute.getPath().get(0).getZ() < zToleranceDown) && routeCounter < MAX_ROUTE_MODIFICATIONS) {
			// produces -1, 1, -2, 2, ....
			double alternatingOffset = Math.pow(-1, routeCounter) * ((routeCounter + 1) / 2);

			System.out.println("alternate: " + alternatingOffset);
			Point3D newStart = new Point3D(start.getX() + alternatingOffset, start.getY() + alternatingOffset, start.getZ());
			newRoute = getSoapRoute(newStart, end, impedance);
			routeCounter++;
		}

		// if route counter = 10, modifying search criteria was not successfull
		// fall back to first route
		if ((newRoute.getPath().get(0).getZ() > zToleranceUp || newRoute.getPath().get(0).getZ() < zToleranceDown) && routeCounter == MAX_ROUTE_MODIFICATIONS) {
			newRoute = getSoapRoute(start, end, impedance);
		}

		// convert it to JSON
		Gson gson = new GsonBuilder().registerTypeAdapter(Route.class, new RouteJsonSerializer()).setPrettyPrinting().serializeNulls().create();
		String json = gson.toJson(newRoute);

		// return JSON route
		return json;
	}

	/**
	 * this method accesses the ArcGIS SOAP web service for routing (NAServer)
	 * 
	 * @param start
	 * @param destination
	 * @param impedanceAttribute
	 * @return
	 */
	public static Route getSoapRoute(Point3D start, Point3D destination, String impedanceAttribute) {

		Route newRoute = new Route();

		// connect to NA SOAP service
		NAServerBindingStub naServer = new NAServerBindingStub(Constants.NA_SERVER_SOAP_URL);
		String routeLayerName = naServer.getNALayerNames(EsriNAServerLayerType.esriNAServerRouteLayer)[0];

		System.out.println(routeLayerName);
		// System.out.println(naServer.getNALayerNames(EsriNAServerLayerType.esriNAServerRouteLayer).length + "<br>");

		// get default routing params from server
		NAServerSolverParams solverParams = naServer.getSolverParameters(routeLayerName);
		NAServerRouteParams routeParams = new NAServerRouteParams();

		// edit route params
		routeParams = (NAServerRouteParams) solverParams;
		routeParams.setReturnRouteGeometries(true);
		routeParams.setReturnDirections(true);
		routeParams.setImpedanceAttributeName(impedanceAttribute);

		// add stop features
		newRoute.getStops().getFeatures().add(new RouteStopFeature(start, new RouteStopAttributes("Start", newRoute.getRouteName())));
		newRoute.getStops().getFeatures().add(new RouteStopFeature(destination, new RouteStopAttributes("Destination", newRoute.getRouteName())));

		// add stops to route params
		routeParams.setStops(getStopsAsPropSet(newRoute));

		// SOLVE route
		NAServerRouteResults routeResult = (NAServerRouteResults) naServer.solve(routeParams);

		// get path geometries
		PolylineN resultLine = (PolylineN) routeResult.getRouteGeometries()[0];

		System.out.println("number of routes: " + routeResult.getRouteGeometries().length);

		// iterate all path points
		for (Point point : resultLine.getPathArray()[0].getPointArray()) {
			PointN pointN = (PointN) point;

			newRoute.getPath().add(new Point3D(pointN.getX(), pointN.getY(), pointN.getZ()));
			System.out.println("x: " + pointN.getX() + ", y: " + pointN.getY() + ", z: " + pointN.getZ() + "<br>");
		}

		// get path directions
		// text messages and segment lengths
		NAStreetDirection[] directions = routeResult.getDirections()[0].getDirections();
		double cumulativeLength = 0.0;

		// each directions
		for (NAStreetDirection direction : directions) {
			RouteFeature currentRF = new RouteFeature();

			// each text per direction
			for (int i = 0; i < direction.getStrings().length; i++) {
				if (direction.getStringTypes()[i].toString().equals("esriDSTLength")) {
					// segment length

					// try to convert string to double
					try {
						double tmp = Double.parseDouble(direction.getStrings()[i].split(" ")[0]);
						currentRF.setLength(tmp);
					} catch (NumberFormatException nfe) {
						// do nothing, default is 0
					}
				} else if (direction.getStringTypes()[i].toString().equals("esriDSTGeneral")) {
					// routing text info
					currentRF.setText(direction.getStrings()[i]);
				}
			}

			// add feature to list
			newRoute.getRouteFeatures().add(currentRF);

			// sum up length
			cumulativeLength += currentRF.getLength();
		}
		newRoute.setLength(cumulativeLength);

		return newRoute;
	}

	/**
	 * helper method to create prop set
	 * 
	 * @param route
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static NAServerLocations getStopsAsPropSet(Route route) {
		// add stops
		PropertySet[] propSets = new PropertySet[2];

		// start point
		PropertySetProperty prop11 = new PropertySetProperty("x", route.getStops().getFeatures().get(0).getGeometry().getX());// "701012.8757");
		PropertySetProperty prop12 = new PropertySetProperty("y", route.getStops().getFeatures().get(0).getGeometry().getY());// "5662665.3092");
		PropertySetProperty prop13 = new PropertySetProperty("z", route.getStops().getFeatures().get(0).getGeometry().getZ());// "16");

		// System.out.println("value" + prop13.getValue());

		PropertySetProperty[] propArr1 = { prop11, prop12, prop13 };
		propSets[0] = new PropertySet();
		propSets[0].setPropertyArray(propArr1);

		// end point
		PropertySetProperty prop21 = new PropertySetProperty("x", route.getStops().getFeatures().get(1).getGeometry().getX());// "701012.8757");
		PropertySetProperty prop22 = new PropertySetProperty("y", route.getStops().getFeatures().get(1).getGeometry().getY());// "5662665.3092");
		PropertySetProperty prop23 = new PropertySetProperty("z", route.getStops().getFeatures().get(1).getGeometry().getZ());// "16");

		PropertySetProperty[] propArr2 = { prop21, prop22, prop23 };
		propSets[1] = new PropertySet();
		propSets[1].setPropertyArray(propArr2);

		// add props to prop set
		NAServerPropertySets stopsPropSets = new NAServerPropertySets();
		stopsPropSets.setPropertySets(propSets);
		return stopsPropSets;
	}
}