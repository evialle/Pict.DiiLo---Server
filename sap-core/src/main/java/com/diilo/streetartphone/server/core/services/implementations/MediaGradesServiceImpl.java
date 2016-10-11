/**
 * 
 */
package com.diilo.streetartphone.server.core.services.implementations;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diilo.streetartphone.server.core.dao.MediaDao;
import com.diilo.streetartphone.server.core.dao.MediaGradeDao;
import com.diilo.streetartphone.server.core.entities.MediaGradeBean;
import com.diilo.streetartphone.server.core.exceptions.PictureAlreadyGradedException;
import com.diilo.streetartphone.server.core.services.MediaGradesService;

/**
 * @author Eric
 */
@Service("mediaGradesService")
public class MediaGradesServiceImpl implements MediaGradesService {

	@Autowired
	private MediaGradeDao	pictureGradeDao;

	@Autowired
	private MediaDao		pictureDao;

	@Override
	public void addPositiveGrade(final String idPicture, final String email) throws PictureAlreadyGradedException {

		// 1- add the picture as graded for the given email and add it as graded
		checkGrade(idPicture, email);

		// 2- update the counter for the Picture
		pictureDao.addPositiveGrade(idPicture);
	}

	@Override
	public void addNegativeGrade(final String idPicture, final String email) throws PictureAlreadyGradedException {
		// 1- add the picture as graded for the given email and add it as graded
		checkGrade(idPicture, email);

		// 2- update the counter for the Picture
		pictureDao.addNegativeGrade(idPicture);
	}

	private void checkGrade(final String idPicture, final String email) throws PictureAlreadyGradedException {
		if (pictureGradeDao.isAlreadyGraded(idPicture, email)) {
			throw new PictureAlreadyGradedException();
		} else {
			MediaGradeBean gradeBean = new MediaGradeBean();
			gradeBean.setEmail(email);
			gradeBean.setDate(new Date());
			gradeBean.setIdMedia(idPicture);

			pictureGradeDao.save(gradeBean);
		}
	}

}
