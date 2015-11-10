package com.airhacks.headlands.sync;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author airhacks.com
 */
public class CacheService {

    private Client client;
    private String resource = "/headlands/resources/caches";

    @PostConstruct
    public void initialize() {
        this.client = ClientBuilder.newClient();
    }

    public void sync(Consumer<String> progressListener, String fromHost, String toHost) {
        Set<String> cacheNames = getCacheNames(fromHost);
        progressListener.accept(cacheNames.size() + " caches found!");
        cacheNames.forEach(name -> copy(progressListener, name, fromHost, toHost));

    }

    Set<String> getCacheNames(String host) {
        WebTarget target = getTarget(host);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        JsonArray cacheNames = response.readEntity(JsonArray.class);
        List<JsonObject> jsonStringList = cacheNames.getValuesAs(JsonObject.class);
        return jsonStringList.stream().map(j -> j.getString("name")).collect(Collectors.toSet());
    }

    WebTarget getTarget(String host) {
        return this.client.target(host + resource);
    }

    void copy(Consumer<String> progressListener, String cacheName, String fromHost, String toHost) {
        final WebTarget toTarget = getTarget(toHost);
        createCache(toTarget, cacheName);
        JsonObject dump = toTarget.path(cacheName).path("entries").
                request(MediaType.APPLICATION_JSON).
                get(JsonObject.class);
        progressListener.accept("Dump with " + dump.size() + " downloaded!");
        dump.keySet().forEach(key -> createEntry(toTarget, cacheName, key, dump.getString(key)));
        progressListener.accept("Content synced");

    }

    public static Response createEntry(WebTarget caches, String cache, String key, String value) {
        return caches.path(cache).path("entries").path(key).request().put(Entity.text(value));
    }

    public static Response createCache(WebTarget target, String cacheName) {
        JsonObject configuration = Json.createObjectBuilder().build();
        return target.path(cacheName).request().put(Entity.json(configuration));
    }

}
