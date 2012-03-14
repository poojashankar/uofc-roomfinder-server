package com.uofc.roomfinder.entities;

import java.util.LinkedList;

import com.google.gson.annotations.Expose;

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
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
