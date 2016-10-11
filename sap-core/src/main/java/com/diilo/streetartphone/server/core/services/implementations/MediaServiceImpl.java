package com.diilo.streetartphone.server.core.services.implementations;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import twitter4j.TwitterException;

import com.diilo.streetartphone.server.core.dao.MediaDao;
import com.diilo.streetartphone.server.core.entities.MediaBean;
import com.diilo.streetartphone.server.core.entities.MediaBean.MediaTypeEnum;
import com.diilo.streetartphone.server.core.entities.PlaceBean;
import com.diilo.streetartphone.server.core.entities.UserBean;
import com.diilo.streetartphone.server.core.entities.UserPositionBean;
import com.diilo.streetartphone.server.core.exceptions.FacebookException;
import com.diilo.streetartphone.server.core.exceptions.PictureHostUploadException;
import com.diilo.streetartphone.server.core.exceptions.TechnicalException;
import com.diilo.streetartphone.server.core.exceptions.UnknownPictureException;
import com.diilo.streetartphone.server.core.services.CloudFileService;
import com.diilo.streetartphone.server.core.services.FacebookService;
import com.diilo.streetartphone.server.core.services.MediasService;
import com.diilo.streetartphone.server.core.utils.ImageUtilsService;
import com.diilo.streetartphone.server.core.utils.TwitterService;
import com.diilo.streetartphone.server.core.utils.VideoUtilsService;

@Service("mediaService")
public class MediaServiceImpl implements MediasService {

	private static final Logger	LOG				= LoggerFactory.getLogger(MediaServiceImpl.class);

	/** Max size of a tweets in nb of characters. */
	private static final int	TWEET_MAX_SIZE	= 110;

	@Value("#{configCoreProperties['service.video.regular.prefix']}")
	public String				VIDEO_PREFIX;
	@Value("#{configCoreProperties['service.video.regular.suffix']}")
	public String				VIDEO_SUFFIX;

	@Value("#{configCoreProperties['service.picture.thumb.suffix']}")
	public String				THUMB_SUFFIX;
	@Value("#{configCoreProperties['service.picture.regular.suffix']}")
	public String				REGULAR_SUFFIX;

	@Value("#{configCoreProperties['service.picture.thumb.prefix']}")
	public String				THUMB_PREFIX;
	@Value("#{configCoreProperties['service.picture.regular.prefix']}")
	public String				REGULAR_PREFIX;

	@Value("#{configCoreProperties['service.picture.lowqual.prefix']}")
	private String				LOWQUAL_PREFIX;
	@Value("#{configCoreProperties['service.picture.lowqual.suffix']}")
	private String				LOWQUAL_SUFFIX;

	@Value("#{configCoreProperties['service.picture.position.tolerancy']}")
	private double				POSITION_TOLERANCY;

	@Value("#{configCoreProperties['service.picture.area.tolerancy']}")
	private double				AREA_TOLERANCY;

	@Value("#{configCoreProperties['service.picture.fileid.size']}")
	private int					FILEID_SIZE;

	@Value("#{configCoreProperties['service.picture.url.shorten.prefix']}")
	private String				PICTURE_SHORTEN_URL_PREFIXE;

	@Value("#{configCoreProperties['service.picture.regular.suffix']}")
	private String				SUFFIX_PICTURE_URL;

	@Value("#{configCoreProperties['service.media.url.prefix']}")
	private String				URL_MEDIA_PREFIX;

	@Autowired
	private ImageUtilsService	imageUtilsService;

	@Autowired
	private CloudFileService	cloudFileService;

	@Autowired
	private TwitterService		twitterService;

	@Autowired
	private FacebookService		facebookService;

	@Autowired
	private VideoUtilsService	videoUtilsService;

	@Autowired
	private MediaDao			mediaDao;

	@Autowired
	private TaskExecutor		taskExecutor;

	@Override
	public void addVideo(final MultipartFile video, final UserBean user, final String description, double longitude,
			double latitude, boolean sentToTwitter, boolean sentToFacebook) throws TechnicalException {

		// 1- create a media id
		String idVideo = createMediaId();

		try {
			// 2- Media Transformation
			File videoFile = new File(VIDEO_PREFIX + idVideo + VIDEO_SUFFIX);
			File regularFile = new File(REGULAR_PREFIX + idVideo + REGULAR_SUFFIX);
			File thumbFile = new File(THUMB_PREFIX + idVideo + THUMB_SUFFIX);
			File lowqualFile = new File(LOWQUAL_PREFIX + idVideo + LOWQUAL_SUFFIX);

			// 2-1 save tmp the video
			video.transferTo(videoFile);

			// 2-2 create regularfile as a thumbnail
			videoUtilsService.createThumbnail(videoFile, regularFile);
			// 2-3 create thumbnail and lowqual from regular file
			BufferedImage pictureBuffImg = ImageIO.read(regularFile);
			imageUtilsService.produceThumb(pictureBuffImg, thumbFile, true);
			imageUtilsService.produceLowQuality(pictureBuffImg, lowqualFile);

			// 3- save in database
			MediaBean videoBean = new MediaBean();
			videoBean.setFileId(idVideo);
			videoBean.setDescription(description);
			videoBean.setMediaType(MediaTypeEnum.VIDEO_MP4_TYPE);
			videoBean.setDate(new Date());
			videoBean.setLatitude(latitude);
			videoBean.setLongitude(longitude);
			videoBean.setUrlRegularImage(URL_MEDIA_PREFIX + "pictures/" + idVideo + REGULAR_SUFFIX);
			videoBean.setUrlRegularVideo(URL_MEDIA_PREFIX + "videos/" + idVideo + VIDEO_SUFFIX);
			videoBean.setNickname(user.getNickname());

			// 4- Save
			String pk = mediaDao.save(videoBean);
			LOG.info("new video (PK: " + pk + " ): " + videoBean);

			// 5- Run The uploads on a dedicated thread
			cloudDispatch(user, videoBean, sentToTwitter, sentToFacebook);

		} catch (IllegalStateException e) {
			LOG.error(e.getMessage());
			throw new TechnicalException(e);

		} catch (IOException e) {
			LOG.error(e.getMessage());
			throw new TechnicalException(e);

		}

	}

	@Override
	public void addPicture(final MultipartFile picture, final UserBean user, final String description,
			double longitude, double latitude, boolean sentToTwitter, boolean sentToFacebook) throws TechnicalException {
		LOG.debug("An image has been sent: " + picture.getContentType());

		// 1- create a picture id
		String idPicture = createMediaId();

		// 2- move pictures & create thumb
		try {
			File regularFile = new File(REGULAR_PREFIX + idPicture + REGULAR_SUFFIX);
			File thumbFile = new File(THUMB_PREFIX + idPicture + THUMB_SUFFIX);
			File lowqualFile = new File(LOWQUAL_PREFIX + idPicture + LOWQUAL_SUFFIX);

			// Computation of the image
			picture.transferTo(regularFile);
			BufferedImage pictureBuffImg = ImageIO.read(regularFile);
			imageUtilsService.produceThumb(pictureBuffImg, thumbFile);
			imageUtilsService.produceLowQuality(pictureBuffImg, lowqualFile);

			// 3- save in database
			MediaBean pictureBean = new MediaBean();
			pictureBean.setFileId(idPicture);
			pictureBean.setMediaType(MediaTypeEnum.PICTURE_JPG_TYPE);
			pictureBean.setDescription(description);
			pictureBean.setDate(new Date());
			pictureBean.setLatitude(latitude);
			pictureBean.setLongitude(longitude);
			pictureBean.setUrlRegularImage(URL_MEDIA_PREFIX + "pictures/" + idPicture + SUFFIX_PICTURE_URL);
			pictureBean.setNickname(user.getNickname());

			pictureBean.setVertical(imageUtilsService.isVertical(pictureBuffImg));

			String pk = mediaDao.save(pictureBean);
			LOG.info("new picture (PK: " + pk + " ): " + picture);

			// 4- Run The uploads on a dedicated thread
			cloudDispatch(user, pictureBean, sentToTwitter, sentToFacebook);

		} catch (IllegalStateException e) {
			LOG.error(e.getMessage());
			throw new TechnicalException(e);
		} catch (IOException e) {
			LOG.error(e.getMessage());
			throw new TechnicalException(e);
		}
	}

	private void cloudDispatch(final UserBean user, final MediaBean media, final boolean sentToTwitter,
			final boolean sentToFacebook) {

		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 1- Send it to imageshack and delete it later
				try {
					cloudFileService.uploadMediaFile(media);

					// 2- should we send it to twitter?
					if (sentToTwitter) {
						sendByTwitter(user, media);
					}

					// 3- should we send it to Facebook?
					if (sentToFacebook) {
						sendByFacebook(user, media);
					}
				} catch (PictureHostUploadException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FacebookException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

	private boolean sendByFacebook(UserBean user, MediaBean picture) throws FacebookException {
		if (user.getFacebookAccessToken() == null) {
			return false;
		} else {
			// Connect to Facebook and send the message

			facebookService.addStream(user, FacebookService.PrivacyEnum.ALL_FRIENDS, PICTURE_SHORTEN_URL_PREFIXE
					+ picture.getFileId(), picture.getUrlRegularVideo(), null, picture.getUrlRegularImage(),
					"Instant captured by Pict.DiiLo", null, picture.getDescription());
			return true;
		}
	}

	private boolean sendByTwitter(final UserBean user, final MediaBean picture) {

		if (user.getTwitterAuthToken() == null) {
			return false;
		} else {

			// 1- Build the status message
			String tweetDescrp;
			if (picture.getDescription().length() > TWEET_MAX_SIZE) {
				tweetDescrp = picture.getDescription().substring(0, TWEET_MAX_SIZE - 3) + "...";
			} else {
				tweetDescrp = picture.getDescription();
			}
			StringBuilder statusTxt = new StringBuilder(tweetDescrp).append(" ").append(PICTURE_SHORTEN_URL_PREFIXE)
					.append(picture.getFileId());

			// 2- Connect to twitter and send the message
			try {
				twitterService.sendMessage(user, statusTxt.toString(), picture.getLatitude(), picture.getLongitude());
			} catch (TwitterException e) {
				LOG.error("Twitter error while sending Message for: " + user.getEmail() + " - " + statusTxt.toString()
						+ " (" + e.getMessage() + ")");
				return false;
			}
			return true;
		}
	}

	private String createMediaId() {
		String id;
		do {
			// Generate the random key
			id = RandomStringUtils.randomAlphanumeric(FILEID_SIZE);
		} while (isFileIdExists(id));

		return id;
	}

	private boolean isFileIdExists(String id) {
		return (mediaDao.get(id) != null);
	}

	@Override
	public List<MediaBean> listPictures(String nicknameToList) {

		// 1- find pictures
		List<MediaBean> list = mediaDao.list(nicknameToList);
		return list;
	}

	@Override
	public List<MediaBean> listPicturesInPerimeter(double latitude, double longitude) {
		// find pictures
		List<MediaBean> list = mediaDao.listInPerimeter(latitude - POSITION_TOLERANCY, longitude - POSITION_TOLERANCY,
				latitude + POSITION_TOLERANCY, longitude + POSITION_TOLERANCY);
		Comparator<MediaBean> comparator = new PositionComparator(latitude, longitude);
		Collections.sort(list, comparator);
		return list;
	}

	/**
	 * Order by Media closer to the given position
	 * 
	 * @author eric
	 */
	private class PositionComparator implements Comparator<MediaBean> {

		private double	latitude, longitude;

		public PositionComparator(final double latitudeParam, final double longitudeParam) {
			this.latitude = latitudeParam;
			this.longitude = longitudeParam;
		}

		@Override
		public int compare(MediaBean o1, MediaBean o2) {
			// points for o1
			double d1 = computeDistance(o1);
			// points for o2
			double d2 = computeDistance(o2);
			if (d2 > d1) {
				return -1;
			} else if (d2 == d1) {
				return 0;
			} else {
				return 1;
			}
		}

		private double computeDistance(MediaBean media) {
			return Math.abs(media.getLatitude() - latitude) + Math.abs(media.getLatitude() - longitude);
		}

	}

	@Override
	public List<MediaBean> listPicturesInLargeArea(double latitude, double longitude) {
		List<MediaBean> list = mediaDao.listInPerimeter(latitude - AREA_TOLERANCY, longitude - AREA_TOLERANCY, latitude
				+ AREA_TOLERANCY, longitude + AREA_TOLERANCY);
		return list;
	}

	@Override
	public MediaBean getPictureInfo(String idPicture) throws UnknownPictureException {
		MediaBean picture = mediaDao.get(idPicture);
		if (picture == null) {
			throw new UnknownPictureException();
		} else {
			return picture;
		}

	}

	@Override
	public List<MediaBean> listPicturesInPlace(PlaceBean place) {

		return mediaDao.listInPerimeter(place.getLatitudeMin(), place.getLongitudeMin(), place.getLatitudeMax(),
				place.getLongitudeMax());

	}

	@Override
	public void deletePicture(final MediaBean picture) {
		// 1- delete from the database
		mediaDao.delete(picture);

		// 2- delete from file system
		// 2-1- delete regular if it exists
		File regularFile = new File(REGULAR_PREFIX + picture.getFileId() + REGULAR_SUFFIX);
		if (regularFile.exists()) {
			regularFile.delete();
		}

		// 2-2- delete thumb picture
		File thumbFile = new File(THUMB_PREFIX + picture.getFileId() + THUMB_SUFFIX);
		if (thumbFile.exists()) {
			thumbFile.delete();
		}

		// 2-3- delete lowqual picture
		File lowqualFile = new File(LOWQUAL_PREFIX + picture.getFileId() + LOWQUAL_SUFFIX);
		if (lowqualFile.exists()) {
			lowqualFile.delete();
		}

	}

	@Override
	public MediaBean lastMedia(final UserPositionBean userPositionBean, final Date minDate, final Date maxDate) {
		// find pictures
		MediaBean media = mediaDao.lastMedia(userPositionBean, AREA_TOLERANCY, minDate, maxDate);
		return media;
	}
}
