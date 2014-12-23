package com.airhacks.headlands.cache.boundary;

import com.airhacks.headlands.cache.Entity.CacheConfiguration;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author airhacks.com
 */
@Path("caches")
@Stateless
public class CachesResource {

    @Inject
    CacheDiscoverer discoverer;

    @GET
    public JsonArray all() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        Iterable<String> cacheNames = discoverer.cacheNames();
        cacheNames.forEach(arrayBuilder::add);
        return arrayBuilder.build();
    }

    @PUT
    @Path("{cacheName}")
    public Response newCache(@PathParam("cacheName") @NotNull String cacheName, CacheConfiguration cacheConfiguration, @Context UriInfo info) {

        boolean created = discoverer.createCache(cacheName, cacheConfiguration);
        if (!created) {
            return Response.ok().header("x-info", "Cache: " + cacheName + " already created").build();
        }
        return Response.created(info.getAbsolutePath()).build();
    }

}
