/**
 * 
 */
package com.diilo.streetartphone.server.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

import com.diilo.streetartphone.server.core.exceptions.UserBadAuthentification;
import com.diilo.streetartphone.server.core.exceptions.UserEmailAlreadyExistException;
import com.diilo.streetartphone.server.core.exceptions.UserNicknameAlreadyExistException;
import com.diilo.streetartphone.server.core.exceptions.UserUnknowException;
import com.diilo.streetartphone.server.core.services.UserService;
import com.diilo.streetartphone.server.services.rs.controllers.ControllerConstants;
import com.diilo.streetartphone.server.services.rs.controllers.MediasManagerRemoteController;

/**
 * @author Eric
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "classpath:applicationContext-services.xml", "classpath:applicationContext-core.xml",
		"classpath:applicationContext-core-datasource.xml" })
public class MediasManagerRemoteControllerTest {

	private static final String				PASSWORD_TESTUNIT	= "123456";

	private static final String				EMAIL_TESTUNIT		= "test1@test-unit.com";

	private static final Logger				LOG					= LoggerFactory
																		.getLogger(MediasManagerRemoteControllerTest.class);

	@SpringBeanByType
	private MediasManagerRemoteController	mediasManagerRemoteController;

	@SpringBeanByType
	private UserService						userService;

	private String							credentials;

	private final String					pictureVertFile		= this.getClass().getResource("/img/srcVer.JPG")
																		.getFile();

	@Before
	public void before() throws UserUnknowException, UserBadAuthentification, UserNicknameAlreadyExistException,
			UserEmailAlreadyExistException {
		userService.deleteUserByEmail(EMAIL_TESTUNIT);
		userService.createUser(EMAIL_TESTUNIT, EMAIL_TESTUNIT, PASSWORD_TESTUNIT, "21321625", "fr", "0.0.0.0");

		credentials = userService.createSessionCredential(EMAIL_TESTUNIT, PASSWORD_TESTUNIT);
		LOG.debug("Credentials: " + credentials);
	}

	/**
	 * Test method for
	 * {@link com.diilo.streetartphone.server.services.rs.controllers.MediasManagerRemoteController#postNewPicture(java.lang.String, java.lang.String, double, double, org.springframework.web.multipart.MultipartFile)}
	 * .
	 * 
	 * @throws IOException
	 */
	public void testPostNewPicture() throws IOException {
		InputStream inputStream = new FileInputStream(pictureVertFile);
		MultipartFile pictureData = new MockMultipartFile("pictureData", inputStream);
		mediasManagerRemoteController.postNewMedia(credentials, "this is a description", 1.0, 1.0, pictureData, false,
				false);
	}

	/**
	 * Test method for
	 * {@link com.diilo.streetartphone.server.services.rs.controllers.MediasManagerRemoteController#getAllPictures(java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @throws UserBadAuthentification
	 * @throws UserUnknowException
	 */
	@Test
	public void testGetAllPictures() throws UserUnknowException, UserBadAuthentification {
		ModelAndView mav = mediasManagerRemoteController.getAllPictures(credentials, "test");
		List list = (List) mav.getModel().get(ControllerConstants.LIST_MODELLABEL);
		assertNotNull(list);
		assertEquals(0, list.size());

		mav = mediasManagerRemoteController.getAllPictures(credentials, "Test-Unit");
		list = (List) mav.getModel().get(ControllerConstants.LIST_MODELLABEL);
		assertNotNull(list);
		assertEquals(3, list.size());

	}

	/**
	 * Test method for
	 * {@link com.diilo.streetartphone.server.services.rs.controllers.MediasManagerRemoteController#getPicturesInPerimeter(java.lang.String, double, double)}
	 * .
	 */
	@Test
	public void testGetPicturesInPerimeter() {
		double latitude = 2.0;
		double longitude = 2.0;
		ModelAndView mav = mediasManagerRemoteController.getPicturesInPerimeter(latitude, longitude);
		assertNotNull(mav.getModel().get(ControllerConstants.LIST_MODELLABEL));
		List picturesList = (List) mav.getModel().get(ControllerConstants.LIST_MODELLABEL);
		assertEquals(2, picturesList.size());
	}

	/**
	 * Test method for
	 * {@link com.diilo.streetartphone.server.services.rs.controllers.MediasManagerRemoteController#getPicturesAndCredentialsInPerimeter(double, double, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetPicturesAndCredentialsInPerimeter() {
		double latitude = 2.0;
		double longitude = 2.0;
		ModelAndView mav = mediasManagerRemoteController.getPicturesAndCredentialsInPerimeter("myUDID", latitude,
				longitude, EMAIL_TESTUNIT, PASSWORD_TESTUNIT);
		assertNotNull(mav.getModel().get(ControllerConstants.LIST_MODELLABEL));
		assertNotNull(mav.getModel().get(ControllerConstants.CREDENTIALS_MODELLABEL));
		List picturesList = (List) mav.getModel().get(ControllerConstants.LIST_MODELLABEL);
		assertEquals(2, picturesList.size());
	}

	@Test
	public void testAddPositiveGrade() {
		String idPicture = "1";
		ModelAndView mav = mediasManagerRemoteController.addPositiveGrade(credentials, idPicture);
		assertNotNull(mav.getModel().get(ControllerConstants.STATUS_MODELLABEL));
	}

	@Test
	public void testAddNegativeGrade() {
		String idPicture = "1";
		ModelAndView mav = mediasManagerRemoteController.addNegativeGrade(credentials, idPicture);
		assertNotNull(mav.getModel().get(ControllerConstants.STATUS_MODELLABEL));
	}
}
