package com.airhacks.headlands.notifications.boundary;

import com.airhacks.headlands.cache.control.Initializer;
import com.airhacks.headlands.notifications.control.CacheEntryChangedListener;
import com.airhacks.headlands.notifications.entity.CacheChangedEvent;
import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;
import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
public class NotificationsEdge {

    @Inject
    Initializer discoverer;

    @Inject
    Event<CacheChangedEvent> event;

    public void registerListener(String cacheName) {
        Cache<Object, Object> cache = this.discoverer.cacheManager().getCache(cacheName);
        if (cache == null) {
            return;
        }
        CacheEntryChangedListener listener = new CacheEntryChangedListener(cacheName, event::fire);
        Factory<CacheEntryChangedListener> listenerFactory = FactoryBuilder.factoryOf(listener);
        MutableCacheEntryListenerConfiguration configuration = new MutableCacheEntryListenerConfiguration(listenerFactory, null, false, true);
        cache.registerCacheEntryListener(configuration);
    }

}
