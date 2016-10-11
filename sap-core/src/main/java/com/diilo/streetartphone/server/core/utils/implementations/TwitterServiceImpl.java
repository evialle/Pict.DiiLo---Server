/**
 * 
 */
package com.diilo.streetartphone.server.core.utils.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.diilo.streetartphone.server.core.entities.UserBean;
import com.diilo.streetartphone.server.core.exceptions.TwitterServiceException;
import com.diilo.streetartphone.server.core.utils.TwitterService;

/**
 * @author Eric
 */
@Service
public class TwitterServiceImpl implements TwitterService {

	private static final Logger	LOG	= LoggerFactory.getLogger(TwitterServiceImpl.class);

	@Value("#{configCoreProperties['twitter.consumer.key']}")
	private String				TWITTER_CONSUMER_KEY;

	@Value("#{configCoreProperties['twitter.consumer.secret']}")
	private String				TWITTER_CONSUMER_SECRET;

	/**
	 * @param user
	 * @return the tweeter user's id
	 * @throws TwitterServiceException
	 */
	public int getUserId(final UserBean user) throws TwitterServiceException {
		try {
			Twitter twitter = buildTwitter(user);
			return twitter.getId();
		} catch (IllegalStateException e) {
			LOG.error("Error for " + user.toString() + " - " + e.getMessage());
			throw new TwitterServiceException(e);
		} catch (TwitterException e) {
			LOG.error("Error for " + user.toString() + " - " + e.getMessage());
			throw new TwitterServiceException(e);

		}
	}

	private Twitter buildTwitter(final UserBean user) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(false).setSource("Pict.DiiLo http://pict.diilo.com")
				.setOAuthConsumerKey(TWITTER_CONSUMER_KEY).setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET)
				.setOAuthAccessToken(user.getTwitterAuthToken())
				.setOAuthAccessTokenSecret(user.getTwitterAuthSecretToken());

		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();

		return twitter;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.diilo.streetartphone.server.services.TwitterService#sendMessage(com.diilo.streetartphone.server
	 * .entities.UserBean, java.lang.String)
	 */
	@Override
	public void sendMessage(UserBean user, String message, double latitude, double longitude) throws TwitterException {

		Twitter twitter = buildTwitter(user);

		GeoLocation geoLocation = new GeoLocation(latitude, longitude);
		twitter.updateStatus(message, geoLocation);
	}

	/**
	 * @param userPosition
	 * @return the last tweet near the last position if this tweets isn't produced by user;
	 */
	@Override
	public Tweet lastTweet(final double latitude, final double longitude) {
		Tweet lastTweet = null;
		// The factory instance is re-useable and thread safe.
		Twitter twitter = new TwitterFactory().getInstance();
		StringBuilder queryString = new StringBuilder().append("near:").append(latitude).append(",").append(longitude)
				.append(" within:0.6km count:1");
		Query query = new Query(queryString.toString());
		QueryResult result;
		try {
			result = twitter.search(query);
			// System.out.println("hits:" + result.getTotal());
			if (result.getTweets().size() > 0) {
				lastTweet = result.getTweets().get(0);
			}
		} catch (TwitterException e) {
			LOG.warn("TwitterException for position: " + latitude + ", " + longitude + " - " + e.getMessage());
		}

		return lastTweet;
	}
}
