/**
 * 
 */
package com.diilo.streetartphone.server.core.services;

import com.diilo.streetartphone.server.core.entities.UserBean;
import com.diilo.streetartphone.server.core.exceptions.FacebookException;

/**
 * @author Eric
 */
public interface FacebookService {

	enum PrivacyEnum {
		EVERYONE("EVERYONE"), CUSTOM("CUSTOM"), ALL_FRIENDS("ALL_FRIENDS"), NETWORK_FRIENDS("NETWORK_FRIENDS"), FRIENDS_OF_FRIENDS(
				"FRIENDS_OF_FRIENDS");

		private String	value;

		private PrivacyEnum(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return (value);
		}
	}

	/**
	 * @param user
	 * @param link
	 *            (optionnal) url
	 * @param name
	 *            (optionnal) The name of the link
	 * @param picture
	 *            (optionnal) url of the picture
	 * @param caption
	 *            (optionnal) The caption of the link (appears beneath the link name)
	 * @param description
	 *            (optionnal) A description of the link (appears beneath the link caption)
	 * @param message
	 *            (optionnal) The message.
	 * @param source
	 *            (optionnal) A URL to a Flash movie or video file to be embedded within the post.
	 *            read_stream. JSON string containing the URL.
	 * @param privacy
	 *            EVERYONE, CUSTOM, ALL_FRIENDS, NETWORKS_FRIENDS, FRIENDS_OF_FRIENDS
	 * @throws FacebookException
	 */
	boolean addStream(UserBean user, PrivacyEnum privacy, String link, String source, String name, String picture,
			String caption, String description, String message) throws FacebookException;

}
