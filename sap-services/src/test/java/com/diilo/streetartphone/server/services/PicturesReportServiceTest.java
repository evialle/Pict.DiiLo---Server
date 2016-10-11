/**
 * 
 */
package com.diilo.streetartphone.server.services;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

import com.diilo.streetartphone.server.core.entities.ReportBean;
import com.diilo.streetartphone.server.core.services.MediaReportsService;

/**
 * @author Eric
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "classpath:applicationContext-services.xml", "classpath:applicationContext-core.xml",
		"classpath:applicationContext-core-datasource.xml" })
public class PicturesReportServiceTest {

	@SpringBeanByType
	private MediaReportsService	picturesReportService;

	private static final String	PASSWORD_TESTUNIT	= "123456";

	private static final String	EMAIL_TESTUNIT		= "test4@test-unit.com";

	private static final String	IDPICTURE_TESTUNIT	= "0";

	/**
	 * Test method for
	 * {@link com.diilo.streetartphone.server.services.implementations.MediaReportsServiceImpl#reportPicture(long, java.lang.String)}
	 * .
	 */
	@Test
	public void testReportPicture() {
		picturesReportService.reportPicture(IDPICTURE_TESTUNIT, EMAIL_TESTUNIT, "My comments");
	}

	/**
	 * Test method for
	 * {@link com.diilo.streetartphone.server.services.implementations.MediaReportsServiceImpl#listReportedPictures()}
	 * .
	 */
	@Test
	public void testListReportedPictures() {
		List<ReportBean> list = picturesReportService.listReportedPictures();
		assertNotNull(list);
	}

}
