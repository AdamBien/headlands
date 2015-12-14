package com.airhacks.headlands.cache.boundary;

import com.airhacks.headlands.cache.control.ConfigurationExposer;
import com.airhacks.headlands.cache.control.Initializer;
import com.airhacks.headlands.cache.entity.CacheConfiguration;
import javax.validation.constraints.NotNull;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author airhacks.com
 */
@Produces(MediaType.APPLICATION_JSON)
public class CacheResource {

    Initializer initializer;
    ConfigurationExposer discoverer;

    @Context
    ResourceContext rc;

    public CacheResource(Initializer initializer, ConfigurationExposer cd) {
        this.discoverer = cd;
        this.initializer = initializer;
    }

    @PUT
    public Response newCache(@PathParam("cacheName") @NotNull String cacheName, CacheConfiguration cacheConfiguration, @Context UriInfo info) {
        boolean created = initializer.createCache(cacheName, cacheConfiguration);
        if (!created) {
            return Response.ok().header("x-info", "Cache: " + cacheName + " already created").build();
        }
        return Response.created(info.getAbsolutePath()).build();
    }

    @OPTIONS
    public Response info(@PathParam("cacheName") String cacheName) {
        CacheConfiguration configuration = this.discoverer.getConfiguration(cacheName);
        if (configuration == null) {
            return Response.noContent().build();
        }
        return Response.ok(configuration).build();
    }

    @Path("entries")
    public EntriesResource entries(@PathParam("cacheName") @NotNull String cacheName) {
        return rc.initResource(new EntriesResource(initializer));
    }

}
