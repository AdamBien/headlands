package com.airhacks.headlands.notifications.control;

import java.io.Serializable;
import javax.cache.event.CacheEntryCreatedListener;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryRemovedListener;
import javax.cache.event.CacheEntryUpdatedListener;

/**
 *
 * @author airhacks.com
 */
public class CacheEntryChangedListener implements CacheEntryCreatedListener<String, String>,
        CacheEntryUpdatedListener<String, String>,
        CacheEntryRemovedListener<String, String>,
        Serializable {

    private String cacheName;

    public CacheEntryChangedListener(String cacheName) {
        this.cacheName = cacheName;
    }

    @Override
    public void onCreated(Iterable<CacheEntryEvent<? extends String, ? extends String>> events) throws CacheEntryListenerException {

    }

    @Override
    public void onUpdated(Iterable<CacheEntryEvent<? extends String, ? extends String>> events) throws CacheEntryListenerException {
    }

    @Override
    public void onRemoved(Iterable<CacheEntryEvent<? extends String, ? extends String>> events) throws CacheEntryListenerException {
    }

}
