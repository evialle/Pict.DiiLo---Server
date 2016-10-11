/**
 * 
 */
package com.diilo.streetartphone.server.services;

import static org.junit.Assert.fail;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByName;

import com.diilo.streetartphone.server.core.entities.PlaceBean;
import com.diilo.streetartphone.server.core.entities.UserBean;
import com.diilo.streetartphone.server.core.exceptions.NotUniquePlaceException;
import com.diilo.streetartphone.server.core.exceptions.UserBadAuthentification;
import com.diilo.streetartphone.server.core.exceptions.UserUnknowException;
import com.diilo.streetartphone.server.core.services.PlaceService;
import com.diilo.streetartphone.server.core.services.UserService;

/**
 * @author Eric
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "classpath:applicationContext-services.xml", "classpath:applicationContext-core.xml",
		"classpath:applicationContext-core-datasource.xml" })
public class PlaceServiceTest {

	private static final String	EMAIL_ERIC_VIALLE_GMAIL_COM	= "eric.vialle2@gmail.com";
	private static final String	PASSWORD_123456				= "123456";

	@SpringBeanByName
	private PlaceService		placeService;

	@SpringBeanByName
	private UserService			userService;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for
	 * {@link com.diilo.streetartphone.server.services.implementations.PlaceServiceImpl#addPlace(java.lang.String, java.lang.String, double, double, com.diilo.streetartphone.server.entities.UserBean, double)}
	 * .
	 * 
	 * @throws NotUniquePlaceException
	 * @throws UserBadAuthentification
	 * @throws UserUnknowException
	 */
	@Test
	public void testAddPlaceStringStringDoubleDoubleUserBeanDouble() throws NotUniquePlaceException,
			UserUnknowException, UserBadAuthentification {
		UserBean user = userService.findByEmailAndPassword(EMAIL_ERIC_VIALLE_GMAIL_COM, PASSWORD_123456);
		placeService.addPlace("Nogent Sur Marne", "Chez éric!!", 1.0, 2.0, user, 200.0);
	}

	/**
	 * Test method for
	 * {@link com.diilo.streetartphone.server.services.implementations.PlaceServiceImpl#addPlace(java.lang.String, java.lang.String, double, double, com.diilo.streetartphone.server.entities.UserBean, double, java.util.Date, java.util.Date)}
	 * .
	 * 
	 * @throws NotUniquePlaceException
	 * @throws UserBadAuthentification
	 * @throws UserUnknowException
	 */
	@Test
	public void testAddPlaceStringStringDoubleDoubleUserBeanDoubleDateDate() throws NotUniquePlaceException,
			UserUnknowException, UserBadAuthentification {
		UserBean user = userService.findByEmailAndPassword(EMAIL_ERIC_VIALLE_GMAIL_COM, PASSWORD_123456);
		PlaceBean place = placeService.addPlace("Nogent Sur Marne", "Chez éric 2 !!", 1.0, 2.0, user, 200.0,
				new Date(), new Date());
		Assert.assertNotNull(place);
	}

	/**
	 * Test method for
	 * {@link com.diilo.streetartphone.server.services.implementations.PlaceServiceImpl#addPlaceAsFavorite(com.diilo.streetartphone.server.entities.UserBean, java.lang.String)}
	 * .
	 */
	@Test
	public void testAddPlaceAsFavorite() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.diilo.streetartphone.server.services.implementations.PlaceServiceImpl#listPlaces(double, double)}
	 * .
	 */
	@Test
	public void testListPlaces() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.diilo.streetartphone.server.services.implementations.PlaceServiceImpl#normalizer(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testNormalizer() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.diilo.streetartphone.server.services.implementations.PlaceServiceImpl#removePlaceAsFavorite(com.diilo.streetartphone.server.entities.UserBean, java.lang.String)}
	 * .
	 */
	@Test
	public void testRemovePlaceAsFavorite() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.diilo.streetartphone.server.services.implementations.PlaceServiceImpl#deletePlace(java.lang.String, com.diilo.streetartphone.server.entities.UserBean)}
	 * .
	 */
	@Test
	public void testDeletePlace() {
		fail("Not yet implemented");
	}

}
