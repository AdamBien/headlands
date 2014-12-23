package com.airhacks.headlands.cache.boundary;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class CachesResourceIT {

    private Client client;
    private WebTarget tut;

    @Before
    public void init() {
        this.client = ClientBuilder.newClient();
        this.tut = this.client.target("http://localhost:8080/headlands/resources/caches");

    }

    @Test
    public void crud() {
        String cacheName = "cache-" + System.currentTimeMillis();
        Response response = this.tut.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), is(200));
        JsonObject configuration = Json.createObjectBuilder().build();
        response = this.tut.path(cacheName).request().put(Entity.json(configuration));
        assertThat(response.getStatus(), is(201));

        response = this.tut.path(cacheName).request(MediaType.APPLICATION_JSON).options();
        assertThat(response.getStatus(), is(200));

        JsonObject info = response.readEntity(JsonObject.class);
        System.out.println("info = " + info);
        assertNotNull(info);

        response = this.tut.path(cacheName).request().put(Entity.json(configuration));
        assertThat(response.getStatus(), is(200));

    }

}
