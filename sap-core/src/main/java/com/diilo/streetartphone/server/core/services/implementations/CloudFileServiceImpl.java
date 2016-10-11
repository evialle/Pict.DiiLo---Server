package com.diilo.streetartphone.server.core.services.implementations;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.diilo.streetartphone.server.core.dao.MediaDao;
import com.diilo.streetartphone.server.core.entities.MediaBean;
import com.diilo.streetartphone.server.core.exceptions.PictureHostUploadException;
import com.diilo.streetartphone.server.core.services.CloudFileService;

@Service
public class CloudFileServiceImpl implements CloudFileService {

	private static final Logger	LOG						= LoggerFactory.getLogger(MediaServiceImpl.class);
	private static final String	IMAGESHACK_UPLOAD_URL	= "http://www.imageshack.us/upload_api.php";

	private static final int	NB_OF_RETRY				= 10;

	@Value("#{configCoreProperties['service.picture.imageshack.key']}")
	private String				IMAGESHACK_KEY;

	@Value("#{configCoreProperties['service.picture.thumb.suffix']}")
	public String				THUMB_SUFFIX;
	@Value("#{configCoreProperties['service.picture.regular.suffix']}")
	public String				REGULAR_PICTURE_SUFFIX;

	@Value("#{configCoreProperties['service.picture.thumb.prefix']}")
	public String				THUMB_PREFIX;
	@Value("#{configCoreProperties['service.picture.regular.prefix']}")
	public String				REGULAR_PICTURE_PREFIX;

	@Value("#{configCoreProperties['service.video.regular.prefix']}")
	public String				VIDEO_PREFIX;

	@Value("#{configCoreProperties['service.video.regular.suffix']}")
	private String				VIDEO_SUFFIX;

	/** Delay of 5 Minutes before deleting an uploaded file. */
	private long				DELAY_DELATION_MS		= 300007;

	@Autowired
	private MediaDao			mediaDao;

	@Override
	public MediaBean uploadMediaFile(final MediaBean media) throws PictureHostUploadException {

		// 1- Send on server
		// 1-1 Send picture on server
		String urlPicture = sendMediaToHostSynchronous(media.getUrlRegularImage());
		LOG.info("The picture " + media.getFileId() + " is hosted on " + urlPicture);

		// 1-2 Send potential movie on server
		String urlVideo = null;
		if (media.getMediaType() == MediaBean.MediaTypeEnum.VIDEO_MP4_TYPE) {
			urlVideo = sendMediaToHostSynchronous(media.getUrlRegularVideo());
			LOG.info("The video " + media.getFileId() + " is hosted on " + urlVideo);

		}

		// 2- update the media with the new urls
		if (urlPicture != null) {
			media.setUrlRegularImage(urlPicture);
		}
		if (urlVideo != null) {
			media.setUrlRegularVideo(urlVideo);
		}
		mediaDao.update(media);

		// 3- Delete the file, in 5 minutes
		deletePictureLater(media, DELAY_DELATION_MS);

		return media;
	}

	/**
	 * Delete the picture after the supplied delay
	 * 
	 * @param idPicture
	 * @param delayInMs
	 */
	private void deletePictureLater(final MediaBean media, final long delayInMs) {

		Timer timer = new Timer("Deleting Media: " + media.getFileId(), false);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {

				File picturefile = new File(REGULAR_PICTURE_PREFIX + media.getFileId() + REGULAR_PICTURE_SUFFIX);
				delete(picturefile);

				File videoFile = new File(VIDEO_PREFIX + media.getFileId() + VIDEO_SUFFIX);
				delete(videoFile);
			}
		}, delayInMs);
	}

	private boolean delete(File file) {
		boolean success = file.delete();

		if (success == false) {
			LOG.error("The file: " + file.getAbsolutePath() + " can't be deleted");
		} else {
			LOG.info("The file: " + file.getAbsolutePath() + " has been deleted");
		}
		return success;
	}

	/**
	 * Upload a picture to ImageShack.us
	 * 
	 * @see http://code.google.com/p/imageshackapi/wiki/ImageshackAPI
	 * @param idPicture
	 *            id of the picture to upload
	 * @return the url of the file
	 */
	private String sendMediaToHostSynchronous(final String url) throws PictureHostUploadException {
		// 1- connexion au client
		int retry = 0;
		do {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(IMAGESHACK_UPLOAD_URL);

			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

			try {
				reqEntity.addPart("url", new StringBody(url));
				reqEntity.addPart("key", new StringBody(IMAGESHACK_KEY));
				reqEntity.addPart("rembar", new StringBody("yes"));
				reqEntity.addPart("public", new StringBody("no"));

				httppost.setEntity(reqEntity);

				LOG.debug("executing request " + httppost.getRequestLine());
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity resEntity = response.getEntity();

				String urlImageShack = null;
				if (resEntity != null) {
					// XML returned by Imageshack
					String page = EntityUtils.toString(resEntity);
					LOG.debug("Imageshack return: " + page);

					// find the url in the xml
					Pattern p = Pattern.compile("<image_link>(.+)</image_link>");
					Matcher m = p.matcher(page);

					while (m.find()) {
						urlImageShack = m.group(1).trim();
					}
					if (urlImageShack != null) {
						return urlImageShack;
					}
				}

			} catch (UnsupportedEncodingException e) {
				LOG.error("UnsupportedEncodingException for file: " + url);

				throw new PictureHostUploadException();
			} catch (ParseException e) {
				LOG.error("ParseException for file: " + url);

				throw new PictureHostUploadException();
			} catch (IOException e) {
				LOG.error("IOException for file: " + url);

				throw new PictureHostUploadException();
			}

			retry++;
		} while (retry <= NB_OF_RETRY);

		throw new PictureHostUploadException();
	}

}
