/**
 * 
 */
package com.diilo.streetartphone.server.services.rs.controllers;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.diilo.streetartphone.server.core.entities.PlaceBean;
import com.diilo.streetartphone.server.core.entities.UserBean;
import com.diilo.streetartphone.server.core.exceptions.NotUniquePlaceException;
import com.diilo.streetartphone.server.core.exceptions.UnknownPlaceException;
import com.diilo.streetartphone.server.core.exceptions.UserBadAuthentification;
import com.diilo.streetartphone.server.core.services.PlaceService;
import com.diilo.streetartphone.server.core.services.UserPositionService;
import com.diilo.streetartphone.server.core.services.UserService;

/**
 * @author Eric
 */
@Controller
public class PlaceRemoteController {

	private static final Logger	LOG	= LoggerFactory.getLogger(PlaceRemoteController.class);

	@Autowired
	private PlaceService		placeService;

	@Autowired
	private UserService			userService;

	@Autowired
	private UserPositionService	userPositionService;

	@RequestMapping(value = "/place/list", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView listPlaces(@RequestParam(required = true) double longitude,
			@RequestParam(required = true) double latitude) {
		ModelAndView mav = new ModelAndView("json");

		List<PlaceBean> placeList = placeService.listPlaces(longitude, latitude);
		mav.addObject("placeList", placeList);

		mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);

		return mav;
	}

	@RequestMapping(value = "/place/{credentials}/list", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView listPlacesWithCredentials(@PathVariable final String credentials,
			@RequestParam(required = true) double longitude, @RequestParam(required = true) double latitude) {
		ModelAndView mav = new ModelAndView("json");

		try {
			UserBean user = userService.getUserByCredentials(credentials);
			userPositionService.savePositionForNotifications(user, latitude, longitude);

			List<PlaceBean> placeList = placeService.listPlaces(longitude, latitude);
			mav.addObject("placeList", placeList);

			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);
		} catch (UserBadAuthentification e) {
			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_BADAUTH);
			LOG.warn("Bad authentification: " + credentials);
		}

		return mav;
	}

	@RequestMapping(value = "/place/{credentials}/create", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView createPlace(@PathVariable final String credentials,
			@RequestParam(required = true) final String town, @RequestParam(required = true) final String name,
			@RequestParam(required = true) final double longitude,
			@RequestParam(required = true) final double latitude,
			@RequestParam(required = false, defaultValue = "500") final int tolerancyMeters,
			@RequestParam(required = false) final Date startDate, @RequestParam(required = false) final Date endDate) {

		ModelAndView mav = new ModelAndView("json");

		try {
			UserBean user = userService.getUserByCredentials(credentials);
			PlaceBean place = placeService.addPlace(town, name, longitude, latitude, user, tolerancyMeters, startDate,
					endDate);

			placeService.addPlaceAsFavorite(user, place.getId());

			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);
		} catch (UserBadAuthentification e) {
			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_BADAUTH);
			LOG.warn("Bad authentification: " + credentials);
		} catch (NotUniquePlaceException e) {
			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_PLACE_NOTUNIQUE);
			LOG.warn("NotUniquePlaceException: " + credentials);
		} catch (UnknownPlaceException e) {
			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_PLACE_UNKNOWN);
			LOG.warn("UnknownPlaceException: " + credentials);
		}

		return mav;
	}

	@RequestMapping(value = "/place/{credentials}/remove/{idPlace}", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView removePlace(@PathVariable final String credentials, @PathVariable final String idPlace) {
		ModelAndView mav = new ModelAndView("json");

		try {
			UserBean user = userService.getUserByCredentials(credentials);

			placeService.deletePlace(idPlace, user);
			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);

		} catch (UserBadAuthentification e) {
			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_BADAUTH);
			LOG.warn("Bad authentification: " + credentials);
		} catch (UnknownPlaceException e) {
			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_BADAUTH);
			LOG.warn("Bad authentification: " + credentials);
		}

		return mav;
	}

	@RequestMapping(value = "/place/{credentials}/favoriteplaces/add/{idPlace}", method = { RequestMethod.GET,
			RequestMethod.POST })
	public ModelAndView addPlaceAsFavorite(@PathVariable final String credentials, @PathVariable final String idPlace) {
		ModelAndView mav = new ModelAndView("json");

		try {
			UserBean user = userService.getUserByCredentials(credentials);
			placeService.addPlaceAsFavorite(user, idPlace);

			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);
		} catch (UnknownPlaceException e) {
			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_PLACE_UNKNOWN);
			LOG.warn("UnknownPlaceException: " + credentials);
		} catch (UserBadAuthentification e) {
			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_BADAUTH);
			LOG.warn("Bad authentification: " + credentials);
		}

		return mav;
	}

	@RequestMapping(value = "/place/{credentials}/favoriteplaces/remove/{idPlace}", method = { RequestMethod.GET,
			RequestMethod.POST })
	public ModelAndView removePlaceAsFavorite(@PathVariable final String credentials, @PathVariable final String idPlace) {
		ModelAndView mav = new ModelAndView("json");

		try {
			UserBean user = userService.getUserByCredentials(credentials);
			placeService.removePlaceAsFavorite(user, idPlace);

			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);

		} catch (UserBadAuthentification e) {
			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_BADAUTH);
			LOG.warn("Bad authentification: " + credentials);
		}

		return mav;
	}
}