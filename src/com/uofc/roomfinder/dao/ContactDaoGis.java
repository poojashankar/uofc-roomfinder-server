package com.uofc.roomfinder.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uofc.roomfinder.entities.Building;
import com.uofc.roomfinder.entities.Contact;
import com.uofc.roomfinder.entities.ContactList;
import com.uofc.roomfinder.util.UrlReader;

/**
 * 
 * @author lauteb
 */
public class ContactDaoGis implements ContactDAO {

	/**
	 * searches the ArcGIS server for buildings AND names
	 */
	@Override
	public ContactList findContacts(String searchString) {

		ContactList contacts = new ContactList();

		// then look if there is at least a room
		contacts.addAll(this.findRoomOnArcGisServer(searchString));

		return contacts;
	}

	private ContactList findRoomOnArcGisServer(String searchString) {
		// check form like ICT117
		String regex = "(?<=[\\w&&\\D])(?=\\d)";
		String building = null;
		String room = null;

		if (searchString.split(" ").length == 2) {
			building = searchString.split(" ")[0];
			room = searchString.split(" ")[1];
		} else {

			// split letters and numbers (ICT550 -> ICT, 550)
			if (searchString.split(regex).length == 2) {
				building = searchString.split(regex)[0];
				room = searchString.split(regex)[1];
			} else if (searchString.split(regex).length == 1 && searchString.replace(" ", "").split(regex).length == 2) {
				// split letters and numbers (ICT 550 -> ICT, 550)
				building = searchString.replace(" ", "").split(regex)[0];
				room = searchString.replace(" ", "").split(regex)[1];
			}
		}

		try {
			// get JSON from server parse it into JsonObject
			System.out.println("before search building: " + building + " room: " + room);
			String jsonString = this.getJsonFromArcGisLayer(building, room);
			JsonParser parser = new JsonParser();
			JsonObject jsonObject = (JsonObject) parser.parse(jsonString);

			ContactList contacts = new ContactList();
			ContactList explicitContacts = new ContactList(); // if result matches exactly

			int i = 0;
			try {
				while (true) {
					System.out.println("building: " + building + " room: " + room);
					// create contact list with one contact which has only this room number
					String foundRoom = jsonObject.get("features").getAsJsonArray().get(i).getAsJsonObject().get("attributes").getAsJsonObject()
							.get("SDE.DBO.Building_Room.RM_ID").getAsString();
					String foundBuilding = jsonObject.get("features").getAsJsonArray().get(i).getAsJsonObject().get("attributes").getAsJsonObject()
							.get("SDE.DBO.Building_Room.BLD_ID").getAsString();
					Contact contact = new Contact();
					List<String> roomList = new LinkedList<String>();
					roomList.add(foundBuilding + foundRoom);
					contact.setRoomNumber(roomList);

					contacts.add(contact);

					System.out.println("found " + foundBuilding + foundRoom + ", searched: " + building + room);
					
					// if it matches exactly add to explicit list
					if ((foundBuilding + foundRoom).contains((building + room).toUpperCase())){
						explicitContacts.add(contact);
					}
						

					i++;
				}
			} catch (Exception e) {
			}

			System.out.println("explicit: " + explicitContacts.size() + "   normal: " + contacts.size());
			
			// if there are good matches in explicit list, return that
			if (explicitContacts.size() > 0)
				return explicitContacts;
			else
				return contacts;
			
		} catch (Exception ex) {
			// if there is no match on server -> return empty list
			return new ContactList();
		}
	}

	/**
	 * helper method for accessing the REST service of the ArcGIS building MapServer
	 * 
	 * @param whereClause
	 * @return
	 */
	private String getJsonFromArcGisLayer(String building, String room) {

		final String ARCGIS_BUILDING_MAPSERVER = "http://136.159.24.32/ArcGIS/rest/services/Rooms/Rooms/MapServer/";
		final String ARCGIS_BUILDING_LAYER = "111";
		final String ARCGIS_BUILDING_RETURN_FIELDS = "SDE.DBO.Building_Room.RM_ID,+SDE.DBO.Building_Room.BLD_ID";
		final String ARCGIS_RETURN_GEOMETRY = "false";

		String whereClause = "SDE.DBO.Building_Room.RM_ID like '%" + room + "%' AND SDE.DBO.Building_Room.BLD_ID like '%" + building + "%'";

		// build up URL
		String queryUrl = ARCGIS_BUILDING_MAPSERVER + ARCGIS_BUILDING_LAYER + "/";
		queryUrl += "query?text=&geometry=&geometryType=esriGeometryPoint&inSR=&spatialRel=esriSpatialRelIntersects&relationParam=&objectIds=&time=&returnCountOnly=false&returnIdsOnly=false&returnGeometry="
				+ ARCGIS_RETURN_GEOMETRY + "&maxAllowableOffset=&outSR=";
		queryUrl += "&where=" + whereClause;
		queryUrl += "&outFields=" + ARCGIS_BUILDING_RETURN_FIELDS;
		queryUrl += "&f=pjson";

		return UrlReader.readFromURL(queryUrl);
	}

	/**
	 * no names in ArcGIS server database
	 */
	@Override
	public ContactList findContactsByName(String name) {

		return null;

	}

	@Override
	public ContactList findContactsBuildingAndRoom(String buildingAndRoom) {
		return this.findContacts(buildingAndRoom);
	}

}
