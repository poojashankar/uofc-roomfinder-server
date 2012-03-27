package com.uofc.roomfinder.entities;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * represents a data set of the public LDAP directory of the UofC
 * 
 * @author benjaminlautenschlaeger
 */
public class Contact {
	private String preName;
	private String surName;
	private String commonName;
	private List<String> emails;
	private List<String> telephoneNumbers;
	private List<String> roomNumber;
	private List<String> departments;

	/**
	 * default constructor
	 */
	public Contact() {
		emails = new LinkedList<String>();
		telephoneNumbers = new LinkedList<String>();
		roomNumber = new LinkedList<String>();
		departments = new LinkedList<String>();
	}

	/**
	 * deserializes a JSON string
	 * 
	 * @param jsonString
	 */
	public Contact(String jsonString) {
		this();

		// deserialze JSON String
		Gson gson = new GsonBuilder().serializeNulls().create();
		Contact newContact = gson.fromJson(jsonString, Contact.class);

		this.preName = newContact.getPreName();
		this.surName = newContact.getSurName();
		this.commonName = newContact.getCommonName();
		this.emails = newContact.getEmails();
		this.telephoneNumbers = newContact.getTelephoneNumbers();
		this.roomNumber = newContact.getRoomNumber();
		this.departments = newContact.getDepartments();
	}

	/**
	 * 
	 * @return JSON representation of object
	 */
	public String toJsonString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		String json = gson.toJson(this);

		return json;
	}

	// getter&setter
	public String getPreName() {
		return preName;
	}

	public void setPreName(String preName) {
		this.preName = preName;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	public List<String> getTelephoneNumbers() {
		return telephoneNumbers;
	}

	public void setTelephoneNumbers(List<String> telephoneNumbers) {
		this.telephoneNumbers = telephoneNumbers;
	}

	public List<String> getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(List<String> roomNumber) {
		this.roomNumber = roomNumber;
	}

	public List<String> getDepartments() {
		return departments;
	}

	public void setDepartments(List<String> departments) {
		this.departments = departments;
	}
}
