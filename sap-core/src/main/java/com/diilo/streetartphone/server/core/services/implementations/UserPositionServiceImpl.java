/**
 * 
 */
package com.diilo.streetartphone.server.core.services.implementations;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import twitter4j.Tweet;

import com.diilo.streetartphone.server.core.dao.UserPositionDao;
import com.diilo.streetartphone.server.core.entities.MediaBean;
import com.diilo.streetartphone.server.core.entities.UserBean;
import com.diilo.streetartphone.server.core.entities.UserPositionBean;
import com.diilo.streetartphone.server.core.services.MediasService;
import com.diilo.streetartphone.server.core.services.UserPositionService;
import com.diilo.streetartphone.server.core.utils.TwitterService;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

/**
 * @author Eric
 */
@Service("userPositionService")
public class UserPositionServiceImpl implements UserPositionService {

	private static final int	MAX_MINUTES_POSITION_IDLE	= 75;

	private static final int	MIN_MINUTES_POSITION_IDLE	= 15;

	private static final Logger	LOG							= LoggerFactory.getLogger(UserPositionServiceImpl.class);

	@Autowired
	private UserPositionDao		userPositionDao;

	@Autowired
	private MediasService		mediasService;

	@Autowired
	private TwitterService		twitterService;

	// @Autowired
	private ApnsService			apnsService;

	/**
	 * @param user
	 * @param latitude
	 * @param longitude
	 * @return null if the user doesn't authorize notifications (no apns token)
	 */
	@Async
	public void savePositionForNotifications(final UserBean user, final double latitude, final double longitude) {

		if (user.getApnsToken() != null) {

			Tweet lasTweet = twitterService.lastTweet(latitude, longitude);
			UserPositionBean userPosition = new UserPositionBean();

			userPosition.setLatitude(latitude);
			userPosition.setLongitude(longitude);
			userPosition.setUser(user);
			userPosition.setLastTweetId(lasTweet.getId());

			userPositionDao.saveOrUpdate(userPosition);
		}
	}

	public void launchUserPositionsAnalysis() {
		do {
			retrieveUserPositionsToAnalyse();
			try {
				// Wait 5 minutes => 300000ms
				wait(300000);
			} catch (InterruptedException e) {
				LOG.warn("WTF: " + e.getMessage());
			}
		} while (true);
	}

	/**
	 * 
	 */
	public void retrieveUserPositionsToAnalyse() {

		// 1- Calcul des dates
		Calendar maxInactiveCalendar = Calendar.getInstance();
		maxInactiveCalendar.add(Calendar.MINUTE, -MIN_MINUTES_POSITION_IDLE);
		Date maximumInactiveDate = maxInactiveCalendar.getTime();

		Calendar minInactiveCalendar = Calendar.getInstance();
		minInactiveCalendar.add(Calendar.MINUTE, -MAX_MINUTES_POSITION_IDLE);
		Date minimumInactiveDate = minInactiveCalendar.getTime();

		// 2- find all the Users that could receive a notification
		List<UserPositionBean> list = userPositionDao.listNotifiablesInactiveBetween(minimumInactiveDate,
				maximumInactiveDate);

		for (UserPositionBean userPositionBean : list) {
			Tweet lastTweet = twitterService.lastTweet(userPositionBean.getLatitude(), userPositionBean.getLongitude());

			// We check if this tweet belongs to the user or if it has already been notified
			if ((lastTweet.getId() != userPositionBean.getLastTweetId())
					&& (lastTweet.getFromUserId() != userPositionBean.getLastTweetId())) {

				// Send a notification
				// TODO Send a notification
				sendNotification(userPositionBean.getUser(), "message");

				// Say when a notification has been sent
				notificationSent(userPositionBean);
			} else { // We check if there's a Pict.DiiLo
				MediaBean media = mediasService.lastMedia(userPositionBean, minimumInactiveDate, maximumInactiveDate);
				if (media != null) {
					// send notification
					// TODO Send a notification
					sendNotification(userPositionBean.getUser(), "message");

					// Say when a notification has been sent
					notificationSent(userPositionBean);
				}
			}
		}

	}

	private UserPositionBean notificationSent(final UserPositionBean userPositionBean) {
		userPositionBean.setLastNotificationDate(new Date());
		userPositionDao.update(userPositionBean);
		return userPositionBean;
	}

	private void sendNotification(final UserBean user, final String message) {
		String simplePayload = APNS.newPayload().alertBody(message).badge(45).sound("default").build();
		apnsService.push(user.getApnsToken(), simplePayload);
	}
}