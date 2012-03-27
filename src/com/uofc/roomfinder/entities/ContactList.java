package com.uofc.roomfinder.entities;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ContactList extends LinkedList<Contact> implements List<Contact>{

	private static final long serialVersionUID = 1L;

	/**
	 * default constructor
	 */
	public ContactList() {

	}
	
	/**
	 * deserializes a JSON string
	 * @param jSON_STRING
	 */
	public ContactList(String jsonString) {
		this();

		// deserialize JSON String
		Gson gson = new GsonBuilder().serializeNulls().create();
		List<Contact> newContactList = gson.fromJson(jsonString, new TypeToken<List<Contact>>(){}.getType());
		
		//add each object to list
		for (Contact newContact : newContactList){
			this.add(newContact);
		}
	}

	/**
	 * 
	 * @return  JSON representation of object
	 */
	public String toJsonString(){
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		String json = gson.toJson(this);

		return json;
	}
	
}
