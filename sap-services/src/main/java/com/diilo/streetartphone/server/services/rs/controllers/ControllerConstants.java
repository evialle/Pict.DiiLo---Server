package com.diilo.streetartphone.server.services.rs.controllers;

public class ControllerConstants {

	public static final int STATUS_OK = 1;
	public static final int STATUS_USER_NICKNAMEALREADYEXISTS = 1001;
	public static final int STATUS_USER_UNKNOWN = 1002;
	public static final int STATUS_USER_BADAUTH = 1003;
	public static final int STATUS_USER_EMAILALREADYEXISTS = 1004;

	public static final int STATUS_PICTURE_ALREADYGRADED = 2001;
	public static final int STATUS_PICTURE_UNKNOWN = 2002;

	public static final int STATUS_PLACE_NOTUNIQUE = 3001;
	public static final int STATUS_PLACE_UNKNOWN = 3002;

	public static final int STATUS_TECHNICAL_KO = 9999;

	public static final String STATUS_MODELLABEL = "status";
	public static final String LIST_MODELLABEL = "list";
	public static final String CREDENTIALS_MODELLABEL = "credentials";
	public static final String TWITTER_STATUS_MODELLABEL = "twitter_status";
	public static final String NICKNAME_MODELLABEL = "nickname";
	public static final String FACEBOOK_STATUS_MODELLABEL = "facebook_status";

}
