package com.uofc.roomfinder.rest;

import java.util.Vector;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.uofc.roomfinder.dao.AnnotationDAO;
import com.uofc.roomfinder.dao.AnnotationDAOImpl;
import com.uofc.roomfinder.entities.Annotation;
import com.uofc.roomfinder.entities.AnnotationList;

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
	public String getAnnotationByIds(@PathParam("annotationIds") String annotationIds) {

		// Split into long array
		String[] arrAnnotationIds = annotationIds.split(",");

		Vector<Long> vecLong = new Vector<Long>();
		for (String annoId : arrAnnotationIds) {
			vecLong.add(Long.parseLong(annoId));
		}
		Long[] arrLong = new Long[vecLong.size()];
		vecLong.toArray(arrLong);

		// get annotation package
		AnnotationList annotationPackage = annotationDao.findByIds(arrLong);

		return annotationPackage.toJsonString();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/cat/{category}")
	public String getAnnotationByCategory(@PathParam("category") String category) {
		// get annotation package
		AnnotationList annotationPackage = annotationDao.findByCategory(category);

		// and return it as JSON
		return annotationPackage.toJsonString();
	}

}
