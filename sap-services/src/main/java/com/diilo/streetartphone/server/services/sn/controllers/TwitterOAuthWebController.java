/**
 * 
 */
package com.diilo.streetartphone.server.services.sn.controllers;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;

import com.diilo.streetartphone.server.core.entities.UserBean;
import com.diilo.streetartphone.server.core.exceptions.UserBadAuthentification;
import com.diilo.streetartphone.server.core.services.UserService;

/**
 * @author Eric
 */
@Controller
public class TwitterOAuthWebController {

	private static final String	REQUESTOKEN_TWITTER_SESSION	= "requestTokenTwitter";

	private static final Logger	LOG							= LoggerFactory.getLogger(TwitterOAuthWebController.class);

	@Value("#{configCoreProperties['twitter.consumer.key']}")
	private String				TWITTER_CONSUMER_KEY;

	@Value("#{configCoreProperties['twitter.consumer.secret']}")
	private String				TWITTER_CONSUMER_SECRET;

	@Autowired
	private UserService			userService;

	/**
	 * Entry point for the authentification. This method will redirect to Twitter authentification/acceptance
	 * page.
	 * 
	 * @param credentials
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/twitter/{credentials}/login", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView loginTwitter(@PathVariable final String credentials, final HttpServletRequest request) {

		ModelAndView mav = new ModelAndView();
		try {
			UserBean user = userService.getUserByCredentials(credentials);

			LOG.info("Login request for user: " + user);

			Twitter twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);

			// Build the callback url
			StringBuilder oauthCallBack = new StringBuilder(request.getScheme()).append("://")
					.append(request.getServerName()).append(":").append(request.getServerPort())
					.append(request.getContextPath()).append("/web/twitter/").append(credentials).append("/confirm");

			// Generate the request token in the session (no other choice)
			RequestToken requestToken = twitter.getOAuthRequestToken(oauthCallBack.toString());
			request.getSession().setAttribute(REQUESTOKEN_TWITTER_SESSION, requestToken);

			mav.setViewName("redirect:" + requestToken.getAuthorizationURL());
		} catch (TwitterException e) {
			LOG.error("Twitter Exception: " + e.getMessage());
			mav.setViewName("twitter/badauth");
		} catch (UserBadAuthentification e1) {
			LOG.error("Those credentials are unknown: " + credentials);
			mav.setViewName("twitter/badauth");
		}

		return mav;
	}

	/**
	 * CallBack method when twitter has qualified our application.
	 * 
	 * @param credentials
	 * @param req
	 * @param oauth_token
	 * @param oauth_verifier
	 * @return
	 */
	@RequestMapping(value = "/twitter/{credentials}/confirm", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView confirmTwitter(@PathVariable final String credentials,
			@RequestParam(required = false) final String oauth_verifier,
			@RequestParam(required = false) final String denied, final HttpServletRequest request) {

		ModelAndView mav = new ModelAndView();

		if (denied != null) {
			LOG.info(" Twitter Denied " + credentials);
			mav.setViewName("twitter/badauth");
		} else {
			try {
				RequestToken requestToken = (RequestToken) request.getSession().getAttribute(
						REQUESTOKEN_TWITTER_SESSION);

				Twitter twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
				AccessToken accessToken = twitter.getOAuthAccessToken(requestToken.getToken(),
						requestToken.getTokenSecret(), oauth_verifier);
				int idTwitter = twitter.getId();

				UserBean user = userService.updateTwitterToken(idTwitter, credentials, accessToken);
				LOG.info("Authentification Twitter for " + user.getEmail());
				mav.setViewName("twitter/success");

			} catch (UserBadAuthentification e) {
				LOG.warn("User bad authentification " + credentials);
				mav.setViewName("twitter/badauth");
			} catch (TwitterException e) {
				LOG.warn("Twitter Error " + credentials + " - " + e.getMessage());
				mav.setViewName("twitter/badauth");
			}
		}

		return mav;
	}
}