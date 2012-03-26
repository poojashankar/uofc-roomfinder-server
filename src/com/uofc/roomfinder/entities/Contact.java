package com.uofc.roomfinder.entities;

import java.util.LinkedList;
import java.util.List;

public class Contact {
	String preName;	
	String surName;
	String commonName;
	List<String> emails;
	List<String> telephoneNumbers;
	List<String> roomNumber;
	List<String> departments;
	
	//constructor
	public Contact(){
		emails = new LinkedList<String>();
		telephoneNumbers = new LinkedList<String>();
		roomNumber = new LinkedList<String>();
		departments = new LinkedList<String>();
	}
	
	
	//getter&setter
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
