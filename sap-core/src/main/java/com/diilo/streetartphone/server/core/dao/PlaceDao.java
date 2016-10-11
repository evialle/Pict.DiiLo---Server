/**
 * 
 */
package com.diilo.streetartphone.server.core.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.diilo.streetartphone.server.core.dao.generics.GenericDaoHibernateImpl;
import com.diilo.streetartphone.server.core.entities.PlaceBean;

/**
 * @author Eric
 */
@Repository
public class PlaceDao extends GenericDaoHibernateImpl<PlaceBean, String> {

	@Autowired
	public PlaceDao(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
		super(PlaceBean.class);
		setSessionFactory(sessionFactory);
	}

	public List<PlaceBean> getByLatitudeLongitude(final double latitude, final double longitude) {
		List<PlaceBean> list = getHibernateTemplate()
				.find("from PlaceBean place where place.longitudeMin < ? and place.longitudeMax > ? and place.latitudeMin < ? and place.latitudeMax > ? order by place.date desc limit 70",
						longitude, longitude, latitude, latitude);

		return list;
	}
}
