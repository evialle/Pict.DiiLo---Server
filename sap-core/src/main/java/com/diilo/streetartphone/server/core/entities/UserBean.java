package com.diilo.streetartphone.server.core.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.CollectionOfElements;

import com.diilo.streetartphone.server.core.utils.JsonDateSerializer;

@JsonAutoDetect
@Entity
@Table(name = "users")
public class UserBean implements Serializable {

	public static int			NO_TWITTER_ACCOUNT	= -1;

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4875496789845654654L;

	/** French language. */
	public final static String	LANG_FR				= "fr";

	/** English language. */
	public final static String	LANG_EN				= "en";

	@Id
	@JsonIgnore
	@Column(unique = true, length = 128)
	private String				email;

	@JsonIgnore
	private String				udid;

	@Column(unique = true)
	private String				nickname;

	@Column(length = 2)
	private String				lang;

	@JsonIgnore
	private String				password;

	@JsonIgnore
	@CollectionOfElements(fetch = FetchType.LAZY)
	private List<PlaceBean>		favoritePlaces;

	@Column()
	private int					twitterUserId		= NO_TWITTER_ACCOUNT;

	@JsonIgnore
	@Column(nullable = true)
	private String				twitterAuthToken;

	@JsonIgnore
	@Column(nullable = true)
	private String				twitterAuthSecretToken;

	@JsonIgnore
	@Column(nullable = true)
	private String				facebookAccessToken;

	@JsonIgnore
	@Column(nullable = true)
	private String				apnsToken;

	@Temporal(TemporalType.TIMESTAMP)
	private Date				date;

	/**
	 * @return Locale containing the lang of the user.
	 */
	public Locale getLocale() {
		return new Locale(getLang());
	}

	/**
	 * @return the udid
	 */
	public String getUdid() {
		return udid;
	}

	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param udid
	 *            the udid to set
	 */
	public void setUdid(String udid) {
		this.udid = udid;
	}

	/**
	 * @param nickname
	 *            the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDate() {
		return date;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param twitterAuthToken
	 *            the twitterAuthToken to set
	 */
	public void setTwitterAuthToken(String twitterAuthToken) {
		this.twitterAuthToken = twitterAuthToken;
	}

	/**
	 * @return the twitterAuthToken
	 */
	public String getTwitterAuthToken() {
		return twitterAuthToken;
	}

	/**
	 * @param twitterAuthSecretToken
	 *            the twitterAuthSecretToken to set
	 */
	public void setTwitterAuthSecretToken(String twitterAuthSecretToken) {
		this.twitterAuthSecretToken = twitterAuthSecretToken;
	}

	/**
	 * @return the twitterAuthSecretToken
	 */
	public String getTwitterAuthSecretToken() {
		return twitterAuthSecretToken;
	}

	/**
	 * @param lang
	 *            the lang to set
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * @return true if a twitter account is coupled
	 */
	public boolean isTwitterEnabled() {
		return (getTwitterAuthSecretToken() != null);
	}

	/**
	 * @param favoritePlaces
	 *            the favoritePlaces to set
	 */
	public void setFavoritePlaces(List<PlaceBean> favoritePlaces) {
		this.favoritePlaces = favoritePlaces;
	}

	/**
	 * @return the favoritePlaces
	 */
	public List<PlaceBean> getFavoritePlaces() {
		return favoritePlaces;
	}

	/**
	 * @param facebookAccessToken
	 *            the facebookAccessToken to set
	 */
	public void setFacebookAccessToken(String facebookAccessToken) {
		this.facebookAccessToken = facebookAccessToken;
	}

	/**
	 * @return the facebookAccessToken
	 */
	public String getFacebookAccessToken() {
		return facebookAccessToken;
	}

	public boolean isFacebookEnabled() {
		return (getFacebookAccessToken() != null);
	}

	public int getTwitterUserId() {
		return this.twitterUserId;
	}

	/**
	 * @param twitterUserId
	 *            the twitterUserId to set
	 */
	public void setTwitterUserId(int twitterUserId) {
		this.twitterUserId = twitterUserId;
	}

	/**
	 * @param apnsToken
	 *            the apnsToken to set
	 */
	public void setApnsToken(String apnsToken) {
		this.apnsToken = apnsToken;
	}

	/**
	 * @return the apnsToken
	 */
	public String getApnsToken() {
		return apnsToken;
	}

	@Override
	public String toString() {
		return new StringBuilder(getNickname()).append(" - ").append(getEmail()).toString();
	}
}
