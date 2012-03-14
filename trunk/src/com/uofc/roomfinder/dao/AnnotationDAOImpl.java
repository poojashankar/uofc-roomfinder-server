package com.uofc.roomfinder.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import com.uofc.roomfinder.entities.Annotation;
import com.uofc.roomfinder.entities.AnnotationPackage;
import com.uofc.roomfinder.entities.Coordinate;
import com.uofc.roomfinder.util.ConnectionFactory;

/**
 * 
 * @author lauteb
 */
public class AnnotationDAOImpl extends GenericDAOImpl<Annotation, Long>	implements AnnotationDAO {

	
	/**
	 * save the given annotation to DB
	 */
	@Override
	public void save(Annotation annotation) {

		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();

			// insert sql
			String sql = "INSERT INTO tbl_annotations (text, latitude, longitude, elevation, distance, has_detail_page, webpage)"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?)";

			// return generated keys -> to retrieve the autoincrement id
			prepStmt = conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);

			// every field
			prepStmt.setString(1, annotation.getText());
			prepStmt.setString(2, annotation.getLatitude());
			prepStmt.setString(3, annotation.getLongitude());
			prepStmt.setString(4, annotation.getElevation());
			prepStmt.setString(5, (annotation.getDistance()==null) ? "0" : annotation.getDistance());
			prepStmt.setInt(6, annotation.getHas_detail_page());
			prepStmt.setString(7, (annotation.getWebpage()==null) ? "0" : annotation.getWebpage());

			// exec stmt
			prepStmt.executeUpdate();

			// get last inserted auto increment id
			rs = prepStmt.getGeneratedKeys();
			if (rs.next()) {
				annotation.setId(rs.getLong(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(prepStmt);
			DbUtils.closeQuietly(conn);
		}
	}

	
	/**
	 * deletes the annotation by ID of the object
	 */
	@Override
	public void delete(Annotation annotation) {
		Connection conn = null;
		PreparedStatement prepStmt = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();

			// insert sql
			String sql = "DELETE FROM tbl_annotations WHERE id = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, annotation.getId());

			// exec stmt
			prepStmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtils.closeQuietly(prepStmt);
			DbUtils.closeQuietly(conn);
		}
	}

	
	/**
	 * returns a single Annotation with given ID
	 */
	public Annotation findByID(Long id) {

		Annotation annotation = null;

		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();

			// insert sql
			String sql = "SELECT * FROM tbl_annotations WHERE id = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, id);

			// exec stmt
			rs = prepStmt.executeQuery();

			// fill DTO
			if (rs.next()) {
				annotation = new Annotation(rs);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(prepStmt);
			DbUtils.closeQuietly(conn);
		}

		return annotation;
	}
	
	/**
	 * searches all annotations by given IDs
	 * 
	 * @returns an annotation package with all those annotations
	 */
	public AnnotationPackage findByIds(Long... ids) {
		System.out.println("length of IDs: " + ids.length);
		if (ids.length < 1)
			return null;
		
		Annotation annotation = null;
		AnnotationPackage annotationPackage = new AnnotationPackage();
		
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		
		//get IDs in a row for sql statement
		StringBuilder strBuilder = new StringBuilder();
		for(int i=0; i<ids.length ;i++){
			strBuilder.append("?,");
		}
		String strQuestionMarks = strBuilder.substring(0, strBuilder.length() -1);

		try {
			conn = ConnectionFactory.getInstance().getConnection();

			String sql = "SELECT * FROM tbl_annotations WHERE id IN ("+strQuestionMarks+")";
			System.out.println(sql);
			prepStmt = conn.prepareStatement(sql);
			
			//set every ID
			int i = 1;
			System.out.println("ID1: " + ids[0]);
			
			for(Long id : ids){
				prepStmt.setLong(i++, id);
			}

			// exec stmt
			rs = prepStmt.executeQuery();

			// fill DTO
			while (rs.next()) {
				annotation = new Annotation(rs);
				annotationPackage.addAnnotation(annotation);				
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(prepStmt);
			DbUtils.closeQuietly(conn);
		}

		return annotationPackage;
	}

	
	public List<Annotation> findNearbyAnnotations(Coordinate currentPosition) {
		// TODO Auto-generated method stub
		return null;
	}

}
