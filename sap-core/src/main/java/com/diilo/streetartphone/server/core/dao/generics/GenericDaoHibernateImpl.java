package com.diilo.streetartphone.server.core.dao.generics;

import java.io.Serializable;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public abstract class GenericDaoHibernateImpl<T, PK extends Serializable> extends HibernateDaoSupport implements
		GenericDao<T, PK> {

	private final Class<T>	type;
	private final String	findAllQuery;

	public GenericDaoHibernateImpl(Class<T> type) {
		this.type = type;
		findAllQuery = "From " + type.getName();
	}

	@Override
	public void update(T o) {
		getHibernateTemplate().update(o);
		getHibernateTemplate().flush();
	}

	@Override
	public void delete(T o) {
		getHibernateTemplate().delete(o);
		getHibernateTemplate().flush();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return getHibernateTemplate().find(findAllQuery);
	}

	@Override
	public T get(PK id) {
		return getHibernateTemplate().get(type, id);
	}

	@Override
	public T load(PK id) {
		return getHibernateTemplate().load(type, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public PK save(T newInstance) {
		PK pk = (PK) getHibernateTemplate().save(newInstance);
		getHibernateTemplate().flush();
		return pk;
	}

	@Override
	public void saveOrUpdate(T transientObject) {
		getHibernateTemplate().saveOrUpdate(transientObject);
		getHibernateTemplate().flush();
	}

}