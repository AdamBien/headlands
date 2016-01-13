package com.airhacks.headlands.cache.boundary;

import com.airhacks.rulz.jaxrsclient.JAXRSClientProvider;
import java.util.List;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class CachesResourceIT {

    @Rule
    public JAXRSClientProvider tut = JAXRSClientProvider.buildWithURI("http://localhost:8080/headlands/resources/caches");

    static final String EXPIRY_FOR_ACCESS = "expiryForAccess";

    @Test
    public void crudCaches() {
        final WebTarget target = this.tut.target();

        String cacheName = "cache-" + System.currentTimeMillis();
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), is(200));
        response = createCache(target, cacheName);
        assertThat(response.getStatus(), is(201));

        response = target.request(MediaType.APPLICATION_JSON).get();
        JsonArray cacheNames = response.readEntity(JsonArray.class);
        List<JsonObject> jsonStringList = cacheNames.getValuesAs(JsonObject.class);
        Optional<String> result = jsonStringList.stream().map(j -> j.getString("name")).
                filter(name -> name.equalsIgnoreCase(cacheName)).
                findAny();

        assertTrue(result.isPresent());

        response = target.path(cacheName).request(MediaType.APPLICATION_JSON).options();
        assertThat(response.getStatus(), is(200));

        JsonObject info = response.readEntity(JsonObject.class);
        System.out.println("info = " + info);
        assertNotNull(info);
    }

    @Test
    public void validateExpiryForAccess() {
        final WebTarget target = this.tut.target();
        String cacheName = "cache-" + System.currentTimeMillis();
        long expectedExpiration = 4242;

        Response response = createCacheWithExpiration(target, expectedExpiration, cacheName);
        assertThat(response.getStatus(), is(201));

        response = target.path(cacheName).request(MediaType.APPLICATION_JSON).options();
        assertThat(response.getStatus(), is(200));

        JsonObject configuration = response.readEntity(JsonObject.class);
        assertNotNull(configuration);
        long actualExpiration = configuration.getJsonNumber(EXPIRY_FOR_ACCESS).longValue();
        assertThat(actualExpiration, is(expectedExpiration));

    }

    public static Response createCache(WebTarget target, String cacheName) {
        JsonObject configuration = Json.createObjectBuilder().build();
        return target.path(cacheName).request().put(Entity.json(configuration));

    }

    public static Response createCacheWithExpiration(WebTarget target, long expiryForAccess, String cacheName) {
        JsonObject configuration = Json.createObjectBuilder().add(EXPIRY_FOR_ACCESS, expiryForAccess).build();
        return target.path(cacheName).request().put(Entity.json(configuration));
    }

}
