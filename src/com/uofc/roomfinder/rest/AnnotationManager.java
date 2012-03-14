package com.uofc.roomfinder.rest;

import java.util.List;
import java.util.Vector;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uofc.roomfinder.dao.AnnotationDAO;
import com.uofc.roomfinder.dao.AnnotationDAOImpl;
import com.uofc.roomfinder.entities.Annotation;
import com.uofc.roomfinder.entities.AnnotationPackage;
import com.uofc.roomfinder.entities.Coordinate;
import com.uofc.roomfinder.util.gson.AnnotationJsonSerialzer;

@Path("/annotation")
public class AnnotationManager {

	AnnotationDAO annotationDao = new AnnotationDAOImpl();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/id/{annotationId}")
	public String getAnnotationById(@PathParam("annotationId") long annotationId) {
		// get annotation from db
		Annotation annotation = annotationDao.findByID(annotationId);

		// get json representation ob object
		return annotation.toJsonString();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ids/{annotationIds}")
	public String getAnnotationByIds(
			@PathParam("annotationIds") String annotationIds) {
		System.out.println(annotationIds);

		// Split into long array
		String[] arrAnnotationIds = annotationIds.split(",");
		System.out.println(arrAnnotationIds[0] + " - length: "
				+ arrAnnotationIds.length);

		Vector<Long> vecLong = new Vector<Long>();
		for (String annoId : arrAnnotationIds) {
			vecLong.add(Long.parseLong(annoId));
			System.out.println(Long.getLong(annoId));
		}
		Long[] arrLong = new Long[vecLong.size()];
		vecLong.toArray(arrLong);

		// get annotation package
		AnnotationPackage annotationPackage = annotationDao.findByIds(arrLong);

		return annotationPackage.toJsonString();
	}

}
