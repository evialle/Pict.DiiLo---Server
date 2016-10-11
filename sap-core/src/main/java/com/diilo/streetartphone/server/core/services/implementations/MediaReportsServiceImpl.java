/**
 * 
 */
package com.diilo.streetartphone.server.core.services.implementations;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diilo.streetartphone.server.core.dao.MediaReportDao;
import com.diilo.streetartphone.server.core.entities.ReportBean;
import com.diilo.streetartphone.server.core.services.MediaReportsService;

/**
 * @author Eric
 */
@Service("mediaReportsService")
public class MediaReportsServiceImpl implements MediaReportsService {

	private static final Logger	LOG	= LoggerFactory.getLogger(MediaServiceImpl.class);

	@Autowired
	private MediaReportDao		pictureReportDao;

	@Override
	public void reportPicture(final String idMedia, final String email, final String comments) {

		LOG.info("Reporting " + idMedia + " for " + email);

		ReportBean report = new ReportBean();
		report.setIdMedia(idMedia);
		report.setEmail(email);
		report.setDate(new Date());
		report.setComments(comments);

		pictureReportDao.save(report);

	}

	@Override
	public List<ReportBean> listReportedPictures() {
		return pictureReportDao.findAll();
	}

}
