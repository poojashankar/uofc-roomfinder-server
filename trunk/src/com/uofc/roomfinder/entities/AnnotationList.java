package com.uofc.roomfinder.entities;

import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uofc.roomfinder.util.gson.AnnotationJsonDeserializer;
import com.uofc.roomfinder.util.gson.AnnotationJsonSerialzer;

public class AnnotationList {
	private final static String STATUS_OK = "OK";
		
	private String status;
	private int num_results;
	private LinkedList<Annotation> results;
	
	//constructor
	public AnnotationList(){
		super();
		results = new LinkedList<Annotation>();
		status = STATUS_OK;		
	}
	
	/**
	 * converts JSON string into an annotation object
	 * 
	 * @param jsonString
	 *            JSON representation of object
	 */
	public AnnotationList(String jsonString) {
		this();

		// deserialize JSON String
		Gson gson = new GsonBuilder().registerTypeAdapter(Annotation.class, new AnnotationJsonDeserializer()).serializeNulls().create();
		AnnotationList newAnnoPackage = gson.fromJson(jsonString, AnnotationList.class);

		this.num_results = newAnnoPackage.getNum_results();
		this.status = newAnnoPackage.getStatus();
		
		for(Annotation anno : newAnnoPackage.getResults()){
			this.addAnnotation(anno);
		}
		
	}
	
	//getters & setters
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getNum_results() {
		return num_results;
	}
	public LinkedList<Annotation> getResults() {
		return results;
	}
	
	/**
	 * adds an annotation to the result list (and updates the num_results value)
	 * @param annotation
	 */
	public void addAnnotation(Annotation annotation){
		this.results.add(annotation);
		this.num_results = this.results.size();
	}
	

	public String toJsonString() {
		Gson gson = new GsonBuilder().registerTypeAdapter(Annotation.class, new AnnotationJsonSerialzer()).setPrettyPrinting().serializeNulls().create();
		String json = gson.toJson(this);

		return json;
	}
	
	
}
