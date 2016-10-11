/**
 * 
 */
package com.diilo.streetartphone.server.core.utils;

import twitter4j.Tweet;
import twitter4j.TwitterException;

import com.diilo.streetartphone.server.core.entities.UserBean;
import com.diilo.streetartphone.server.core.exceptions.TwitterServiceException;

/**
 * @author Eric Vialle
 */
public interface TwitterService {

	/** Send synchronousMessage. */
	void sendMessage(UserBean user, String message, double latitude, double longitude) throws TwitterException;

	int getUserId(final UserBean user) throws TwitterServiceException;

	Tweet lastTweet(double latitude, double longitude);
}
