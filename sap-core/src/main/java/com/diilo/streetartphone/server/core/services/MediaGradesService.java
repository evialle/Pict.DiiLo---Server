package com.diilo.streetartphone.server.core.services;

import com.diilo.streetartphone.server.core.exceptions.PictureAlreadyGradedException;

public interface MediaGradesService {

	/**
	 * Give a positive grade to a picture (but only one grade per picture, per user)
	 * 
	 * @throws PictureAlreadyGradedException
	 */
	public void addPositiveGrade(final String idPicture, final String email) throws PictureAlreadyGradedException;

	/**
	 * Give a negative grade to a picture (but only one grade per picture, per user)
	 * 
	 * @throws PictureAlreadyGradedException
	 */
	public void addNegativeGrade(final String idPicture, final String email) throws PictureAlreadyGradedException;

}
