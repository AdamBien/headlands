package com.airhacks.headlands.processors.boundary;

import com.airhacks.headlands.cache.boundary.CachesResourceIT;
import com.airhacks.headlands.cache.boundary.EntriesResourceIT;
import static com.airhacks.rulz.jaxrsclient.HttpMatchers.noContent;
import static com.airhacks.rulz.jaxrsclient.HttpMatchers.successful;
import com.airhacks.rulz.jaxrsclient.JAXRSClientProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class EntryProcessorsResourceIT {

    @Rule
    public JAXRSClientProvider processors = JAXRSClientProvider.buildWithURI("http://localhost:8080/headlands/resources/processors");

    @Rule
    public JAXRSClientProvider caches = JAXRSClientProvider.buildWithURI("http://localhost:8080/headlands/resources/caches");

    @Test
    public void executeProcessors() throws IOException {
        final WebTarget target = caches.target();
        String cacheName = "gurus";
        String key = "42";
        String value = "nuke";

        Response response = CachesResourceIT.createCache(target, cacheName);
        assertThat(response, successful());
        response = EntriesResourceIT.createEntry(target, cacheName, key, value);
        assertThat(response, successful());

        String script = loadScript("processor.js");
        JsonObject processor = createProcessor(script, key);

        response = processors.target().
                path(cacheName).
                request(MediaType.APPLICATION_JSON).
                post(Entity.json(processor));

        assertThat(response, successful());
        JsonObject processorResult = response.readEntity(JsonObject.class);
        assertNotNull(processorResult);
        System.out.println("processorResult = " + processorResult);
    }

    @Test
    public void executeProcessorsWithNonExistingKey() throws IOException {
        final WebTarget target = caches.target();
        String cacheName = "gurus";
        String key = "42" + System.currentTimeMillis();

        Response response = CachesResourceIT.createCache(target, cacheName);
        assertThat(response, successful());

        String script = loadScript("passthrough.js");
        JsonObject processor = createProcessor(script, key);

        response = processors.target().
                path(cacheName).
                request(MediaType.APPLICATION_JSON).
                post(Entity.json(processor));

        assertThat(response, noContent());
    }

    String loadScript(String fileName) throws IOException {
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/" + fileName));
        return new String(content, "UTF-8");
    }

    private JsonObject createProcessor(String script, String key) {
        JsonArray keys = Json.createArrayBuilder().
                add(key).
                build();
        return Json.createObjectBuilder().
                add("script", script).
                add("keys", keys).
                build();
    }
}
