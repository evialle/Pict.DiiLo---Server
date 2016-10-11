/**
 * 
 */
package com.diilo.streetartphone.server.core.entities;

import java.io.Serializable;
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
@Table(name = "reports")
public class ReportBean implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4484908414508771650L;

	@Id
	@GeneratedValue
	private long				id;

	@Column(nullable = false)
	private String				email;

	@Column(nullable = false, length = 8)
	private String				idMedia;

	@Column(nullable = true)
	private String				comments;

	@Temporal(TemporalType.TIMESTAMP)
	private Date				date;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
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

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

}
