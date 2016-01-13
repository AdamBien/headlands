package com.airhacks.headlands.cache.boundary;

import com.airhacks.headlands.cache.control.ConfigurationProvider;
import com.airhacks.headlands.cache.control.Initializer;
import com.airhacks.headlands.cache.entity.CacheConfiguration;
import java.util.Set;
import javax.json.JsonObject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

/**
 *
 * @author airhacks.com
 */
public class EntriesResource {

    private Initializer discoverer;

    @PathParam("cacheName")
    @NotNull
    private String cacheName;
    private final ConfigurationProvider configuration;

    public EntriesResource(Initializer discoverer, ConfigurationProvider configuration) {
        this.discoverer = discoverer;
        this.configuration = configuration;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response all(@QueryParam("maxEntries") @DefaultValue("100") long maxEntries) {
        StreamingOutput so = (o) -> {
            discoverer.dumpInto(cacheName, maxEntries, o);
        };
        CacheControl cacheControl = getCacheControl();
        return Response.ok(so).cacheControl(cacheControl).build();
    }

    @GET
    @Path("{key}")
    public Response getValue(@PathParam("key") String key) {
        CacheControl cacheControl = getCacheControl();
        final String result = this.discoverer.get(cacheName, key);
        if (result == null) {
            return Response.noContent().
                    cacheControl(cacheControl).
                    build();
        }
        return Response.ok(result).
                cacheControl(cacheControl).
                build();
    }

    CacheControl getCacheControl() {
        CacheConfiguration c = configuration.getConfiguration(cacheName);
        int expiryForAccess = (int) c.getExpiryForAccessInSeconds();
        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(expiryForAccess);
        cacheControl.setPrivate(false);
        return cacheControl;
    }

    @DELETE
    @Path("{key}")
    public Response remove(@PathParam("key") String key) {
        this.discoverer.remove(cacheName, key);
        return Response.ok().build();
    }

    @DELETE
    public Response removeAll() {
        this.discoverer.removeAll(cacheName);
        return Response.ok().build();
    }

    @PUT
    @Path("{key}")
    public Response save(@PathParam("key") @NotNull String key, String value) {
        this.discoverer.put(this.cacheName, key, value);
        return Response.ok().build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(JsonObject content) {
        Set<String> keySet = content.keySet();
        keySet.parallelStream().
                forEach((k) -> this.discoverer.put(cacheName, k, content.getString(k)));
        return Response.ok().
                header("x-message", content.size() + " entries saved!").build();
    }

}
