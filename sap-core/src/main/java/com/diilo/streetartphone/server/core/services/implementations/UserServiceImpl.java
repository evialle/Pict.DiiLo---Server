/**
 * 
 */
package com.diilo.streetartphone.server.core.services.implementations;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import twitter4j.http.AccessToken;

import com.diilo.streetartphone.server.core.PropertiesCode;
import com.diilo.streetartphone.server.core.dao.UserDao;
import com.diilo.streetartphone.server.core.entities.UserBean;
import com.diilo.streetartphone.server.core.exceptions.TechnicalException;
import com.diilo.streetartphone.server.core.exceptions.UserBadAuthentification;
import com.diilo.streetartphone.server.core.exceptions.UserEmailAlreadyExistException;
import com.diilo.streetartphone.server.core.exceptions.UserNicknameAlreadyExistException;
import com.diilo.streetartphone.server.core.exceptions.UserUnknowException;
import com.diilo.streetartphone.server.core.services.UserService;
import com.diilo.streetartphone.server.core.utils.EhCacheUtils;
import com.diilo.streetartphone.server.core.utils.EmailManagerService;

/**
 * @author Eric
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	private static final Logger	LOG	= LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDao				userDao;

	@Autowired
	@Qualifier("messageSource")
	private MessageSource		messageSource;

	@Autowired
	private EmailManagerService	emailManager;

	@Override
	/**
	 * @return the user bean
	 */
	public UserBean createUser(final String nickname, final String email, final String password, final String udid,
			final String lang, String ipAddress) throws UserNicknameAlreadyExistException,
			UserEmailAlreadyExistException {

		// 1- check if the nickname is already taken
		if (nicknameExists(nickname)) {
			LOG.info("The phone tried to log in with an already taken nickname: " + nickname + " (" + email
					+ ") - udid: " + udid + " - IP: " + ipAddress);
			throw new UserNicknameAlreadyExistException();
		} else if (emailExists(email)) {
			throw new UserEmailAlreadyExistException();
		} else {
			UserBean userBean = new UserBean();

			// 2- Save it in the database
			userBean.setNickname(nickname.trim());
			userBean.setUdid(udid);
			userBean.setEmail(email.trim().toLowerCase());
			userBean.setPassword(password.trim());
			userBean.setLang(lang);
			userBean.setDate(new Date());

			userDao.save(userBean);
			LOG.info("A new user has been added. nickname: " + nickname + " (" + email + ") - udid: " + udid
					+ " - IP: " + ipAddress);

			// 3- send the e-mail with the secret
			sendInscriptionEmail(userBean);

			return userBean;
		}
	}

	/**
	 * @param email
	 * @return true if the user is in our database.
	 */
	private boolean emailExists(final String email) {
		return (userDao.getByEmail(email) != null);
	}

	/** Send an email to tell to the user that the subscription has been successful. */
	@Async
	private void sendInscriptionEmail(final UserBean userBean) {
		try {
			String emailSubject = messageSource.getMessage(PropertiesCode.SERVICE_USER_CREATION_SUBJECT, null,
					userBean.getLocale());

			String emailContent = messageSource.getMessage(PropertiesCode.SERVICE_USER_CREATION_CONTENT,
					new Object[] { userBean.getNickname() }, userBean.getLocale());

			emailManager.sendEmail(userBean.getNickname(), userBean.getEmail(), emailSubject, emailContent);
		} catch (MailException me) {
			LOG.error("A subscription email hasn't been sent to User: " + userBean + ".");
		}
	}

	private boolean nicknameExists(String nickname) {
		return (userDao.getByNickname(nickname) != null);
	}

	@Override
	public UserBean findByEmailAndPassword(final String email, final String password) throws UserUnknowException,
			UserBadAuthentification {
		UserBean user = userDao.getByEmail(email);
		if (user == null) {
			throw new UserUnknowException();
		} else if (user.getPassword().equals(password)) {
			return user;
		} else {
			throw new UserBadAuthentification();
		}
	}

	@Override
	public String createSessionCredential(final String email, final String password) throws UserUnknowException,
			UserBadAuthentification {
		UserBean user = findByEmailAndPassword(email, password);
		String credentials = UUID.randomUUID().toString();

		EhCacheUtils.getInstance(EhCacheUtils.CREDENTIALS).addToCache(credentials, user);

		return credentials;
	}

	@Override
	public void updateSessionCredential(final String credentials, final UserBean userBean) {
		EhCacheUtils.getInstance(EhCacheUtils.CREDENTIALS).deleteFromCache(credentials);
		EhCacheUtils.getInstance(EhCacheUtils.CREDENTIALS).addToCache(credentials, userBean);
	}

	@Override
	public UserBean getUserByCredentials(final String credentials) throws UserBadAuthentification {
		UserBean user = (UserBean) EhCacheUtils.getInstance(EhCacheUtils.CREDENTIALS).getFromCache(credentials);
		if (user == null) {
			throw new UserBadAuthentification();
		} else {
			return user;
		}
	}

	@Override
	public boolean deleteUserByEmail(final String email) {
		UserBean user = userDao.getByEmail(email);
		if (user != null) {
			userDao.delete(user);
			LOG.info("User " + email + " has been deleted");
			return true;
		} else {
			LOG.info("User " + email + " can't be deleted");
			return false;
		}

	}

	@Override
	public void sendPasswordByMail(String email) throws UserUnknowException, TechnicalException {
		UserBean userBean = userDao.getByEmail(email);

		if (userBean != null) {
			try {
				String emailSubject = messageSource.getMessage(PropertiesCode.SERVICE_USER_EMAIL_LOST_PASSWORD_SUBJECT,
						null, userBean.getLocale());

				String emailContent = messageSource.getMessage(PropertiesCode.SERVICE_USER_EMAIL_LOST_PASSWORD_CONTENT,
						new Object[] { userBean.getPassword() }, userBean.getLocale());

				emailManager.sendEmail(userBean.getNickname(), userBean.getEmail(), emailSubject, emailContent);
			} catch (MailException me) {
				LOG.error("A password email has been sent to User: " + userBean + ".");
				throw new TechnicalException();
			}
		} else {
			throw new UserUnknowException();
		}
	}

	@Override
	public UserBean changePassword(String email, String oldPassword, String newPassword) throws UserUnknowException,
			UserBadAuthentification {
		UserBean userBean = userDao.getByEmail(email);

		if (userBean != null) {
			if (userBean.getPassword().equals(oldPassword)) {
				// update in db
				userBean.setPassword(newPassword);
				userDao.update(userBean);

				return userBean;
			} else {
				throw new UserBadAuthentification();
			}

		} else {
			throw new UserUnknowException();
		}
	}

	@Override
	public UserBean updateTwitterToken(final int idTwitterUser, final String credentials, final AccessToken token)
			throws UserBadAuthentification {

		UserBean user = this.getUserByCredentials(credentials);

		LOG.debug("Update Twitter token for: " + user.getEmail() + " with " + token);

		// Update in database
		userDao.setTwitterTokenToUsersEmail(user.getEmail(), idTwitterUser, token.getToken(), token.getTokenSecret());

		// Update session
		user.setTwitterAuthToken(token.getToken());
		user.setTwitterAuthSecretToken(token.getTokenSecret());
		EhCacheUtils.getInstance(EhCacheUtils.CREDENTIALS).deleteFromCache(credentials);
		EhCacheUtils.getInstance(EhCacheUtils.CREDENTIALS).addToCache(credentials, user);

		return user;
	}

	@Override
	public UserBean disableTwitterToken(final String credentials) throws UserBadAuthentification {
		return updateTwitterToken(0, credentials, new AccessToken("", ""));
	}

	@Override
	public boolean isTwitterEnabled(final String email) {
		UserBean user = userDao.getByEmail(email);
		return (user.getTwitterAuthToken() != null);
	}

	@Override
	public UserBean updateFacebookToken(final String credentials, final String facebookAccessToken)
			throws UserBadAuthentification {
		UserBean user = this.getUserByCredentials(credentials);

		LOG.debug("Update Fb token for: " + user.getEmail() + " with " + facebookAccessToken);

		// Update in database
		userDao.setFacebookTokenToUsersEmail(user.getEmail(), facebookAccessToken);

		// Update session
		user.setFacebookAccessToken(facebookAccessToken);
		EhCacheUtils.getInstance(EhCacheUtils.CREDENTIALS).deleteFromCache(credentials);
		EhCacheUtils.getInstance(EhCacheUtils.CREDENTIALS).addToCache(credentials, user);

		return user;
	}

	@Override
	public UserBean disableFacebookToken(final String credentials) throws UserBadAuthentification {
		return updateFacebookToken(credentials, "");
	}

	@Override
	public UserBean disableFacebookToken(final UserBean user) {
		user.setFacebookAccessToken(null);
		userDao.update(user);
		LOG.debug("The Facebook account has been disabled for user: " + user);
		return user;
	}

	@Override
	public boolean isFacebookEnabled(final String email) {
		UserBean user = userDao.getByEmail(email);
		return (user.getFacebookAccessToken() != null);
	}
}
