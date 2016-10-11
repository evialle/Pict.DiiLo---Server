/**
 * 
 */
package com.diilo.streetartphone.server.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

import com.diilo.streetartphone.server.core.dao.UserDao;
import com.diilo.streetartphone.server.core.entities.UserBean;

/**
 * @author Eric
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "classpath:applicationContext-services.xml", "classpath:applicationContext-core.xml",
		"classpath:applicationContext-core-datasource.xml" })
public class UserDaoTest {

	private static final String	NICKNAME_TESTUNIT	= "Test-Unit77777";

	private static final String	EMAIL_TESTUNIT		= "test777777@test-unit.com";

	@SpringBeanByType
	private UserDao				userDao;

	/**
	 * Test method for {@link com.diilo.streetartphone.server.dao.UserDao#getByNickname(java.lang.String)}.
	 */
	@Test
	public void testGetByNickname() {
		UserBean user = userDao.getByNickname(NICKNAME_TESTUNIT);
		assertEquals(NICKNAME_TESTUNIT, user.getNickname());
		assertEquals(EMAIL_TESTUNIT, user.getEmail());
	}

	/**
	 * Test method for {@link com.diilo.streetartphone.server.dao.UserDao#getByEmail(java.lang.String)}.
	 */
	@Test
	public void testGetByEmail() {
		UserBean user = userDao.getByEmail(EMAIL_TESTUNIT);
		assertEquals(NICKNAME_TESTUNIT, user.getNickname());
		assertEquals(EMAIL_TESTUNIT, user.getEmail());

	}

}
