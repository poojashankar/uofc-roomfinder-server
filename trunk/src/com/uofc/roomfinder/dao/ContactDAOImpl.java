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

import com.uofc.roomfinder.entities.Contact;

/**
 * 
 * @author lauteb
 */
public class ContactDAOImpl implements ContactDAO {

	final static String LDAP_SERVER_NAME = "directory.ucalgary.ca";
	final static String ROOT_CONTEXT = "o=ucalgary.ca Scope=LDAP_SCOPE_SUBTREE";

	/**
	 * searches the LDAP directory for entries (field: common name)
	 */
	@Override
	public List<Contact> findContactsByName(String searchString) {
		List<Contact> contacts = new LinkedList<Contact>();

		// build search string
		StringBuilder searchStringbuilder = new StringBuilder("cn=*");
		String[] splittedSearchString = searchString.split(" ");

		for (String singleSearchString : splittedSearchString) {
			searchStringbuilder.append(singleSearchString + "*");
		}

		// search
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
	public List<Contact> findContactsBuildingAndRoom(String searchString) {
		List<Contact> contacts = new LinkedList<Contact>();

		// build search string
		StringBuilder searchStringbuilder = new StringBuilder("roomNumber=*");
		String[] splittedSearchString = searchString.split(" ");

		for (String singleSearchString : splittedSearchString) {
			searchStringbuilder.append(singleSearchString + "*");
		}

		// search
		contacts = searchLdap4Contacts(searchStringbuilder.toString());

		// if no match found check search string the other way
		if (contacts.size() == 0 && splittedSearchString.length > 1) {
			contacts = this.searchLdap4Contacts("roomNumber=*" + splittedSearchString[1] + "*" + splittedSearchString[0] + "*");
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
	private List<Contact> searchLdap4Contacts(String searchString) {
		List<Contact> contacts = new LinkedList<Contact>();

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
			System.err.println(e);
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
		newContact.setCommonName((String) attribs.get("cn").get(0));
		newContact.setSurName((String) attribs.get("sn").get(0));
		newContact.setPreName((String) attribs.get("givenName").get(0));

		// add phone numbers
		NamingEnumeration<?> values = ((BasicAttribute) attribs.get("telephoneNumber")).getAll();
		while (values.hasMore()) {
			newContact.getTelephoneNumbers().add(values.next().toString());
		}

		// add mail addresses
		values = ((BasicAttribute) attribs.get("mail")).getAll();
		while (values.hasMore()) {
			newContact.getEmails().add(values.next().toString());
		}

		// add room numbers
		values = ((BasicAttribute) attribs.get("roomNumber")).getAll();
		while (values.hasMore()) {
			newContact.getRoomNumber().add(values.next().toString());
		}

		// add departments
		values = ((BasicAttribute) attribs.get("department")).getAll();
		while (values.hasMore()) {
			newContact.getDepartments().add(values.next().toString());
		}
	}
}
