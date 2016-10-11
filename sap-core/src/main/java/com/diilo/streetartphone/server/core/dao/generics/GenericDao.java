package com.diilo.streetartphone.server.core.dao.generics;

import java.io.Serializable;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

public interface GenericDao<T, PK extends Serializable> {

	/** Persist the newInstance object into database */
	PK save(T newInstance);

	void saveOrUpdate(T transientObject);

	/**
	 * Retrieve an object that was previously persisted to the database using the indicated id as primary key
	 */
	T get(PK id);

	T load(PK id);

	List<T> findAll();

	/** Save changes made to a persistent object. */
	void update(T transientObject);

	/** Remove an object from persistent storage in the database */
	void delete(T persistentObject);

	HibernateTemplate getHibernateTemplate();

}