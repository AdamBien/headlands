package com.airhacks.headlands.cache.boundary;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class EntriesResourceIT {

    private Client client;
    private WebTarget tut;
    private String cacheName;

    @Before
    public void init() {
        this.client = ClientBuilder.newClient();
        this.tut = this.client.target("http://localhost:8080/headlands/resources/caches");
        this.cacheName = "cache-" + System.currentTimeMillis();
        JsonObject configuration = Json.createObjectBuilder().build();
        this.tut.path(cacheName).request().put(Entity.json(configuration));

    }

    @Test
    public void crudEntries() {
        String expectedValue = "java rocks " + System.currentTimeMillis();
        String expectedKey = "status" + System.currentTimeMillis();
        this.tut.path(cacheName).path("entries").path(expectedKey).request().put(Entity.text(expectedValue));

        JsonObject cacheContent = this.tut.path(cacheName).path("entries").request().get(JsonObject.class);
        System.out.println("cacheContent = " + cacheContent);
        assertNotNull(cacheContent);

        String actualValue = this.tut.path(cacheName).path("entries").path(expectedKey).request().get(String.class);
        assertThat(actualValue, is(expectedValue));

        Response deletion = this.tut.path(cacheName).path("entries").path(expectedKey).request().delete();
        assertThat(deletion.getStatus(), is(200));
    }

    @Test
    public void removeAll() {
        String expectedValue = "java rocks " + System.currentTimeMillis();
        String expectedKey = "should be deleted" + System.currentTimeMillis();
        this.tut.path(cacheName).path("entries").path(expectedKey).request().put(Entity.text(expectedValue));

        String actualValue = this.tut.path(cacheName).path("entries").path(expectedKey).request().get(String.class);
        assertThat(actualValue, is(expectedValue));

        Response deletion = this.tut.path(cacheName).path("entries").request().delete();
        assertThat(deletion.getStatus(), is(200));

        Response response = this.tut.path(cacheName).path("entries").path(expectedKey).request().get();
        assertThat(response.getStatus(), is(204));

    }

    @After
    public void deleteCache() {
        this.tut.path(cacheName).request().delete();
        this.client.close();

    }

}
