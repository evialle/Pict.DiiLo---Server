/**
 * 
 */
package com.diilo.streetartphone.server.core.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.diilo.streetartphone.server.core.dao.generics.GenericDaoHibernateImpl;
import com.diilo.streetartphone.server.core.entities.MediaBean;
import com.diilo.streetartphone.server.core.entities.UserPositionBean;

/**
 * @author Eric
 */
@Repository
public class MediaDao extends GenericDaoHibernateImpl<MediaBean, String> {

	private static final Logger	LOG	= LoggerFactory.getLogger(MediaDao.class);

	@Autowired
	public MediaDao(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
		super(MediaBean.class);
		setSessionFactory(sessionFactory);
	}

	public List<MediaBean> list(final String nickname) {
		return getHibernateTemplate().find("from MediaBean media where media.nickname=? order by media.date desc",
				nickname);
	}

	public List<MediaBean> listInPerimeter(final double minLatitude, final double minLongitude,
			final double maxLatitude, final double maxLongitude) {
		LOG.debug("Min Latitude:" + minLatitude + " - Max Latitude: " + maxLatitude + " - MinLongitude: "
				+ minLongitude + " - Max Longitude: " + maxLongitude);
		return getHibernateTemplate()
				.find("from MediaBean media where (media.latitude between ? and ?) and (media.longitude between ? and ?) order by media.date desc limit 70",
						minLatitude, maxLatitude, minLongitude, maxLongitude);
	}

	public MediaBean findByFileId(final String fileId) {
		return get(fileId);
	}

	/**
	 * Add a positive vote to a picture
	 * 
	 * @param fileId
	 */
	public void addPositiveGrade(final String fileId) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createQuery("update MediaBean set positiveGrade = positiveGrade+1 where fileId = :fileId");
		q.setString("fileId", fileId);
		q.executeUpdate();

		getHibernateTemplate().flush();
	}

	/**
	 * Add a negative vote to a picture
	 * 
	 * @param fileId
	 */
	public void addNegativeGrade(final String fileId) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createQuery("update MediaBean set negativeGrade = negativeGrade+1 where fileId = :fileId");
		q.setString("fileId", fileId);
		q.executeUpdate();

		getHibernateTemplate().flush();
	}

	public boolean updateUrlRegularImage(final String idPicture, final String urlRegularImage) {
		MediaBean media = get(idPicture);
		if (media == null) {
			return true;
		} else {
			media.setUrlRegularImage(urlRegularImage);
			update(media);
			return true;
		}
	}

	public MediaBean lastMedia(final UserPositionBean userPositionBean, final double tolerancyDegree,
			final Date minDate, final Date maxDate) {
		List<MediaBean> mediaList = getHibernateTemplate()
				.find("from MediaBean media where (media.latitude between ? and ?) and (media.longitude between ? and ?) and (media.date > ?) and (media.date < ?) and (media.nickname != ?) limit 1",
						userPositionBean.getLatitude() - tolerancyDegree,
						userPositionBean.getLatitude() + tolerancyDegree,
						userPositionBean.getLongitude() - tolerancyDegree,
						userPositionBean.getLongitude() + tolerancyDegree, minDate, maxDate,
						userPositionBean.getUser().getNickname());

		if ((mediaList == null) || (mediaList.size() == 0)) {
			return null;
		} else {
			return mediaList.get(0);
		}

	}
}
