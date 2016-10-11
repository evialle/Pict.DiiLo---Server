/**
 * 
 */
package com.diilo.streetartphone.server.services.rs.controllers;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.diilo.streetartphone.server.core.entities.UserBean;
import com.diilo.streetartphone.server.core.exceptions.TechnicalException;
import com.diilo.streetartphone.server.core.exceptions.UserBadAuthentification;
import com.diilo.streetartphone.server.core.exceptions.UserEmailAlreadyExistException;
import com.diilo.streetartphone.server.core.exceptions.UserNicknameAlreadyExistException;
import com.diilo.streetartphone.server.core.exceptions.UserUnknowException;
import com.diilo.streetartphone.server.core.services.UserService;

/**
 * @author Eric Vialle
 */
@Controller
public class UserRemoteController {

	private static final Logger	LOG	= LoggerFactory.getLogger(UserRemoteController.class);

	@Autowired
	private UserService			userService;

	/**
	 * Create a user in the database.
	 * 
	 * @param udid
	 * @param nickname
	 * @param email
	 * @param password
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/user/{udid}/create/{lang}", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView create(@PathVariable final String udid, @PathVariable final String lang,
			@RequestParam(required = true) final String nickname, @RequestParam(required = true) final String email,
			@RequestParam(required = true) final String password, final HttpServletRequest req) {

		LOG.debug("Create: " + nickname + " - email: " + email + " - password lenght: " + password.length());
		ModelAndView mv = new ModelAndView("json");
		try {
			UserBean user = userService.createUser(nickname, email, password, udid, lang, req.getRemoteAddr());
			String token = userService.createSessionCredential(user.getEmail(), user.getPassword());
			LOG.debug("Token for " + user.getNickname() + ": " + token);
			LOG.info("Creation Successful: " + nickname + " - email: " + email + " - password lenght: "
					+ password.length());

			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);
			mv.addObject(ControllerConstants.CREDENTIALS_MODELLABEL, token);
		} catch (UserNicknameAlreadyExistException e) {
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_NICKNAMEALREADYEXISTS);
			LOG.info("Creation Failed - User's nickname, exists: " + nickname + " - email: " + email
					+ " - password lenght: " + password.length());
		} catch (UserEmailAlreadyExistException e) {
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_EMAILALREADYEXISTS);
			LOG.info("Creation Failed - User's email, exists: " + nickname + " - email: " + email
					+ " - password lenght: " + password.length());
		} catch (UserUnknowException e) {
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_UNKNOWN);
			LOG.error("Creation Failed - User unknown => IMPOSSIBLE: " + nickname + " - email: " + email
					+ " - password lenght: " + password.length());
		} catch (UserBadAuthentification e) {
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_BADAUTH);
			LOG.error("Creation Failed - User bad auth => IMPOSSIBLE: " + nickname + " - email: " + email
					+ " - password lenght: " + password.length());
		}

		return mv;
	}

	/**
	 * Login into the system, thanks to its email and password.
	 * 
	 * @param udid
	 * @param email
	 * @param password
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/user/{udid}/login", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView login(@PathVariable final String udid, @RequestParam(required = true) final String email,
			@RequestParam(required = true) final String password, final HttpServletRequest req) {

		ModelAndView mv = new ModelAndView("json");
		try {
			String credentials = userService.createSessionCredential(email, password);
			UserBean user = userService.getUserByCredentials(credentials);

			LOG.info("Token for " + email + ": " + credentials + " - " + udid);

			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);
			mv.addObject(ControllerConstants.CREDENTIALS_MODELLABEL, credentials);
			mv.addObject(ControllerConstants.TWITTER_STATUS_MODELLABEL, userService.isTwitterEnabled(email));
			mv.addObject(ControllerConstants.NICKNAME_MODELLABEL, user.getNickname());
		} catch (UserUnknowException e) {
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_UNKNOWN);
			LOG.info("Authentification failed (email) for " + email + " - " + udid);
		} catch (UserBadAuthentification e) {
			mv.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_BADAUTH);
			LOG.info("Authentification failed (password) for " + email + " - " + udid);
		}

		return mv;
	}

	/**
	 * Set a new password.
	 * 
	 * @param credentials
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	@RequestMapping(value = "/user/{credentials}/chgpassword", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView changePassword(@PathVariable final String credentials, @RequestParam final String oldPassword,
			@RequestParam final String newPassword) {
		ModelAndView mav = new ModelAndView("json");

		try {
			UserBean user = userService.getUserByCredentials(credentials);
			user = userService.changePassword(user.getEmail(), oldPassword, newPassword);
			userService.updateSessionCredential(credentials, user);
			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);
		} catch (UserUnknowException e) {
			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_UNKNOWN);
		} catch (UserBadAuthentification e) {
			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_BADAUTH);
		}
		return mav;
	}

	/**
	 * @param email
	 * @param udid
	 * @return
	 */
	@RequestMapping(value = "/user/{udid}/sendpassword", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView sendPassword(@RequestParam final String email, @PathVariable final String udid) {
		ModelAndView mav = new ModelAndView("json");

		LOG.info("Password request for " + email + " (" + udid + ")");
		try {
			userService.sendPasswordByMail(email);
			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);

		} catch (UserUnknowException e) {
			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_UNKNOWN);

		} catch (TechnicalException e) {
			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_TECHNICAL_KO);
		}

		return mav;
	}

	/**
	 * Says if the user is connected to a Social Network (such as Facebook or Twitter).
	 * 
	 * @param credentials
	 * @return
	 */
	@RequestMapping(value = "/user/{credentials}/twitterstatus", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView twitterStatus(@PathVariable final String credentials) {
		return socialNetworkStatus(credentials);
	}

	@RequestMapping(value = "/user/{credentials}/snstatus", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView socialNetworkStatus(@PathVariable final String credentials) {
		ModelAndView mav = new ModelAndView("json");

		try {
			UserBean user = userService.getUserByCredentials(credentials);

			// Twitter
			boolean twitterStatus = user.getTwitterAuthToken() != null;
			mav.addObject(ControllerConstants.TWITTER_STATUS_MODELLABEL, twitterStatus);

			// Facebook
			boolean facebookStatus = user.getFacebookAccessToken() != null;
			mav.addObject(ControllerConstants.FACEBOOK_STATUS_MODELLABEL, facebookStatus);

			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_OK);

		} catch (UserBadAuthentification e) {
			mav.addObject(ControllerConstants.STATUS_MODELLABEL, ControllerConstants.STATUS_USER_BADAUTH);
		}
		return mav;

	}
}
