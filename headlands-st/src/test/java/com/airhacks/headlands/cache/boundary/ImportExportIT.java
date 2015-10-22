package com.airhacks.headlands.cache.boundary;

import static com.airhacks.rulz.jaxrsclient.HttpMatchers.successful;
import com.airhacks.rulz.jaxrsclient.JAXRSClientProvider;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class ImportExportIT {

    @Rule
    public JAXRSClientProvider caches = JAXRSClientProvider.buildWithURI("http://localhost:8080/headlands/resources/caches");

    @Test
    public void exportAndImport() throws IOException {
        String input = "input" + System.currentTimeMillis();
        String output = "output" + System.currentTimeMillis();
        final String dumpFile = "./target/" + input + ".json";
        final WebTarget target = caches.target();
        final int CACHE_SIZE = 10000;
        CachesResourceIT.createCache(target, input);
        CachesResourceIT.createCache(target, output);
        for (int i = 0; i < CACHE_SIZE; i++) {
            EntriesResourceIT.createEntry(target, input, "key" + i, "value" + i);
        }

        Response response = target.path(input).path("entries").request(MediaType.APPLICATION_JSON).get();
        assertThat(response, successful());
        JsonObject dump = response.readEntity(JsonObject.class);
        assertThat(dump.size(), is(CACHE_SIZE - 1));
        FileWriter writer = new FileWriter(dumpFile);
        try (JsonWriter jsonWriter = Json.createWriter(writer)) {
            jsonWriter.writeObject(dump);
        }
        FileReader reader = new FileReader(dumpFile);

        JsonObject readDump;
        try (JsonReader jsonReader = Json.createReader(reader)) {
            readDump = jsonReader.readObject();
        }
        response = target.path(output).path("entries").request().put(Entity.json(readDump));
        assertThat(response, successful());

    }
}
