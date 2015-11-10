package com.airhacks.headlands.processors.boundary;

import com.airhacks.headlands.cache.boundary.CachesResourceIT;
import com.airhacks.headlands.cache.boundary.EntriesResourceIT;
import static com.airhacks.rulz.jaxrsclient.HttpMatchers.successful;
import com.airhacks.rulz.jaxrsclient.JAXRSClientProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class CacheProcessorsResourceIT {

    @Rule
    public JAXRSClientProvider processors = JAXRSClientProvider.buildWithURI("http://localhost:8080/headlands/resources/cache-processors");

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

        String script = loadScript("copycache.js");

        response = processors.target().
                path(cacheName).
                request(MediaType.APPLICATION_JSON).
                post(Entity.text(script));

        assertThat(response, successful());
        JsonObject processorResult = response.readEntity(JsonObject.class);
        assertNotNull(processorResult);
        assertThat(processorResult.keySet(), hasItem(key));
        List<String> values = processorResult.
                values().
                stream().
                map(j -> (JsonString) j).
                map(json -> json.getString()).
                collect(Collectors.toList());
        assertThat(values, hasItem(value));

        MultivaluedMap<String, Object> headers = response.getHeaders();
        headers.entrySet().forEach((t) -> {
            System.out.println("Key: " + t.getKey() + " value: " + t.getValue());
        });
    }

    String loadScript(String fileName) throws IOException {
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/" + fileName));
        return new String(content, "UTF-8");
    }
}
