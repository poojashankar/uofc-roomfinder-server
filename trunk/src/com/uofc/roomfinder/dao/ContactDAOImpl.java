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
public class ContactDAOImpl implements ContactDAO {

	final static String LDAP_SERVER_NAME = "directory.ucalgary.ca";
	final static String ROOT_CONTEXT = "o=ucalgary.ca Scope=LDAP_SCOPE_SUBTREE";

	/**
	 * searches the LDAP directory for buildings AND names
	 */
	@Override
	public ContactList findContacts(String searchString) {

		ContactList contacts = new ContactList();

		// first try to search by name
		contacts.addAll(this.findContactsByName(searchString));

		// then try to search for a building with room
		contacts.addAll(this.findContactsBuildingAndRoom(searchString));

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
			System.out.println("building: " + building + " room: " + room);
			String jsonString = this.getJsonFromArcGisLayer(building, room);
			JsonParser parser = new JsonParser();
			JsonObject jsonObject = (JsonObject) parser.parse(jsonString);

			// create contact list with one contact which has only this room number
			room = jsonObject.get("features").getAsJsonArray().get(0).getAsJsonObject().get("attributes").getAsJsonObject().get("SDE.DBO.Building_Room.RM_ID")
					.getAsString();
			building = jsonObject.get("features").getAsJsonArray().get(0).getAsJsonObject().get("attributes").getAsJsonObject()
					.get("SDE.DBO.Building_Room.BLD_ID").getAsString();

			ContactList contacts = new ContactList();
			Contact contact = new Contact();
			List<String> roomList = new LinkedList<String>();
			roomList.add(building + room);
			contact.setRoomNumber(roomList);
			contacts.add(contact);
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
	 * searches the LDAP directory for entries (field: common name)
	 */
	@Override
	public ContactList findContactsByName(String searchString) {
		ContactList contacts = new ContactList();

		// build search string
		StringBuilder searchStringbuilder = new StringBuilder("cn=*");
		String[] splittedSearchString = searchString.split(" ");

		for (String singleSearchString : splittedSearchString) {
			searchStringbuilder.append(singleSearchString + "*");
		}

		// normal search
		contacts = searchLdap4Contacts(searchStringbuilder.toString());

		// if no match found check search string the other way
		if (contacts.size() == 0 && splittedSearchString.length > 1) {
			contacts = this.searchLdap4Contacts("cn=*" + splittedSearchString[1] + "*" + splittedSearchString[0] + "*");
		}

		return contacts;
	}

	/**
	 * searches the LDAP directory for entries (field: roomNumber)
	 */
	@Override
	public ContactList findContactsBuildingAndRoom(String searchString) {
		ContactList contacts = new ContactList();

		// build search string
		StringBuilder searchStringbuilder = new StringBuilder("roomNumber=*");
		String[] splittedSearchString = searchString.split(" ");

		for (String singleSearchString : splittedSearchString) {
			searchStringbuilder.append(singleSearchString + "*");
		}

		// normal search
		contacts = searchLdap4Contacts(searchStringbuilder.toString());

		// if no match found check search string the other way
		if (contacts.size() == 0 && splittedSearchString.length > 1) {
			contacts = this.searchLdap4Contacts("roomNumber=*" + splittedSearchString[1] + "*" + splittedSearchString[0] + "*");
		}

		// if still no match found, try to find building, get abbreviation, and then search again
		if (contacts.size() == 0 && splittedSearchString.length > 1) {
			String roomNumber = "";
			String building = "";

			for (String singleSearchString : splittedSearchString) {
				if (isNumeric(singleSearchString)) {
					roomNumber = singleSearchString;
				} else {
					building += singleSearchString + " ";
				}
			}

			BuildingDAO buildingDao = new BuildingDAOImpl();
			List<Building> foundBuildings = buildingDao.findBuildingsByName(building);

			for (Building foundBuilding : foundBuildings) {
				contacts = this.searchLdap4Contacts("roomNumber=*" + foundBuilding.getAbbreviation() + "*" + roomNumber + "*");
			}
		}

		return contacts;
	}

	/**
	 * set up environment to access the server
	 * 
	 * @return context for LDAP access
	 * @throws NamingException
	 */
	private DirContext getLdapContext() throws NamingException {
		Properties env = new Properties();

		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://" + LDAP_SERVER_NAME + "/" + ROOT_CONTEXT);

		return new InitialDirContext(env);
	}

	/**
	 * searches in the public UofC LDAP for contacts
	 * 
	 * @param searchString
	 *            given LDAP search query
	 */
	private ContactList searchLdap4Contacts(String searchString) {
		ContactList contacts = new ContactList();

		try {
			DirContext ctx = getLdapContext();

			// search in sub tree
			SearchControls ctrl = new SearchControls();
			ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);

			// search string
			NamingEnumeration<SearchResult> enumeration = ctx.search("", searchString, ctrl);

			// over all contacts
			while (enumeration.hasMore()) {
				SearchResult result = enumeration.next();
				Attributes attribs = result.getAttributes();

				// get infos and add it to result list
				Contact newContact = new Contact();
				createContactFromAttribs(newContact, attribs);
				contacts.add(newContact);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return contacts;
	}

	/**
	 * creates a contact from the LDAP attribs
	 * 
	 * @param newContact
	 * @param attribs
	 * @throws NamingException
	 */
	private void createContactFromAttribs(Contact newContact, Attributes attribs) throws NamingException {
		NamingEnumeration<?> values;

		if (attribs.get("cn") != null) {
			newContact.setCommonName((String) attribs.get("cn").get(0));
		}

		if (attribs.get("sn") != null) {
			newContact.setSurName((String) attribs.get("sn").get(0));
		}

		if (attribs.get("givenName") != null) {
			newContact.setPreName((String) attribs.get("givenName").get(0));
		}

		// add phone numbers (each contact can have several phone numbers)
		if (attribs.get("telephoneNumber") != null) {
			values = ((BasicAttribute) attribs.get("telephoneNumber")).getAll();
			while (values.hasMore()) {
				newContact.getTelephoneNumbers().add(values.next().toString());
			}
		}

		// add mail addresses
		if (attribs.get("mail") != null) {
			values = ((BasicAttribute) attribs.get("mail")).getAll();
			while (values.hasMore()) {
				newContact.getEmails().add(values.next().toString());
			}
		}

		// add room numbers
		if (attribs.get("roomNumber") != null) {
			values = ((BasicAttribute) attribs.get("roomNumber")).getAll();
			while (values.hasMore()) {
				newContact.getRoomNumber().add(values.next().toString());
			}
		}

		// add departments
		if (attribs.get("department") != null) {
			values = ((BasicAttribute) attribs.get("department")).getAll();
			while (values.hasMore()) {
				newContact.getDepartments().add(values.next().toString());
			}
		}
	}

	/**
	 * help method to check if the String is numeric
	 * 
	 * @param input
	 *            string to check
	 * @return true = numeric, false != numeric
	 */
	public boolean isNumeric(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
