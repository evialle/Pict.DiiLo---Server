/**
 * 
 */
package com.diilo.streetartphone.server.core.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Entity;

/**
 * @author Eric
 */
@Entity
@Table(name = "users_positions")
public class UserPositionBean implements Serializable {

	@Id
	@OneToOne
	private UserBean	user;

	private double		latitude, longitude;

	private long		lastTweetId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date		date;

	@Temporal(TemporalType.TIMESTAMP)
	private Date		lastNotificationDate;

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(UserBean user) {
		this.user = user;
	}

	/**
	 * @return the user
	 */
	public UserBean getUser() {
		return user;
	}

	/**
	 * @param lastTweetId
	 *            the lastTweetId to set
	 */
	public void setLastTweetId(long lastTweet) {
		this.lastTweetId = lastTweet;
	}

	/**
	 * @return the lastTweetId
	 */
	public long getLastTweetId() {
		return lastTweetId;
	}

	/**
	 * @param lastNotificationDate
	 *            the lastNotificationDate to set
	 */
	public void setLastNotificationDate(Date lastNotificationDate) {
		this.lastNotificationDate = lastNotificationDate;
	}

	/**
	 * @return the lastNotificationDate
	 */
	public Date getLastNotificationDate() {
		return lastNotificationDate;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
}
