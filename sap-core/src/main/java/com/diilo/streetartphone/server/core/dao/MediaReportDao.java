package com.diilo.streetartphone.server.core.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.diilo.streetartphone.server.core.dao.generics.GenericDaoHibernateImpl;
import com.diilo.streetartphone.server.core.entities.ReportBean;

@Repository
public class MediaReportDao extends GenericDaoHibernateImpl<ReportBean, Long> {

	@Autowired
	public MediaReportDao(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
		super(ReportBean.class);
		setSessionFactory(sessionFactory);
	}
}
