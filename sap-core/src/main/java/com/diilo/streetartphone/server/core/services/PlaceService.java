/**
 * 
 */
package com.diilo.streetartphone.server.core.services;

import java.util.Date;
import java.util.List;

import com.diilo.streetartphone.server.core.entities.PlaceBean;
import com.diilo.streetartphone.server.core.entities.UserBean;
import com.diilo.streetartphone.server.core.exceptions.NotUniquePlaceException;
import com.diilo.streetartphone.server.core.exceptions.UnknownPlaceException;
import com.diilo.streetartphone.server.core.exceptions.UserBadAuthentification;

/**
 * @author Eric
 */
public interface PlaceService {

	PlaceBean addPlace(final String town, final String name, final double longitude, final double latitude,
			final UserBean user, final double tolerancyMeters) throws NotUniquePlaceException;

	PlaceBean addPlace(final String town, final String name, final double longitude, final double latitude,
			final UserBean user, final double tolerancyMeters, final Date startDate, final Date endDate)
			throws NotUniquePlaceException;

	PlaceBean addPlaceAsFavorite(UserBean user, String idPlace) throws UnknownPlaceException;

	List<PlaceBean> listPlaces(double longitude, double latitude);

	void removePlaceAsFavorite(UserBean user, String idPlace);

	void deletePlace(String idPlace, UserBean user) throws UnknownPlaceException, UserBadAuthentification;

	PlaceBean findPlace(String idPlace) throws UnknownPlaceException;
}
