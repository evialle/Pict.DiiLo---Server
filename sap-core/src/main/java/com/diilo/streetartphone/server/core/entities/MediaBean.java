/**
 * 
 */
package com.diilo.streetartphone.server.core.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.diilo.streetartphone.server.core.utils.JsonDateSerializer;

/**
 * @author Eric
 */
@JsonAutoDetect
@Entity
@Table(name = "medias")
public class MediaBean implements Serializable {

	public static enum MediaTypeEnum {
		VIDEO_MP4_TYPE, PICTURE_JPG_TYPE;
	}

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6557878765167040215L;

	@Id
	@Column(nullable = false, length = 8)
	private String				fileId;

	@Column(nullable = false)
	private String				nickname;

	@Column(length = 255, nullable = false)
	private String				description;

	@Column(length = 255)
	private String				urlRegularImage;

	@Column(length = 255, nullable = true)
	private String				urlRegularVideo;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date				date;

	@Column(name = "vertical")
	private boolean				isVertical;

	@Column
	private double				longitude;
	@Column
	private double				latitude;

	@Column
	private int					positiveGrade;
	@Column
	private int					negativeGrade;

	@Column
	private MediaTypeEnum		mediaType;

	@Override
	public String toString() {
		return "Media " + fileId + " by " + nickname + " (" + longitude + ", " + latitude + ") - " + description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the date
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDate() {
		return date;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @return the fileId
	 */
	public String getFileId() {
		return fileId;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @param idPicture
	 *            the fileId to set
	 */
	public void setFileId(String idPicture) {
		this.fileId = idPicture;
	}

	/**
	 * @param nickname
	 *            the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @return the positiveGrade
	 */
	public int getPositiveGrade() {
		return positiveGrade;
	}

	/**
	 * @return the negativeGrade
	 */
	public int getNegativeGrade() {
		return negativeGrade;
	}

	/**
	 * @param positiveGrade
	 *            the positiveGrade to set
	 */
	public void setPositiveGrade(int positiveGrade) {
		this.positiveGrade = positiveGrade;
	}

	/**
	 * @param negativeGrade
	 *            the negativeGrade to set
	 */
	public void setNegativeGrade(int negativeGrade) {
		this.negativeGrade = negativeGrade;
	}

	/**
	 * @param isVertical
	 *            the isVertical to set
	 */
	public void setVertical(boolean isVertical) {
		this.isVertical = isVertical;
	}

	/**
	 * @return the isVertical
	 */
	public boolean isVertical() {
		return isVertical;
	}

	/**
	 * @param urlRegularImage
	 *            the urlRegularImage to set
	 */
	public void setUrlRegularImage(String urlRegularImage) {
		this.urlRegularImage = urlRegularImage;
	}

	/**
	 * @return the urlRegularImage
	 */
	public String getUrlRegularImage() {
		return urlRegularImage;
	}

	/**
	 * @param mediaType
	 *            the mediaType to set
	 */
	public void setMediaType(MediaTypeEnum mediaType) {
		this.mediaType = mediaType;
	}

	/**
	 * @return the mediaType
	 */
	public MediaTypeEnum getMediaType() {
		return mediaType;
	}

	/**
	 * @param urlRegularVideo
	 *            the urlRegularVideo to set
	 */
	public void setUrlRegularVideo(String urlRegularVideo) {
		this.urlRegularVideo = urlRegularVideo;
	}

	/**
	 * @return the urlRegularVideo
	 */
	public String getUrlRegularVideo() {
		return urlRegularVideo;
	}

}
