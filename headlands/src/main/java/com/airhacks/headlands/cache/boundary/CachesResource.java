package com.airhacks.headlands.cache.boundary;

import com.airhacks.headlands.cache.control.ConfigurationExposer;
import com.airhacks.headlands.cache.control.Initializer;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author airhacks.com
 */
@Path("caches")
@Stateless
public class CachesResource {

    @Inject
    Initializer initializer;

    @Inject
    ConfigurationExposer exposer;

    @Context
    ResourceContext rc;

    @GET
    public JsonArray all(@Context UriInfo info) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        List<String> cacheNames = initializer.cacheNames();
        cacheNames.stream().map(n -> createCacheLinks(info, n)).
                forEach(arrayBuilder::add);
        return arrayBuilder.build();
    }

    JsonObject createCacheLinks(UriInfo info, String name) {
        JsonObjectBuilder response = Json.createObjectBuilder();
        UriBuilder cacheLink = info.getAbsolutePathBuilder().
                path(name);
        UriBuilder entries = cacheLink.path("entries");
        response.add("name", name);
        response.add("link", cacheLink.build().toASCIIString());
        response.add("entries", entries.build().toASCIIString());
        return response.build();
    }

    @Path("{cacheName}")
    public CacheResource newCache() {
        return rc.initResource(new CacheResource(this.initializer, this.exposer));
    }

}
