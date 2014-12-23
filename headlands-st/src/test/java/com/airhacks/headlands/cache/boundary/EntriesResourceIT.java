package com.airhacks.headlands.cache.boundary;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class EntriesResourceIT {

    private Client client;
    private WebTarget tut;

    @Before
    public void init() {
        this.client = ClientBuilder.newClient();
        this.tut = this.client.target("http://localhost:8080/headlands/resources/caches");

    }

    @Test
    public void crudEntries() {
        String cacheName = "cache-" + System.currentTimeMillis();
        JsonObject configuration = Json.createObjectBuilder().build();
        this.tut.path(cacheName).request().put(Entity.json(configuration));

        JsonObject cacheContent = this.tut.path(cacheName).path("entries").request().get(JsonObject.class);
        assertNotNull(cacheContent);
    }

}
