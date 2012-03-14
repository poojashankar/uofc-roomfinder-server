package com.uofc.roomfinder.entities;

import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.uofc.roomfinder.util.gson.AnnotationJsonDeserializer;
import com.uofc.roomfinder.util.gson.AnnotationJsonSerialzer;

public class AnnotationPackage {
	private final static String STATUS_OK = "OK";
	private final static String STATUS_FAIL = "FAIL";
		
	@Expose private String status;
	@Expose private int num_results;
	@Expose private LinkedList<Annotation> results;
	
	//constructor
	public AnnotationPackage(){
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
	public AnnotationPackage(String jsonString) {
		this();

		// deserialize JSON String
		Gson gson = new GsonBuilder().registerTypeAdapter(Annotation.class, new AnnotationJsonDeserializer()).serializeNulls().create();
		AnnotationPackage newAnnoPackage = gson.fromJson(jsonString, AnnotationPackage.class);

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
