package com.diilo.streetartphone.server.core.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.diilo.streetartphone.server.core.dao.generics.GenericDaoHibernateImpl;
import com.diilo.streetartphone.server.core.entities.MediaGradeBean;
import com.diilo.streetartphone.server.core.services.implementations.UserServiceImpl;

@Repository
public class MediaGradeDao extends GenericDaoHibernateImpl<MediaGradeBean, Long> {

	private static final Logger	LOG	= LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	public MediaGradeDao(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
		super(MediaGradeBean.class);
		setSessionFactory(sessionFactory);
	}

	/**
	 * Check if the give grade with a give picture
	 * 
	 * @param pictureGradeBean
	 * @return
	 */
	public boolean isAlreadyGraded(final String idPicture, final String email) {
		List<MediaGradeBean> list = getHibernateTemplate().find(
				"from MediaGradeBean mediaGrade where mediaGrade.idMedia=? and mediaGrade.email = ?", idPicture, email);
		return (list.isEmpty() == false);
	}
}
