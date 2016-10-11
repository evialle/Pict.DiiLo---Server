/**
 * 
 */
package com.diilo.streetartphone.server.core.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.diilo.streetartphone.server.core.dao.generics.GenericDaoHibernateImpl;
import com.diilo.streetartphone.server.core.entities.UserPositionBean;

/**
 * @author Eric
 */
@Repository(value = "userPositionDao")
public class UserPositionDao extends GenericDaoHibernateImpl<UserPositionBean, Long> {

	private static final Logger	LOG	= LoggerFactory.getLogger(UserPositionDao.class);

	@Autowired
	public UserPositionDao(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
		super(UserPositionBean.class);
		setSessionFactory(sessionFactory);
	}

	public List<UserPositionBean> listNotifiablesInactiveBetween(Date minimumInactiveDate, Date maximumInactiveDate) {

		@SuppressWarnings("unchecked")
		List<UserPositionBean> list = getHibernateTemplate()
				.find("from UserPositionBean userPosition where "
						+ "userPosition.date > ? and userPosition.date < ? "
						+ "and ((userPosition.lastNotificationDate > ? and userPosition.lastNotificationDate < ?) or (userPosition.lastNotificationDate is null))",
						minimumInactiveDate, maximumInactiveDate, minimumInactiveDate, maximumInactiveDate);

		return list;
	}
}
