package com.airhacks.headlands.cache.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
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

    @Context
    ResourceContext rc;

    @GET
    public JsonArray all(@Context UriInfo info) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        List<String> cacheNames = discoverer.cacheNames();
        cacheNames.stream().map(n -> info.getAbsolutePathBuilder().
                path(n).
                build().
                toASCIIString()).
                forEach(arrayBuilder::add);
        return arrayBuilder.build();
    }

    @Path("{cacheName}")
    public CacheResource newCache() {
        return rc.initResource(new CacheResource(discoverer));
    }

}
