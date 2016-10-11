/**
 * 
 */
package com.diilo.streetartphone.server.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

import com.diilo.streetartphone.server.controllers.MediasManagerRemoteControllerTest;
import com.diilo.streetartphone.server.core.dao.MediaGradeDao;

/**
 * @author Eric
 */

@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "classpath:applicationContext-services.xml", "classpath:applicationContext-core.xml",
		"classpath:applicationContext-core-datasource.xml" })
public class MediaGradeDaoTest {

	@SpringBeanByType
	private MediaGradeDao		mediaGradeDao;

	private static final String	PASSWORD_TESTUNIT	= "123456";

	private static final String	EMAIL_TESTUNIT		= "test2@test-unit.com";

	private static final String	IDPICTURE_TESTUNIT	= "1";

	private static final Logger	LOG					= LoggerFactory.getLogger(MediasManagerRemoteControllerTest.class);

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testIsAlreadyGraded() {
		mediaGradeDao.isAlreadyGraded(IDPICTURE_TESTUNIT, EMAIL_TESTUNIT);
	}
}
