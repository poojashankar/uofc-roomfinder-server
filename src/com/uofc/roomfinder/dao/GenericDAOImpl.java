package com.uofc.roomfinder.dao;

import java.io.Serializable;

/**
 * 
 * @author lauteb
 */
public abstract class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

	// protected Session getSession() {
	// // return HibernateUtil.getSession();
	// }

	@Override
	public void save(T entity) {
		// Session hibernateSession = this.getSession();
		// hibernateSession.saveOrUpdate(entity);
	}

	public void merge(T entity) {
		// Session hibernateSession = this.getSession();
		// hibernateSession.merge(entity);
	}

	@Override
	public void delete(T entity) {
		// Session hibernateSession = this.getSession();
		// hibernateSession.delete(entity);
	}

	// public List<T> findMany(Query query) {
	// List<T> t;
	// t = (List<T>) query.list();
	// return t;
	// }
	//
	// public T findOne(Query query) {
	// T t;
	// t = (T) query.uniqueResult();
	// return t;
	// }

	// public T findByID(Class clazz, BigDecimal id) {
	// // Session hibernateSession = this.getSession();
	// // T t = null;
	// // t = (T) hibernateSession.get(clazz, id);
	// // return t;
	// return null;
	// }
	//
	// public List findAll(Class clazz) {
	// // Session hibernateSession = this.getSession();
	// // List T = null;
	// // Query query = hibernateSession.createQuery("from " + clazz.getName());
	// // T = query.list();
	// // return T;
	// return null;
	// }
}
