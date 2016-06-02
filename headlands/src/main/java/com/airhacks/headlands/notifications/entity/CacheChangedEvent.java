package com.airhacks.headlands.notifications.entity;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.EventType;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 *
 * @author airhacks.com
 */
public class CacheChangedEvent {

    private EventType type;
    private String cacheName;
    private JsonArrayBuilder payload;

    public CacheChangedEvent(String cacheName, EventType type) {
        this.type = type;
        this.cacheName = cacheName;
        this.payload = Json.createArrayBuilder();
    }

    public void consume(CacheEntryEvent<? extends String, ? extends String> event) {
        String key = event.getKey();
        String value = event.getValue();
        String oldValue = event.getOldValue();
        JsonObject diff = Json.createObjectBuilder().add("newValue", value).
                add("oldValue", oldValue).
                build();
        JsonObject changeSet = Json.createObjectBuilder().
                add("cacheName", cacheName).
                add("eventType", type.name()).
                add(key, diff).
                build();
        payload.add(changeSet);
    }

    public JsonArray getPayload() {
        return payload.build();

    }

}
