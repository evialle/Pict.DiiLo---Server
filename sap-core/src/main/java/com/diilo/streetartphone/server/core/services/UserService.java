/**
 * 
 */
package com.diilo.streetartphone.server.core.services;

import twitter4j.http.AccessToken;

import com.diilo.streetartphone.server.core.entities.UserBean;
import com.diilo.streetartphone.server.core.exceptions.TechnicalException;
import com.diilo.streetartphone.server.core.exceptions.UserBadAuthentification;
import com.diilo.streetartphone.server.core.exceptions.UserEmailAlreadyExistException;
import com.diilo.streetartphone.server.core.exceptions.UserNicknameAlreadyExistException;
import com.diilo.streetartphone.server.core.exceptions.UserUnknowException;

/**
 * @author Eric
 */
public interface UserService {

	UserBean createUser(final String nickname, final String email, final String password, final String udid,
			final String ipAddress, final String lang) throws UserNicknameAlreadyExistException,
			UserEmailAlreadyExistException;

	String createSessionCredential(final String email, final String password) throws UserUnknowException,
			UserBadAuthentification;

	UserBean getUserByCredentials(final String credentials) throws UserBadAuthentification;

	UserBean findByEmailAndPassword(final String email, final String password) throws UserUnknowException,
			UserBadAuthentification;

	boolean deleteUserByEmail(final String email);

	void sendPasswordByMail(final String email) throws UserUnknowException, TechnicalException;

	UserBean changePassword(final String email, final String oldPassword, final String newPassword)
			throws UserUnknowException, UserBadAuthentification;

	void updateSessionCredential(final String credentials, final UserBean userBean);

	UserBean disableTwitterToken(String credentials) throws UserBadAuthentification;

	UserBean updateTwitterToken(int idTwitter, String credentials, AccessToken token) throws UserBadAuthentification;

	UserBean updateFacebookToken(String credentials, String token) throws UserBadAuthentification;

	boolean isTwitterEnabled(final String email);

	UserBean disableFacebookToken(String credentials) throws UserBadAuthentification;

	UserBean disableFacebookToken(UserBean user);

	boolean isFacebookEnabled(String email);

}
