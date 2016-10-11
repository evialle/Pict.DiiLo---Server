package com.diilo.streetartphone.server.services.rs.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.diilo.streetartphone.server.core.entities.MediaBean;
import com.diilo.streetartphone.server.core.entities.UserBean;
import com.diilo.streetartphone.server.core.exceptions.PictureAlreadyGradedException;
import com.diilo.streetartphone.server.core.exceptions.TechnicalException;
import com.diilo.streetartphone.server.core.exceptions.UnknownPictureException;
import com.diilo.streetartphone.server.core.exceptions.UserBadAuthentification;
import com.diilo.streetartphone.server.core.exceptions.UserUnknowException;
import com.diilo.streetartphone.server.core.services.MediaGradesService;
import com.diilo.streetartphone.server.core.services.MediaReportsService;
import com.diilo.streetartphone.server.core.services.MediasService;
import com.diilo.streetartphone.server.core.services.UserPositionService;
import com.diilo.streetartphone.server.core.services.UserService;

@Controller
public class MediasManagerRemoteController {

	private static final Logger	LOG	= LoggerFactory.getLogger(MediasManagerRemoteController.class);

	@Autowired
	private MediasService		mediasService;

	@Autowired
	private MediaGradesService	mediaGradesService;

	@Autowired
	private MediaReportsService	mediaReportsService;

	@Autowired
	private UserService			userService;

	@Autowired
	private UserPositionService	userPositionService;

	@RequestMapping(value = "/picturesmgr/{credentials}/post", method = RequestMethod.POST)
	public ModelAndView postNewMedia(@PathVariable final String credentials, @RequestParam final String description,
			@RequestParam final double longitude, @RequestParam final double latitude,
			@RequestParam MultipartFile mediaData,
			@RequestParam(required = false, defaultValue = "false") boolean twitter,
			@RequestParam(required = false, defaultValue = "false") boolean facebook) {

		ModelAndView mv = new ModelAndView("json");

		try {
			// 1- CheckCredentials
			UserBean user = userService.getUserByCredentials(credentials);
			LOG.debug("Media added by: " + user.getNickname());

			// 2- Detect the type of file
			if (mediaData.getContentType().contains("video")) {
				// 2.a - Add video
				mediasService.addVideo(mediaData, user, description, longitude, latitude, twitter, facebook);
				mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);
			} else if (mediaData.getContentType().contains("image")) {
				// 2.b - Add picture
				mediasService.addPicture(mediaData, user, description, longitude, latitude, twitter, facebook);
				mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);
			} else {
				mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_TECHNICAL_KO);
			}

		} catch (UserBadAuthentification e) {
			LOG.warn(e.getMessage());
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_BADAUTH);
		} catch (TechnicalException e) {
			LOG.warn(e.getMessage());
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_TECHNICAL_KO);
		}

		return mv;
	}

	@RequestMapping(value = "/picturesmgr/{credentials}/list/{userNickname}", method = RequestMethod.GET)
	public ModelAndView getAllPictures(@PathVariable final String credentials, @PathVariable final String userNickname) {
		ModelAndView mv = new ModelAndView("json");

		try {
			// 1- CheckCredentials
			UserBean user = userService.getUserByCredentials(credentials);
			LOG.debug("List: " + userNickname + " by " + user.getEmail());

			List<MediaBean> list = mediasService.listPictures(userNickname);
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);
			mv.addObject(ControllerConstants.LIST_MODELLABEL, list);
		} catch (UserBadAuthentification e) {
			LOG.warn(e.getMessage());
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_BADAUTH);
		}

		return mv;
	}

	/**
	 * Get all the picture around the user
	 * 
	 * @param credentials
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	@RequestMapping(value = "/picturesmgr/perimeter", method = RequestMethod.GET)
	public ModelAndView getPicturesInPerimeter(@RequestParam(required = true) final double latitude,
			@RequestParam(required = true) final double longitude) {

		ModelAndView mv = new ModelAndView("json");

		List<MediaBean> list = mediasService.listPicturesInPerimeter(latitude, longitude);
		mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);
		mv.addObject(ControllerConstants.LIST_MODELLABEL, list);

		return mv;
	}

	/**
	 * Get all the picture around the user
	 * 
	 * @param credentials
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	@RequestMapping(value = "/picturesmgr/{credentials}/perimeter", method = RequestMethod.GET)
	public ModelAndView getPicturesInPerimeterWithCredentials(@PathVariable final String credentials,
			@RequestParam(required = true) final double latitude, @RequestParam(required = true) final double longitude) {

		ModelAndView mv = new ModelAndView("json");

		UserBean user;
		try {
			// 1- List Pictures in perimeter
			List<MediaBean> list = mediasService.listPicturesInPerimeter(latitude, longitude);
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);
			mv.addObject(ControllerConstants.LIST_MODELLABEL, list);

			// 2- Save Position for notifications
			user = userService.getUserByCredentials(credentials);
			userPositionService.savePositionForNotifications(user, latitude, longitude);
		} catch (UserBadAuthentification e) {
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_BADAUTH);
		}

		return mv;
	}

	/**
	 * Get all the picture in a large area of the user
	 * 
	 * @param credentials
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	@RequestMapping(value = "/picturesmgr/largearea", method = RequestMethod.GET)
	public ModelAndView getPicturesInLargeArea(@RequestParam(required = true) final double latitude,
			@RequestParam(required = true) final double longitude) {

		ModelAndView mv = new ModelAndView("json");

		List<MediaBean> list = mediasService.listPicturesInLargeArea(latitude, longitude);
		mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);
		mv.addObject(ControllerConstants.LIST_MODELLABEL, list);

		return mv;
	}

	@RequestMapping(value = "/picturesmgr/{udid}/perimeterLogin", method = RequestMethod.GET)
	public ModelAndView getPicturesAndCredentialsInPerimeter(@PathVariable final String udid,
			@RequestParam(required = true) final double latitude,
			@RequestParam(required = true) final double longitude, @RequestParam(required = true) final String email,
			@RequestParam(required = true) final String password) {

		ModelAndView mv = new ModelAndView("json");
		LOG.debug("Request list and credentials for: " + email + " at " + latitude + ";" + longitude);
		try {
			// 1- create credentials
			String credentials = userService.createSessionCredential(email, password);
			UserBean user = userService.getUserByCredentials(credentials);
			mv.addObject(ControllerConstants.CREDENTIALS_MODELLABEL, credentials);
			// 2- set Social networks status
			mv.addObject(ControllerConstants.TWITTER_STATUS_MODELLABEL, user.isTwitterEnabled());
			mv.addObject(ControllerConstants.FACEBOOK_STATUS_MODELLABEL, user.isFacebookEnabled());
			mv.addObject(ControllerConstants.NICKNAME_MODELLABEL, user.getNickname());

			// 3- set positions for Notifications
			userPositionService.savePositionForNotifications(user, latitude, longitude);

			// 4- list pictures
			List<MediaBean> list = mediasService.listPicturesInPerimeter(latitude, longitude);
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);
			mv.addObject(ControllerConstants.LIST_MODELLABEL, list);
		} catch (UserUnknowException e) {
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_UNKNOWN);
		} catch (UserBadAuthentification e) {
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_BADAUTH);
		}

		return mv;
	}

	@RequestMapping(value = "/picturesmgr/{credentials}/{idPicture}/addPositiveGrade", method = RequestMethod.GET)
	public ModelAndView addPositiveGrade(@PathVariable final String credentials, @PathVariable final String idPicture) {

		ModelAndView mv = new ModelAndView("json");

		try {
			// 1- CheckCredentials
			UserBean userBean = userService.getUserByCredentials(credentials);

			// 2- add positive grade
			mediaGradesService.addPositiveGrade(idPicture, userBean.getEmail());
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);
		} catch (UserBadAuthentification e) {
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_BADAUTH);
		} catch (PictureAlreadyGradedException e) {
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_PICTURE_ALREADYGRADED);
		}

		return mv;
	}

	@RequestMapping(value = "/picturesmgr/{credentials}/{idPicture}/addNegativeGrade", method = RequestMethod.GET)
	public ModelAndView addNegativeGrade(@PathVariable final String credentials, @PathVariable final String idPicture) {

		ModelAndView mv = new ModelAndView("json");

		try {
			// 1- CheckCredentials
			UserBean userBean = userService.getUserByCredentials(credentials);

			// 2- add positive grade
			mediaGradesService.addNegativeGrade(idPicture, userBean.getEmail());
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);
		} catch (UserBadAuthentification e) {
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_BADAUTH);
		} catch (PictureAlreadyGradedException e) {
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_PICTURE_ALREADYGRADED);
		}

		return mv;
	}

	@RequestMapping(value = "/picturesmgr/{credentials}/{idPicture}/report", method = RequestMethod.GET)
	public ModelAndView reportPicture(@PathVariable final String credentials, @PathVariable final String idPicture,
			@RequestParam(required = true) final String comments) {

		ModelAndView mv = new ModelAndView("json");

		try {
			// 1- CheckCredentials
			UserBean userBean = userService.getUserByCredentials(credentials);

			// 2- report picture
			mediaReportsService.reportPicture(idPicture, userBean.getEmail(), comments);
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);
		} catch (UserBadAuthentification e) {
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_BADAUTH);
		}

		return mv;
	}

	@RequestMapping(value = "/picturesmgr/{credentials}/{idPicture}/delete", method = RequestMethod.GET)
	public ModelAndView deletePicture(@PathVariable final String credentials, @PathVariable final String idPicture) {
		ModelAndView mv = new ModelAndView("json");

		try {
			// 1- CheckCredentials
			UserBean userBean = userService.getUserByCredentials(credentials);
			MediaBean pictureBean = mediasService.getPictureInfo(idPicture);

			// 2- delete picture if the user is the owner
			if (pictureBean.getNickname().equals(userBean.getNickname())) {
				mediasService.deletePicture(pictureBean);
				LOG.info("Deletion of Media: " + idPicture + " by " + userBean.getNickname());
				mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);
			} else {
				mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_BADAUTH);
			}

		} catch (UserBadAuthentification e) {
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_BADAUTH);
		} catch (UnknownPictureException e) {
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_PICTURE_UNKNOWN);
		}

		return mv;

	}

}
