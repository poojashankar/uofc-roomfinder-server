
<%@page import="com.esri.arcgisws.NAStreetDirection"%>
<%@page import="com.esri.arcgisws.NAStreetDirections"%>
<%@page import="com.esri.arcgisws.PointN"%>
<%@page import="com.esri.arcgisws.Point"%>
<%@page import="com.esri.arcgisws.PolylineN"%>
<%@page import="com.esri.arcgisws.NAServerRouteResults"%>
<%@page import="com.esri.arcgisws.PropertySetProperty"%>
<%@page import="com.esri.arcgisws.NAServerPropertySets"%>
<%@page import="com.esri.arcgisws.NAServerLocations"%>
<%@page import="com.esri.arcgisws.PropertySet"%>
<%@page import="com.esri.arcgisws.NAServerRouteParams"%>
<%@page import="com.esri.arcgisws.EsriNAServerLayerType"%>
<%@page import="com.esri.arcgisws.GetNALayerNames"%>
<%@page import="com.esri.arcgisws.NAServerSolverResults"%>
<%@page import="com.esri.arcgisws.NAServerSolverParams"%>
<%@page import="com.esri.arcgisws.NAServerBindingStub"%>
<%@page import="com.esri.arcgisws.MapServerBindingStub"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
		String naServerEndPoint = "http://136.159.24.32/ArcGIS/services/UC_NETWORK/MapServer/NAServer";
		NAServerBindingStub naServer = new NAServerBindingStub(naServerEndPoint);

		String routeLayerName = naServer.getNALayerNames(EsriNAServerLayerType.esriNAServerRouteLayer)[0];

		out.println(routeLayerName);
		out.println(naServer.getNALayerNames(EsriNAServerLayerType.esriNAServerRouteLayer).length + "<br>");

		NAServerRouteParams routeParams = new NAServerRouteParams();
		NAServerSolverParams solverParams = naServer.getSolverParameters(routeLayerName);

		routeParams = (NAServerRouteParams) solverParams;
		routeParams.setReturnRouteGeometries(true);
		routeParams.setReturnDirections(true);

		out.println(solverParams.getImpedanceAttributeName() + "<br>");

		PropertySet[] propSets = new PropertySet[2];

		//start prop
		PropertySetProperty prop11 = new PropertySetProperty("x", "701192.8861");
		PropertySetProperty prop12 = new PropertySetProperty("y", "5662659.7696");
		PropertySetProperty prop13 = new PropertySetProperty("z", "0");
		//PropertySetProperty prop13 = new PropertySetProperty("name", "Stop 1");

		PropertySetProperty[] propArr1 = { prop11, prop12, prop13 };

		propSets[0] = new PropertySet();
		propSets[0].setPropertyArray(propArr1);

		//end prop
		PropertySetProperty prop21 = new PropertySetProperty("x", "701012.8757");
		PropertySetProperty prop22 = new PropertySetProperty("y", "5662665.3092");
		PropertySetProperty prop23 = new PropertySetProperty("z", "16");
		//PropertySetProperty prop23 = new PropertySetProperty("name", "Stop 2");

		PropertySetProperty[] propArr2 = { prop21, prop22, prop23 };

		propSets[1] = new PropertySet();
		propSets[1].setPropertyArray(propArr2);

		NAServerPropertySets stopsPropSets = new NAServerPropertySets();
		stopsPropSets.setPropertySets(propSets);

		routeParams.setStops(stopsPropSets);

		NAServerRouteResults routeResult = (NAServerRouteResults) naServer.solve(routeParams);

		out.println(routeResult.getSolveMessages().getGPMessages().length);
		out.println("<br>");

		//get path geometries
		PolylineN resultLine = (PolylineN) routeResult.getRouteGeometries()[0];

		out.println(resultLine.getPathArray()[0].getPointArray().length);

		PointN first = (PointN) resultLine.getPathArray()[0].getPointArray()[0];

		for (Point point : resultLine.getPathArray()[0].getPointArray()) {
			PointN pointN = (PointN) point;
			out.println("x: " + pointN.getX() + ", y: " + pointN.getY() + ", z: " + pointN.getZ() + "<br>");
		}

		//get path directions
		NAStreetDirection[] directions = routeResult.getDirections()[0].getDirections();

		out.println(directions.length);

		//SrouteResult.getDirections()[0].getDirections()[0].getStringTypes()[0].

		for (NAStreetDirection direction : directions) {
			//PointN pointN = (PointN) point;
			for (int i = 0; i < direction.getStrings().length; i++)
				out.println(direction.getStrings()[i] + " - " + direction.getStringTypes()[i] + "<br>");
		}
	%>
</body>
</html>