/**
 * 
 */
package com.diilo.streetartphone.server.core.services;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.diilo.streetartphone.server.core.entities.MediaBean;
import com.diilo.streetartphone.server.core.entities.PlaceBean;
import com.diilo.streetartphone.server.core.entities.UserBean;
import com.diilo.streetartphone.server.core.entities.UserPositionBean;
import com.diilo.streetartphone.server.core.exceptions.TechnicalException;
import com.diilo.streetartphone.server.core.exceptions.UnknownPictureException;

/**
 * @author Eric
 */
public interface MediasService {

	List<MediaBean> listPictures(final String nicknameToList);

	List<MediaBean> listPicturesInPerimeter(final double latitude, final double longitude);

	List<MediaBean> listPicturesInLargeArea(final double latitude, final double longitude);

	MediaBean getPictureInfo(final String idPicture) throws UnknownPictureException;

	void addPicture(MultipartFile picture, UserBean user, String description, double longitude, double latitude,
			boolean sentToTwitter, boolean facebook) throws TechnicalException;

	List<MediaBean> listPicturesInPlace(PlaceBean place);

	void deletePicture(final MediaBean picture);

	void addVideo(MultipartFile pictureData, UserBean user, String description, double longitude, double latitude,
			boolean twitter, boolean facebook) throws TechnicalException;

	MediaBean lastMedia(UserPositionBean userPositionBean, Date minDate, Date maxDate);
}