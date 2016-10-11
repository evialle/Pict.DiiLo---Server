/**
 * 
 */
package com.diilo.streetartphone.server.core.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@Table(name = "medias_grades")
public class MediaGradeBean {

	@Id
	@GeneratedValue
	private long	id;

	@Column(nullable = false, length = 128)
	private String	email;

	@Column(nullable = false)
	private String	idMedia;

	@Temporal(TemporalType.TIMESTAMP)
	private Date	date;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the idMedia
	 */
	public String getIdMedia() {
		return idMedia;
	}

	/**
	 * @return the date
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getDate() {
		return date;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param idMedia
	 *            the idMedia to set
	 */
	public void setIdMedia(String idPicture) {
		this.idMedia = idPicture;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

}
