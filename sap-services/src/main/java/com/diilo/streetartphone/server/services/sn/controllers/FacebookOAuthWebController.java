/**
 * 
 */
package com.diilo.streetartphone.server.services.sn.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.diilo.streetartphone.server.core.exceptions.UserBadAuthentification;
import com.diilo.streetartphone.server.core.services.UserService;

/**
 * @author Eric
 */
@Controller
public class FacebookOAuthWebController {

	@Value("#{configServicesProperties['facebook.app.secret']}")
	private String				SECRET;

	@Value("#{configServicesProperties['facebook.app.api.id']}")
	private String				API_KEY;

	@Value("#{configServicesProperties['facebook.app.id']}")
	private String				CLIENT_ID;

	private final static String	FB_OAUTH_URI	= "https://graph.facebook.com/oauth/";

	private static final Logger	LOG				= LoggerFactory.getLogger(FacebookOAuthWebController.class);

	@Autowired
	private UserService			userService;

	@RequestMapping("/facebook/{credentials}/login")
	public ModelAndView loginFacebook(@PathVariable String credentials, final HttpServletRequest request) {

		// Build the callback url

		String view = "redirect:" + FB_OAUTH_URI
				+ "authorize?scope=user_photos,user_videos,publish_stream&display=touch&client_id=" + CLIENT_ID
				+ "&redirect_uri=" + builRedirectUri(credentials, request);

		ModelAndView mav = new ModelAndView(view);
		return mav;
	}

	@RequestMapping("/facebook/unsubcribe/pingfacebook")
	public ModelAndView unsubscribe() {
		return null;
	}

	private String builRedirectUri(String credentials, final HttpServletRequest request) {
		return new StringBuilder(request.getScheme()).append("://").append(request.getServerName()).append(":")
				.append(request.getServerPort()).append(request.getContextPath()).append("/web/facebook/")
				.append(credentials).append("/oauth_accesstoken").toString();
	}

	@RequestMapping("/facebook/{credentials}/oauth_accesstoken")
	public ModelAndView accessTokenRetrieve(@PathVariable String credentials, @RequestParam String code,
			final HttpServletRequest request) {

		LOG.debug(credentials + " - " + code);
		ModelAndView mav = new ModelAndView();

		try {
			String urlAccessToken = "https://graph.facebook.com/oauth/access_token?client_id="
					+ URLEncoder.encode(CLIENT_ID, "UTF-8") + "&redirect_uri="
					+ URLEncoder.encode(builRedirectUri(credentials, request), "UTF-8") + "&client_secret="
					+ URLEncoder.encode(SECRET, "UTF-8") + "&code=" + URLEncoder.encode(code, "UTF-8");

			LOG.debug("URL ACCESS TOKEN: " + urlAccessToken);

			String accessTokenPage = contentPage(urlAccessToken);
			LOG.debug("ACCESS TOKEN PAGE: " + accessTokenPage);

			// find the access_token in the json
			Pattern p = Pattern.compile("access_token=(.+)");
			Matcher m = p.matcher(accessTokenPage);
			String accessToken = null;
			while (m.find()) {
				accessToken = m.group(1).trim();
			}
			if (accessToken != null) {
				LOG.info("Access Token: " + accessToken);

				// Retrieve users and credentials
				userService.updateFacebookToken(credentials, accessToken);

				mav.setViewName("facebook/success");
			} else {
				mav.setViewName("facebook/badauth");
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mav.setViewName("facebook/badauth");
		} catch (UserBadAuthentification e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mav.setViewName("facebook/badauth");
		}

		return mav;
	}

	private String contentPage(String url) {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpGet);
			InputStream content = response.getEntity().getContent();
			InputStreamReader inputStream = new InputStreamReader(content);
			BufferedReader br = new BufferedReader(inputStream);
			String str = br.readLine();
			br.close();
			inputStream.close();

			return str;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} finally {

		}

	}

	public String updateStatus(@RequestParam("status") String fbStatus, @RequestParam("accessToken") String accessToken) {

		return "redirect:login.htm";
	}
}
