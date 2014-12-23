package com.airhacks.headlands.cache.boundary;

import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

/**
 *
 * @author airhacks.com
 */
public class EntriesResource {

    private CacheDiscoverer discoverer;

    @PathParam("cacheName")
    @NotNull
    private String cacheName;

    public EntriesResource(CacheDiscoverer discoverer) {
        this.discoverer = discoverer;
    }

    @GET
    public Response all(@QueryParam("maxEntries") @DefaultValue("100") long maxEntries) {
        StreamingOutput so = (o) -> {
            discoverer.dumpInto(cacheName, maxEntries, o);
        };
        return Response.ok(so).build();
    }

    @GET
    @Path("{key}")
    public String getValue(@PathParam("key") String key) {
        return this.discoverer.getValue(cacheName, key);
    }

}
