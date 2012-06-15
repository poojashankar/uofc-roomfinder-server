package com.uofc.roomfinder.entities;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uofc.roomfinder.util.gson.AnnotationJsonDeserializer;
import com.uofc.roomfinder.util.gson.AnnotationJsonSerialzer;

/**
 * 
 * represents a data set of the table tbl_annotation
 * 
 * the JSON representation of this class should look like: { "id": "2821", "lat": "46.49396", "lng": "11.2088", "elevation": "1865", "title": "Gantkofel",
 * "distance": "9.771", "has_detail_page": "0", "webpage": "" },
 *  
 * @author lauteb 
 */
public class Annotation implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	// coords
	private String latitude;
	private String longitude;
	private String elevation;

	private String text;
	private String distance;
	private int has_detail_page;
	private String webpage;
	private Date timestamp;
	
	private String type;

	// constructor
	public Annotation() {
		webpage = "";
	}

	public Annotation(Long id, String latitude, String longitude, String elevation, String text, String distance, int has_detail_page, String webpage,
			Date timestamp, String type) {
		super();
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.elevation = elevation;
		this.text = text;
		this.distance = distance;
		this.has_detail_page = has_detail_page;
		this.webpage = webpage;
		this.timestamp = timestamp;
		this.type = type;
	}

	/**
	 * creates an instance with all data from the sql resultset
	 * 
	 * @param rs
	 * @throws SQLException
	 */
	public Annotation(ResultSet rs) throws SQLException {
		this();

		this.setId(rs.getLong("ID"));
		this.setText(rs.getString("TEXT"));
		this.setTimestamp(rs.getDate("TIMESTAMP"));
		this.setLatitude(rs.getString("LATITUDE"));
		this.setLongitude(rs.getString("LONGITUDE"));
		this.setElevation(rs.getString("ELEVATION"));
		this.setDistance(rs.getString("DISTANCE"));
		this.setHas_detail_page(rs.getInt("HAS_DETAIL_PAGE"));
		this.setWebpage(rs.getString("WEBPAGE"));
	}

	/**
	 * converts JSON string into an annotaion object
	 * 
	 * @param jsonString
	 *            JSON representation of object
	 */
	public Annotation(String jsonString) {
		this();

		// deserialze JSON String
		Gson gson = new GsonBuilder().registerTypeAdapter(Annotation.class, new AnnotationJsonDeserializer()).serializeNulls().create();
		Annotation newAnno = gson.fromJson(jsonString, Annotation.class);

		this.id = newAnno.getId();
		this.latitude = newAnno.getLatitude();
		this.longitude = newAnno.getLongitude();
		this.elevation = newAnno.getElevation();
		this.text = newAnno.getText();
		this.distance = newAnno.getDistance();
		this.has_detail_page = newAnno.getHas_detail_page();
		this.webpage = newAnno.getWebpage();
		this.timestamp = (newAnno.getTimestamp()==null)?null:newAnno.getTimestamp();
	}

	/**
	 * 
	 * @return JSON representation of object
	 */
	public String toJsonString() {
		Gson gson = new GsonBuilder().registerTypeAdapter(Annotation.class, new AnnotationJsonSerialzer()).setPrettyPrinting().serializeNulls().create();
		String json = gson.toJson(this);

		return json;
	}

	// getters & setters
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	/**
	 * String has a maximum length of 400 chars
	 * 
	 * @param text
	 */
	public void setText(String text) {
		if (text.length() <= 400) {
			this.text = text;
		} else {
			this.text = text.substring(0, 400);
		}
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getElevation() {
		return elevation;
	}

	public void setElevation(String elevation) {
		this.elevation = elevation;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public int getHas_detail_page() {
		return has_detail_page;
	}

	public void setHas_detail_page(int has_detail_page) {
		this.has_detail_page = has_detail_page;
	}

	public String getWebpage() {
		return webpage;
	}

	public void setWebpage(String webpage) {
		this.webpage = webpage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	
}
