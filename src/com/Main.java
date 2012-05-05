package com;

//import com.esri.arcgis.interop.AutomationException;
//import com.esri.arcgis.server.IServerContext;
//import com.esri.arcgis.server.IServerObjectManager;
//import com.esri.arcgis.server.ServerConnection;
//import com.esri.arcgis.system.ServerInitializer;


public class Main {

	public static void main(String args[]){
		
		String domain = "http://asebeast2.cpsc.ucalgary.ca:7000/ArcGIS/rest/services";
		String user = "";
		String password = "";
		
		//new ServerInitializer().initializeServer(domain, user, password);
		
		
//		try {
//			ServerConnection connection = new ServerConnection();
//			connection.connect("http://asebeast2.cpsc.ucalgary.ca:7000/ArcGIS/rest/services");
//			IServerObjectManager som = connection.getServerObjectManager();
//			IServerContext sc = som.createServerContext("", "");
//			
//		} catch (AutomationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		System.out.println("test");
		
	}
	
}
