/**
 * 
 */
package com.diilo.streetartphone.server.core.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
@Table(name = "places")
public class PlaceBean implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6150450946978337543L;

	@Id
	private String				id;

	@Column(length = 64, nullable = false)
	private String				town;

	@Column(length = 64, nullable = false)
	private String				name;

	@Column(name = "start_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				startDate;

	@Column
	private double				longitudeMin;

	@Column
	private double				longitudeMax;

	@Column
	private double				latitudeMin;

	@Column
	private double				latitudeMax;

	@Column(name = "end_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				endDate;

	@Column(name = "creation_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				creationDate;

	@ManyToOne(targetEntity = UserBean.class)
	private UserBean			owner;

	/**
	 * @param town
	 *            the town to set
	 */
	public void setTown(String town) {
		this.town = town;
	}

	/**
	 * @return the town
	 */
	public String getTown() {
		return town;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the startDate
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the endDate
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the creationDate
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(UserBean owner) {
		this.owner = owner;
	}

	/**
	 * @return the owner
	 */
	public UserBean getOwner() {
		return owner;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param longitudeMin
	 *            the longitudeMin to set
	 */
	public void setLongitudeMin(double longitudeMin) {
		this.longitudeMin = longitudeMin;
	}

	/**
	 * @return the longitudeMin
	 */
	public double getLongitudeMin() {
		return longitudeMin;
	}

	/**
	 * @param longitudeMax
	 *            the longitudeMax to set
	 */
	public void setLongitudeMax(double longitudeMax) {
		this.longitudeMax = longitudeMax;
	}

	/**
	 * @return the longitudeMax
	 */
	public double getLongitudeMax() {
		return longitudeMax;
	}

	/**
	 * @param latitudeMin
	 *            the latitudeMin to set
	 */
	public void setLatitudeMin(double latitudeMin) {
		this.latitudeMin = latitudeMin;
	}

	/**
	 * @return the latitudeMin
	 */
	public double getLatitudeMin() {
		return latitudeMin;
	}

	/**
	 * @param latitudeMax
	 *            the latitudeMax to set
	 */
	public void setLatitudeMax(double latitudeMax) {
		this.latitudeMax = latitudeMax;
	}

	/**
	 * @return the latitudeMax
	 */
	public double getLatitudeMax() {
		return latitudeMax;
	}

}
