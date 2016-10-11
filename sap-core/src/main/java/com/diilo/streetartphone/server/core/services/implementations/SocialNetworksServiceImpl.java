/**
 * 
 */
package com.diilo.streetartphone.server.core.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.diilo.streetartphone.server.core.entities.sn.AbstractSocialNetworkStatusBean;
import com.diilo.streetartphone.server.core.entities.sn.GowallaStatusBean;
import com.diilo.streetartphone.server.core.services.SocialNetworksService;

/**
 * @author Eric
 */
@Service("socialNetworksService")
public class SocialNetworksServiceImpl implements SocialNetworksService {

	@Autowired
	private RestTemplate	restTemplate;

	/*
	 * (non-Javadoc)
	 * @see com.diilo.streetartphone.server.core.services.SocialNetworksService#getStatus(double, double, int)
	 */
	@Override
	public List<AbstractSocialNetworkStatusBean> getStatus(final double latitude, final double longitude,
			final int radiusMeters) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<GowallaStatusBean> getGowallaStatus(final double latitude, final double longitude,
			final int radiusMeters) {
		List result = restTemplate.getForObject(
				"http://api.gowalla.com/spots?lat={latitude}&lng={longitude}.7494&radius={radiusMeters}", List.class,
				latitude, longitude);

		return null;
	}
}
