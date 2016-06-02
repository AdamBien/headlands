package com.airhacks.headlands.notifications.boundary;

import java.io.IOException;
import java.io.Writer;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonWriter;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class JsonArrayEncoder implements Encoder.TextStream<JsonArray> {

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void encode(JsonArray payload, Writer writer) throws EncodeException, IOException {
        try (JsonWriter jsonWriter = Json.createWriter(writer)) {
            jsonWriter.writeArray(payload);
        }
    }

    @Override
    public void destroy() {
    }
}
