package com.airhacks.headlands.cache.boundary;

import com.airhacks.headlands.cache.entity.CacheConfiguration;
import javax.validation.constraints.NotNull;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author airhacks.com
 */
public class CacheResource {

    CacheDiscoverer discoverer;

    public CacheResource(CacheDiscoverer cd) {
        this.discoverer = cd;
    }

    @PUT
    public Response newCache(@PathParam("cacheName") @NotNull String cacheName, CacheConfiguration cacheConfiguration, @Context UriInfo info) {
        boolean created = discoverer.createCache(cacheName, cacheConfiguration);
        if (!created) {
            return Response.ok().header("x-info", "Cache: " + cacheName + " already created").build();
        }
        return Response.created(info.getAbsolutePath()).build();
    }
}
