package com.airhacks.headlands.notifications.boundary;

import com.airhacks.headlands.cache.boundary.CacheDiscoverer;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
public class NotificationsEdge {

    @Inject
    CacheDiscoverer discoverer;

    public void registerListener(String cacheName) {

    }

}
