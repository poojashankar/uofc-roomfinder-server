package com.uofc.roomfinder.rest;

import java.util.Date;
import java.util.Vector;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.uofc.roomfinder.dao.AnnotationDAO;
import com.uofc.roomfinder.dao.AnnotationDaoMySQL;
import com.uofc.roomfinder.dao.BuildingDAOMySQL;
import com.uofc.roomfinder.entities.Annotation;
import com.uofc.roomfinder.entities.AnnotationList;

@Path("/annotation")
public class ArDataManager {

	AnnotationDAO annotationDao = new AnnotationDaoMySQL();

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
	@Path("/withdata")
	public String getAnnotationWithPredefinedData(@QueryParam("x") String x, @QueryParam("y") String y, @QueryParam("z") String z,
			@QueryParam("text") String text) {
		// get annotation from db
		Annotation annotation = new Annotation(0l, y, x, z, text, "", 0, null, new Date());

		AnnotationList annotationPackage = new AnnotationList();
		annotationPackage.addAnnotation(annotation);

		// get json representation ob object
		return annotationPackage.toJsonString();
	}

	/**
	 * returns JSON for next way point and destination way point
	 * 
	 * future work: get out of db, instead of passing params from client
	 * 
	 * @param next_x
	 * @param next_y
	 * @param next_z
	 * @param dest_x
	 * @param dest_y
	 * @param dest_z
	 * @param next_text
	 * @param dest_text
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/navigation")
	public String getNavigationAnnotation(@QueryParam("next_x") String next_x, @QueryParam("next_y") String next_y, @QueryParam("next_z") String next_z,
			@QueryParam("dest_x") String dest_x, @QueryParam("dest_y") String dest_y, @QueryParam("dest_z") String dest_z,
			@QueryParam("next_text") String next_text, @QueryParam("dest_text") String dest_text) {

		System.out.println(dest_text);

		// next waypoint annotation
		Annotation next_annotation = new Annotation(3989l, next_y, next_x, "" + 0, next_text, "", 0, null, new Date());
		next_annotation.setType("POI");

		//longitude has to be slightly different... otherwise marker would not get added because its to similar...
		Annotation arrow_annotation = new Annotation(2121212l, next_y, ""+ (Double.parseDouble(next_x)+0.00000001), "" + 0, next_text, "", 0, null, new Date());
		next_annotation.setType("ARROW");

		Annotation dest_annotation = new Annotation(13332l, dest_y, dest_x, "" + 0, dest_text.replace("Route to ", ""), "", 0, null, new Date());
		dest_annotation.setType("SPECIAL_POI");

		AnnotationList annotationPackage = new AnnotationList();
		annotationPackage.addAnnotation(next_annotation);
		annotationPackage.addAnnotation(arrow_annotation);
		annotationPackage.addAnnotation(dest_annotation);

		// get json representation ob object
		return annotationPackage.toJsonString();
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

		AnnotationList annotationList = new AnnotationList();

		if (category.equals("buildings")) {
			annotationList = new BuildingDAOMySQL().getAllBuildingsAsAnnotationList();
		} else {

			// get annotation package
			annotationList = annotationDao.findByCategory(category);

		}

		// and return it as JSON
		return annotationList.toJsonString();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/cat/friends/{user_name}")
	public String getAnnotationByIds() {

		// get annotation package
		AnnotationList annotationPackage = new AnnotationList();

		Annotation anno = new Annotation(1l, "51.078670", "-114.130440", "0", "Patrick", "distance", 0, "", new Date(), "FRIEND");
		annotationPackage.addAnnotation(anno);

		anno = new Annotation(2l, "51.079876", "-114.125408", "4", "Arlo", "distance", 0, "", new Date(), "FRIEND");
		annotationPackage.addAnnotation(anno);

		anno = new Annotation(3l, "51.079949", "-114.127964", "20", "Daniel", "distance", 0, "", new Date(), "FRIEND");
		annotationPackage.addAnnotation(anno);
		
		anno = new Annotation(4l, "51.078859", "-114.13171", "20", "Zabed", "distance", 0, "", new Date(), "FRIEND");
		annotationPackage.addAnnotation(anno);
		
		return annotationPackage.toJsonString();
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/add/graffiti")
	public String saveGraffiti(@QueryParam("text") String text, @QueryParam("user") String user, @QueryParam("lat") String lat, @QueryParam("long") String lng) {

		Annotation newAnno = new Annotation();
		newAnno.setText(text + "--" + user);
		newAnno.setLongitude(lng);
		newAnno.setLatitude(lat);
		newAnno.setElevation("0");
		newAnno.setDistance("0");
		newAnno.setHas_detail_page(0);
		newAnno.setWebpage("");
		newAnno.setType("GRAFFITI");

		System.out.println(new AnnotationDaoMySQL().save(newAnno));

		return "successful";
	}

}
