/**
 * 
 */
package com.diilo.streetartphone.server.core.services;

import com.diilo.streetartphone.server.core.entities.UserBean;

/**
 * @author Eric
 */
public interface UserPositionService {

	void retrieveUserPositionsToAnalyse();

	void savePositionForNotifications(final UserBean user, final double latitude, final double longitude);

}
