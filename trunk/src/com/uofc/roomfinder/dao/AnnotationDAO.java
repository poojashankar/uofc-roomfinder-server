package com.uofc.roomfinder.dao;

import java.util.List;

import com.uofc.roomfinder.entities.Annotation;
import com.uofc.roomfinder.entities.AnnotationPackage;
import com.uofc.roomfinder.entities.Coordinate;

/**
 * 
 * @author lauteb
 */
public interface AnnotationDAO extends GenericDAO<Annotation, Long> {

	public Annotation findByID(Long id);

	public List<Annotation> findNearbyAnnotations(Coordinate currentPosition);

	public AnnotationPackage findByIds(Long... ids);

	public AnnotationPackage findByCategory(String category);

}
