/**
 * 
 */
package com.diilo.streetartphone.server.core.services.implementations;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diilo.streetartphone.server.core.entities.UserBean;
import com.diilo.streetartphone.server.core.exceptions.FacebookException;
import com.diilo.streetartphone.server.core.services.FacebookService;
import com.diilo.streetartphone.server.core.services.UserService;

/**
 * @author Eric
 */
@Service
public class FacebookServiceImpl implements FacebookService {

	private final static String	FACEBOOK_FEED_URL		= "https://graph.facebook.com/me/feed";

	private final static String	MESSAGE_INVALID_TOKEN	= "Invalid access token signature.";

	private final static int	NB_OF_RETRY				= 3;

	private static final Logger	LOG						= LoggerFactory.getLogger(MediaServiceImpl.class);

	@Autowired
	private UserService			userService;

	private static boolean isFieldNotEmpty(String field) {
		return ((field != null) && (field.trim().equals("") == false));
	}

	@Override
	public boolean addStream(UserBean user, PrivacyEnum privacy, String link, String source, String name,
			String picture, String caption, String description, String message) throws FacebookException {

		int retry = 0;

		do {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(FACEBOOK_FEED_URL);

			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

			try {

				if (user.getFacebookAccessToken().contains("&expires=")) {
					String[] facebookTokenArray = user.getFacebookAccessToken().split("&expires=");
					reqEntity.addPart("access_token", new StringBody(facebookTokenArray[0]));
					reqEntity.addPart("expires", new StringBody(facebookTokenArray[1]));
				} else {
					reqEntity.addPart("access_token", new StringBody(user.getFacebookAccessToken()));
				}
				// reqEntity.addPart("privacy", new StringBody(privacy.toString()));

				if (isFieldNotEmpty(link)) {
					reqEntity.addPart("link", new StringBody(link));
				}
				if (isFieldNotEmpty(source)) {
					reqEntity.addPart("source", new StringBody(source));
				}
				if (isFieldNotEmpty(name)) {
					reqEntity.addPart("name", new StringBody(name));
				}
				if (isFieldNotEmpty(picture)) {
					reqEntity.addPart("picture", new StringBody(picture));
				}
				if (isFieldNotEmpty(caption)) {
					reqEntity.addPart("caption", new StringBody(caption));
				}
				if (isFieldNotEmpty(description)) {
					reqEntity.addPart("description", new StringBody(description));
				}
				if (isFieldNotEmpty(message)) {
					reqEntity.addPart("message", new StringBody(message));
				}

				httppost.setEntity(reqEntity);

				LOG.debug("executing request " + httppost.getRequestLine());
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity resEntity = response.getEntity();

				if (resEntity != null) {
					String page = EntityUtils.toString(resEntity);
					LOG.debug("Facebook return: " + page);

					if (page.contains(MESSAGE_INVALID_TOKEN)) {
						// disable Facebook Account for this user
						userService.disableFacebookToken(user);
						break;
					} else {
						// THAT'S OK!
						break;
					}
				}

			} catch (UnsupportedEncodingException e) {
				LOG.error("UnsupportedEncodingException for file: ");
				throw new FacebookException();

			} catch (ParseException e) {
				LOG.error("ParseException for file: ");
				throw new FacebookException();

			} catch (IOException e) {
				LOG.error("IOException for file: ");
				throw new FacebookException();
			}

			retry++;
		} while (retry <= NB_OF_RETRY);
		return true;
	}
}
