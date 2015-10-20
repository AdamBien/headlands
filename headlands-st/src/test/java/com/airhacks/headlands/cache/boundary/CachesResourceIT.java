package com.airhacks.headlands.cache.boundary;

import com.airhacks.rulz.jaxrsclient.JAXRSClientProvider;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class CachesResourceIT {

    @Rule
    public JAXRSClientProvider tut = JAXRSClientProvider.buildWithURI("http://localhost:8080/headlands/resources/caches");

    @Test
    public void crudCaches() {
        final WebTarget target = this.tut.target();

        String cacheName = "cache-" + System.currentTimeMillis();
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), is(200));
        response = createCache(target, cacheName);
        assertThat(response.getStatus(), is(201));

        response = target.path(cacheName).request(MediaType.APPLICATION_JSON).options();
        assertThat(response.getStatus(), is(200));

        JsonObject info = response.readEntity(JsonObject.class);
        System.out.println("info = " + info);
        assertNotNull(info);

        JsonObject configuration = Json.createObjectBuilder().build();
        response = target.path(cacheName).request().put(Entity.json(configuration));
        assertThat(response.getStatus(), is(200));
    }

    public static Response createCache(WebTarget target, String cacheName) {
        JsonObject configuration = Json.createObjectBuilder().build();
        return target.path(cacheName).request().put(Entity.json(configuration));

    }

}
