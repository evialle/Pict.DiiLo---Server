/**
 * 
 */
package com.diilo.streetartphone.server.core.services;

import com.diilo.streetartphone.server.core.entities.MediaBean;
import com.diilo.streetartphone.server.core.exceptions.PictureHostUploadException;

/**
 * @author Eric
 */
public interface CloudFileService {

	/**
	 * Will upload the picture asynchronously and delete it from our local filesystem.
	 * 
	 * @param idPicture
	 * @return
	 * @throws PictureHostUploadException
	 */
	MediaBean uploadMediaFile(MediaBean media) throws PictureHostUploadException;
}
