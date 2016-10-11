package com.diilo.streetartphone.server.core.services.implementations;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.diilo.streetartphone.server.core.dao.PlaceDao;
import com.diilo.streetartphone.server.core.dao.UserDao;
import com.diilo.streetartphone.server.core.entities.PlaceBean;
import com.diilo.streetartphone.server.core.entities.UserBean;
import com.diilo.streetartphone.server.core.exceptions.NotUniquePlaceException;
import com.diilo.streetartphone.server.core.exceptions.UnknownPlaceException;
import com.diilo.streetartphone.server.core.exceptions.UserBadAuthentification;
import com.diilo.streetartphone.server.core.services.PlaceService;

@Service(value = "placeService")
public class PlaceServiceImpl implements PlaceService {

	private static final double	BETA_ON_ALPHA						= 0.99664719;

	private final static double	Earths_equatorial_radius_IN_METERS	= 6378137.0;

	private final static double	LONGITUDE_WIDTH_BY_DEGREE_IN_METERS	= 110600.0;

	@Autowired
	private PlaceDao			placeDao;

	@Autowired
	private UserDao				userDao;

	@Override
	public PlaceBean addPlace(String town, String name, double longitude, double latitude, UserBean user,
			final double tolerancyMeters) throws NotUniquePlaceException {
		return addPlace(town, name, longitude, latitude, user, tolerancyMeters, null, null);
	}

	@Override
	public PlaceBean addPlace(String town, String name, double longitude, double latitude, UserBean user,
			final double tolerancyMeters, Date startDate, Date endDate) throws NotUniquePlaceException {

		PlaceBean placeBean = new PlaceBean();
		placeBean.setId(normalizer(town, name));
		placeBean.setName(name);
		placeBean.setTown(town);
		placeBean.setOwner(user);
		placeBean.setCreationDate(new Date());

		double latitudeDegreesDelta = convertLatitudeDistanceToDegrees(latitude, tolerancyMeters);
		double longitudeDegreesDelta = convertLongitudeDistanceToDegrees(latitude, tolerancyMeters);
		placeBean.setLatitudeMin(latitude - latitudeDegreesDelta);
		placeBean.setLongitudeMin(longitude - longitudeDegreesDelta);
		placeBean.setLatitudeMax(latitude + latitudeDegreesDelta);
		placeBean.setLongitudeMax(longitude + longitudeDegreesDelta);

		try {
			placeDao.delete(placeBean);
			placeDao.save(placeBean);
			return placeBean;
		} catch (DataIntegrityViolationException exception) {
			throw new NotUniquePlaceException();
		}

	}

	@Override
	public PlaceBean addPlaceAsFavorite(final UserBean user, final String idPlace) throws UnknownPlaceException {

		// 1- Get place
		PlaceBean place = placeDao.get(idPlace);
		if (place == null) {
			throw new UnknownPlaceException();
		} else {
			List<PlaceBean> placeList = user.getFavoritePlaces();
			placeList.add(place);

			// 2- Set the user as favorite
			user.setFavoritePlaces(placeList);
			userDao.update(user);

			return place;
		}
	}

	@Override
	public List<PlaceBean> listPlaces(final double latitude, final double longitude) {
		return placeDao.getByLatitudeLongitude(latitude, longitude);
	}

	/**
	 * Remove all accents and replace specialchars by "-"
	 * 
	 * @param town
	 * @param name
	 * @return
	 */
	public String normalizer(final String town, final String name) {
		String label = new StringBuilder(town).append("-").append(name).toString();
		String normalizedText = Normalizer.normalize(label, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+",
				"");
		return normalizedText.replaceAll("[^a-zA-Z0-9]*", "-");
	}

	private double convertLatitudeDistanceToDegrees(final double latitude, final double distance) {
		return distance / LONGITUDE_WIDTH_BY_DEGREE_IN_METERS;
	}

	private double convertLongitudeDistanceToDegrees(final double latitude, final double distance) {
		return distance / longitudeDegreeWidthInMeters(latitude);
	}

	private double longitudeDegreeWidthInMeters(final double latitude) {
		double beta = Math.atan(Math.tan(latitude) * BETA_ON_ALPHA / latitude);
		return (Math.PI / 180.) * Earths_equatorial_radius_IN_METERS * Math.cos(beta);
	}

	@Override
	public void removePlaceAsFavorite(UserBean user, String idPlace) {

		// 1- Remove the place
		List<PlaceBean> placeList = user.getFavoritePlaces();
		for (PlaceBean placeBean : placeList) {
			if (placeBean.getId().equals(idPlace)) {
				placeList.remove(placeBean);
				break;
			}
		}

		// 2- Set the user as favorite
		user.setFavoritePlaces(placeList);
		userDao.update(user);

	}

	@Override
	public void deletePlace(String idPlace, UserBean user) throws UnknownPlaceException, UserBadAuthentification {
		PlaceBean place = placeDao.get(idPlace);

		if (place == null) {
			throw new UnknownPlaceException();
		} else {
			if (place.getOwner().getEmail().equals(user.getEmail())) {

			} else {
				throw new UserBadAuthentification();
			}
		}
	}

	@Override
	public PlaceBean findPlace(String idPlace) throws UnknownPlaceException {
		PlaceBean place = placeDao.get(idPlace);
		if (place == null) {
			throw new UnknownPlaceException();
		} else {
			return place;
		}
	}
}
