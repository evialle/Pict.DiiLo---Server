/**
 * 
 */
package com.diilo.streetartphone.server.core.services;

import java.util.List;

import com.diilo.streetartphone.server.core.entities.sn.AbstractSocialNetworkStatusBean;

/**
 * @author Eric
 */
public interface SocialNetworksService {

	public List<AbstractSocialNetworkStatusBean> getStatus(double latitude, double longitude, int radiusMeters);

}
