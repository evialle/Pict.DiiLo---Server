/**
 * 
 */
package com.diilo.streetartphone.server.core.services;

import java.util.List;

import com.diilo.streetartphone.server.core.entities.ReportBean;

/**
 * @author Eric
 */
public interface MediaReportsService {

	/** The user may report a picture if the picture looks bad :( */
	public void reportPicture(final String idPicture, final String email, final String comments);

	/** For administrator. */
	public List<ReportBean> listReportedPictures();

}
