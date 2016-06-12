package com.airhacks.headlands.notifications.control;

import com.airhacks.headlands.notifications.entity.CacheChangedEvent;
import java.io.Serializable;
import java.util.function.Consumer;
import javax.cache.event.CacheEntryCreatedListener;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryRemovedListener;
import javax.cache.event.CacheEntryUpdatedListener;
import javax.cache.event.EventType;

/**
 *
 * @author airhacks.com
 */
public class CacheEntryChangedListener implements CacheEntryCreatedListener<String, String>,
        CacheEntryUpdatedListener<String, String>,
        CacheEntryRemovedListener<String, String>,
        Serializable {

    private final transient Consumer<CacheChangedEvent> sink;
    private final String cacheName;

    public CacheEntryChangedListener(String cacheName, Consumer<CacheChangedEvent> sink) {
        this.cacheName = cacheName;
        this.sink = sink;
    }

    @Override
    public void onCreated(Iterable<CacheEntryEvent<? extends String, ? extends String>> events) throws CacheEntryListenerException {
        this.sink.accept(convert(EventType.CREATED, events));
    }

    @Override
    public void onUpdated(Iterable<CacheEntryEvent<? extends String, ? extends String>> events) throws CacheEntryListenerException {
        this.sink.accept(convert(EventType.UPDATED, events));
    }

    @Override
    public void onRemoved(Iterable<CacheEntryEvent<? extends String, ? extends String>> events) throws CacheEntryListenerException {
        this.sink.accept(convert(EventType.REMOVED, events));
    }

    CacheChangedEvent convert(EventType type, Iterable<CacheEntryEvent<? extends String, ? extends String>> events) {
        CacheChangedEvent event = new CacheChangedEvent(cacheName, type);
        events.forEach(event::consume);
        return event;
    }

}
