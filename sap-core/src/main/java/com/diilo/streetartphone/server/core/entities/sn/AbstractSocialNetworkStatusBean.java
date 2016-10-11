/**
 * 
 */
package com.diilo.streetartphone.server.core.entities.sn;

import java.util.Date;

/**
 * @author Eric
 */
public abstract class AbstractSocialNetworkStatusBean {

	private Date	date;

	private String	contentText;

	public abstract int getType();

	/**
	 * @param date the date to set
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

	/**
	 * @param contentText the contentText to set
	 */
	public void setContentText(String contentText) {
		this.contentText = contentText;
	}

	/**
	 * @return the contentText
	 */
	public String getContentText() {
		return contentText;
	}

}
