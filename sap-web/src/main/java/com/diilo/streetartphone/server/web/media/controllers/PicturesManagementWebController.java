package com.diilo.streetartphone.server.web.media.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.diilo.streetartphone.server.core.entities.MediaBean;
import com.diilo.streetartphone.server.core.entities.MediaBean.MediaTypeEnum;
import com.diilo.streetartphone.server.core.entities.PlaceBean;
import com.diilo.streetartphone.server.core.exceptions.UnknownPictureException;
import com.diilo.streetartphone.server.core.exceptions.UnknownPlaceException;
import com.diilo.streetartphone.server.core.services.MediasService;
import com.diilo.streetartphone.server.core.services.PlaceService;

@Controller
public class PicturesManagementWebController {

	private static final Logger	LOG	= LoggerFactory.getLogger(PicturesManagementWebController.class);

	@Autowired
	private MediasService		mediaService;

	@Autowired
	private PlaceService		placeService;

	@Value("#{configServicesProperties['service.picture.regular.suffix']}")
	private String				SUFFIX_PICTURE_URL;

	@Value("#{configServicesProperties['service.picture.url.regular.prefix']}")
	private String				URL_REGULAR_PREFIX;

	@RequestMapping(value = "/picturesmgr/image/{idPicture}", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getPicturePage(@PathVariable final String idPicture, final HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		try {
			String userAgent = request.getHeader("User-Agent");
			if ((userAgent != null) && (userAgent.contains("iPhone"))) {
				mav.addObject("mobility", true);
			} else {
				mav.addObject("mobility", false);
			}

			MediaBean picture = mediaService.getPictureInfo(idPicture);
			mav.addObject("picture", picture);
			mav.addObject("suffixPictureUrl", SUFFIX_PICTURE_URL);
			mav.addObject("serverRegularPrefix", URL_REGULAR_PREFIX);
			if (picture.getMediaType() == MediaTypeEnum.VIDEO_MP4_TYPE) {
				mav.setViewName("picturesmgr/video");
			} else {
				mav.setViewName("picturesmgr/picture");
			}

		} catch (UnknownPictureException e) {
			mav.setViewName("picturesmgr/unknownPicture");
			LOG.warn("Unknown picture: " + idPicture);
		}
		return mav;
	}

	/**
	 * @param idPicture
	 * @param request
	 * @return a redirect to the picture full size
	 */
	@RequestMapping(value = "/picturesmgr/imagefile/{idPicture}", method = { RequestMethod.GET, RequestMethod.POST })
	public View getPictureFile(@PathVariable final String idPicture, final HttpServletRequest request) {

		try {
			MediaBean picture = mediaService.getPictureInfo(idPicture);
			return new RedirectView(picture.getUrlRegularImage());
		} catch (UnknownPictureException e) {
			LOG.warn("Unknown picture: " + idPicture);
			return new RedirectView("");
		}

	}

	/**
	 * @param idPicture
	 * @param request
	 * @return a redirect to the thumb
	 */
	@RequestMapping(value = "/picturesmgr/imagethumbfile/{idPicture}", method = { RequestMethod.GET, RequestMethod.POST })
	public View getPictureThumbFile(@PathVariable final String idPicture, final HttpServletRequest request) {

		try {
			MediaBean picture = mediaService.getPictureInfo(idPicture);
			StringBuilder url = new StringBuilder(picture.getUrlRegularImage());
			url.insert(picture.getUrlRegularImage().length() - 4, ".th");
			return new RedirectView(url.toString());
		} catch (UnknownPictureException e) {
			LOG.warn("Unknown picture: " + idPicture);
			return new RedirectView("");
		}

	}

	@RequestMapping(value = "/picturesmgr/place/{idPlace}", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getPlace(@PathVariable final String idPlace) {
		ModelAndView mav = new ModelAndView();
		try {
			PlaceBean place = placeService.findPlace(idPlace);
			List<MediaBean> picturesList = mediaService.listPicturesInPlace(place);

			mav.addObject("place", place);
			mav.addObject("picturesList", picturesList);

			mav.setViewName("picturesmgr/place");

		} catch (UnknownPlaceException e) {
			mav.setViewName("picturesmgr/unknownPlace");
			LOG.warn("Unknown picture: " + idPlace);
		}
		return mav;
	}

}
