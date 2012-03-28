package com;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uofc.roomfinder.entities.Annotation;
import com.uofc.roomfinder.entities.AnnotationList;
import com.uofc.roomfinder.util.gson.AnnotationJsonDeserializer;

public class Main {

	public static void main(String args[]){
		String JSON_TEST = "{ " +
				"  \"status\": \"OK\", " +
				"  \"num_results\": 2, " +
				"  \"results\": [ " +
				"    { " +
				"      \"id\": \"165\", " +
				"      \"lat\": \"y\", " +
				"      \"lng\": \"x\", " +
				"      \"elevation\": \"z\", " +
				"      \"title\": \"test_SAVE_TEST\", " +
				"      \"distance\": \"0\", " +
				"      \"has_detail_page\": \"0\", " +
				"      \"webpage\": \"www.google.de\", " +
				"      \"timestamp\": \"14/Mar/2012 00:00:00\" " +
				"    }, " +
				"    { " +
				"      \"id\": \"166\", " +
				"      \"lat\": \"y\", " +
				"      \"lng\": \"x\", " +
				"      \"elevation\": \"z\", " +
				"      \"title\": \"test_ID_TEST1\", " +
				"      \"distance\": \"0\", " +
				"      \"has_detail_page\": \"0\", " +
				"      \"webpage\": \"www.google.de\", " +
				"      \"timestamp\": \"14/Mar/2012 00:00:00\" " +
				"    } " +
				"  ] " +
				"}";
		
	    Gson gson = new GsonBuilder().registerTypeAdapter(Annotation.class, new AnnotationJsonDeserializer()).serializeNulls().create();
	    AnnotationList annoPackage = gson.fromJson(JSON_TEST, AnnotationList.class);
	    System.out.println(annoPackage.getResults().get(0).getWebpage());
		
	}
	
}
