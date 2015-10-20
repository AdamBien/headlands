package com.airhacks.headlands.cache.boundary;

import static com.airhacks.rulz.jaxrsclient.HttpMatchers.successful;
import com.airhacks.rulz.jaxrsclient.JAXRSClientProvider;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class EntriesResourceIT {

    @Rule
    public JAXRSClientProvider tut = JAXRSClientProvider.buildWithURI("http://localhost:8080/headlands/resources/caches");
    private String cacheName;

    @Before
    public void init() {
        this.cacheName = "cache-" + System.currentTimeMillis();
        JsonObject configuration = Json.createObjectBuilder().build();
        this.tut.target().path(cacheName).request().put(Entity.json(configuration));

    }

    @Test
    public void crudEntries() {
        String expectedValue = "java rocks " + System.currentTimeMillis();
        String expectedKey = "status" + System.currentTimeMillis();
        Response response = createEntry(this.tut.target(), this.cacheName, expectedKey, expectedValue);
        assertThat(response, successful());

        JsonObject cacheContent = this.tut.target().path(cacheName).path("entries").request().get(JsonObject.class);
        System.out.println("cacheContent = " + cacheContent);
        assertNotNull(cacheContent);

        String actualValue = this.tut.target().path(cacheName).path("entries").path(expectedKey).request().get(String.class);
        assertThat(actualValue, is(expectedValue));

        Response deletion = this.tut.target().path(cacheName).path("entries").path(expectedKey).request().delete();
        assertThat(deletion.getStatus(), is(200));
    }

    public static Response createEntry(WebTarget caches, String cache, String key, String value) {
        return caches.path(cache).path("entries").path(key).request().put(Entity.text(value));
    }

    @Test
    public void bulkSave() {
        String expectedValue = "java rocks " + System.currentTimeMillis();
        String expectedKey = "status" + System.currentTimeMillis();

        JsonObject input = Json.createObjectBuilder().add(expectedKey, expectedValue).build();

        this.tut.target().path(cacheName).path("entries").request().put(Entity.json(input));

        JsonObject cacheContent = this.tut.target().path(cacheName).path("entries").request().get(JsonObject.class);
        System.out.println("cacheContent = " + cacheContent);
        assertNotNull(cacheContent);

        String actualValue = this.tut.target().path(cacheName).path("entries").path(expectedKey).request().get(String.class);
        assertThat(actualValue, is(expectedValue));

        Response deletion = this.tut.target().path(cacheName).path("entries").path(expectedKey).request().delete();
        assertThat(deletion.getStatus(), is(200));
    }

    @Test
    public void removeAll() {
        String expectedValue = "java rocks " + System.currentTimeMillis();
        String expectedKey = "should be deleted" + System.currentTimeMillis();
        this.tut.target().path(cacheName).path("entries").path(expectedKey).request().put(Entity.text(expectedValue));

        String actualValue = this.tut.target().path(cacheName).path("entries").path(expectedKey).request().get(String.class);
        assertThat(actualValue, is(expectedValue));

        Response deletion = this.tut.target().path(cacheName).path("entries").request().delete();
        assertThat(deletion.getStatus(), is(200));

        Response response = this.tut.target().path(cacheName).path("entries").path(expectedKey).request().get();
        assertThat(response.getStatus(), is(204));

    }

    @After
    public void deleteCache() {
        this.tut.target().path(cacheName).request().delete();
    }

}
