/**
 * 
 */
package com.diilo.streetartphone.server.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByName;

import com.diilo.streetartphone.server.core.entities.UserBean;
import com.diilo.streetartphone.server.core.exceptions.TechnicalException;
import com.diilo.streetartphone.server.core.exceptions.UserBadAuthentification;
import com.diilo.streetartphone.server.core.exceptions.UserEmailAlreadyExistException;
import com.diilo.streetartphone.server.core.exceptions.UserNicknameAlreadyExistException;
import com.diilo.streetartphone.server.core.exceptions.UserUnknowException;
import com.diilo.streetartphone.server.core.services.UserService;

/**
 * @author Eric
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "classpath:applicationContext-services.xml", "classpath:applicationContext-core.xml",
		"classpath:applicationContext-core-datasource.xml" })
public class UserServiceTest {

	private static final String	NEW_NICKNAME_DIFFERENT	= "new nickname different";
	private static final String	PASSWORD				= "123456";
	private static final String	ERIC_VIALLE_GMAIL_COM	= "eric.vialle@gmail.com";
	private static final String	TEST_NICK83				= "Test'Nick83";

	@SpringBeanByName
	private UserService			userService;

	/**
	 * Test method for
	 * {@link com.diilo.streetartphone.server.services.implementations.UserServiceImpl#createUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @throws UserNicknameAlreadyExistException
	 * @throws UserBadAuthentification
	 * @throws UserUnknowException
	 * @throws UserEmailAlreadyExistException
	 */
	@Test
	public void testCreateUser() throws UserNicknameAlreadyExistException, UserUnknowException,
			UserBadAuthentification, UserEmailAlreadyExistException {
		userService.deleteUserByEmail(ERIC_VIALLE_GMAIL_COM);

		userService.createUser(TEST_NICK83, ERIC_VIALLE_GMAIL_COM, PASSWORD, "1234-5789-7588-555", "fr", "66.66.66.66");
		UserBean user = userService.findByEmailAndPassword(ERIC_VIALLE_GMAIL_COM, PASSWORD);
		assertNotNull(user);
	}

	/**
	 * Test method for
	 * {@link com.diilo.streetartphone.server.services.implementations.UserServiceImpl#findByEmailAndPassword(java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @throws UserBadAuthentification
	 * @throws UserUnknowException
	 */
	@Test
	public void testFindByEmailAndPassword() throws UserBadAuthentification, UserUnknowException {
		UserBean user;
		user = userService.findByEmailAndPassword(ERIC_VIALLE_GMAIL_COM, PASSWORD);
		assertEquals(ERIC_VIALLE_GMAIL_COM, user.getEmail());
		assertEquals(TEST_NICK83.toLowerCase(), user.getNickname().toLowerCase());

	}

	/**
	 * Test method for
	 * {@link com.diilo.streetartphone.server.services.implementations.UserServiceImpl#createSessionCredential(java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @throws UserBadAuthentification
	 * @throws UserUnknowException
	 */
	@Test
	public void testCreateSessionCredential() throws UserUnknowException, UserBadAuthentification {
		userService.createSessionCredential(ERIC_VIALLE_GMAIL_COM, PASSWORD);
	}

	/**
	 * Test method for
	 * {@link com.diilo.streetartphone.server.services.implementations.UserServiceImpl#getUserByCredentials(java.lang.String)}
	 * .
	 * 
	 * @throws UserBadAuthentification
	 * @throws UserUnknowException
	 */
	@Test
	public void testGetUserByCredentials() throws UserUnknowException, UserBadAuthentification {
		String credentials = userService.createSessionCredential(ERIC_VIALLE_GMAIL_COM, PASSWORD);
		UserBean user = userService.getUserByCredentials(credentials);
		assertEquals(user.getNickname(), user.getNickname());
	}

	/**
	 * Test method for
	 * {@link com.diilo.streetartphone.server.services.implementations.UserServiceImpl#deleteUserByEmail(java.lang.String)}
	 * .
	 */
	@Test
	public void testDeleteUserByEmail() {
		boolean resultTrue = userService.deleteUserByEmail(ERIC_VIALLE_GMAIL_COM);
		assertEquals(true, resultTrue);
		// should'nt not send an error
		boolean resultFalse = userService.deleteUserByEmail("adresse@inexistante.com");
		assertEquals(false, resultFalse);
	}

	/**
	 * @throws UserUnknowException
	 * @throws TechnicalException
	 */
	@Test
	public void testSendPassword() throws UserUnknowException, TechnicalException {
		userService.sendPasswordByMail("eric@vialle.org");
	}

	@Test
	public void testChangePassword() throws UserUnknowException, TechnicalException, UserBadAuthentification {

		// 1- need credentials first !!!
		userService.findByEmailAndPassword("eric@vialle.org", PASSWORD);
		userService.changePassword("eric@vialle.org", PASSWORD, "1234567");
		userService.findByEmailAndPassword("eric@vialle.org", "1234567");
		userService.changePassword("eric@vialle.org", "1234567", PASSWORD);
	}

	@Test
	public void testUpdateCredentials() throws UserUnknowException, UserBadAuthentification {
		UserBean userBean = userService.findByEmailAndPassword("eric@vialle.org", PASSWORD);
		String credentials = userService.createSessionCredential("eric@vialle.org", PASSWORD);

		userBean.setNickname(NEW_NICKNAME_DIFFERENT);
		userService.updateSessionCredential(credentials, userBean);

		assertEquals(NEW_NICKNAME_DIFFERENT, userService.getUserByCredentials(credentials).getNickname());
	}
}
