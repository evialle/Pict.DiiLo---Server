/**
 * 
 */
package com.diilo.streetartphone.server.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

import com.diilo.streetartphone.server.core.dao.MediaDao;
import com.diilo.streetartphone.server.core.entities.MediaBean;
import com.diilo.streetartphone.server.core.entities.MediaBean.MediaTypeEnum;

/**
 * @author Eric
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "classpath:applicationContext-services.xml", "classpath:applicationContext-core.xml",
		"classpath:applicationContext-core-datasource.xml" })
public class MediaDaoTest {

	@SpringBeanByType
	MediaDao	mediaDao;

	@Test
	public void testAddPicture() {

		String id = UUID.randomUUID().toString().substring(0, 7);

		MediaBean mediaBean = new MediaBean();
		mediaBean.setFileId(id);
		mediaBean.setDescription("sdqsdsqdqsdqdq");
		mediaBean.setDate(new Date());
		mediaBean.setLatitude(2.0);
		mediaBean.setLongitude(3.0);
		mediaBean.setNickname("eric");
		mediaBean.setMediaType(MediaTypeEnum.PICTURE_JPG_TYPE);
		mediaBean.setUrlRegularImage("http://test.com");
		mediaBean.setVertical(true);
		mediaDao.save(mediaBean);

		assertNotNull(mediaDao.get(id));
		mediaDao.delete(mediaBean);
	}

	/**
	 * Test method for {@link com.diilo.streetartphone.server.dao.MediaDao#findByFileId(java.lang.String)}.
	 */
	@Test
	public void testFindByIdPicture() {
		MediaBean picture = mediaDao.findByFileId("1");
		assertNotNull(picture);
		assertEquals("1", picture.getFileId());
	}

	@Test
	public void testListInPerimeter() {
		List<MediaBean> pictureList = mediaDao.listInPerimeter(1.5, 1.5, 2.5, 2.5);
		assertEquals(2, pictureList.size());
	}
}
