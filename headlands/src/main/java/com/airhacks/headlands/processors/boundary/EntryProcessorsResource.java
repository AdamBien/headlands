package com.airhacks.headlands.processors.boundary;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.EntryProcessorResult;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author airhacks.com
 */
@Stateless
@Path("processors")
public class EntryProcessorsResource {

    @Inject
    EntryProcessorExecutor executor;

    static final String ERROR_HEADER_KEY = "error";

    @POST
    @Path("{cache}")
    public Response execute(@PathParam("cache") String name, JsonObject input) {
        String script = null;
        Set<String> keys = null;
        Set<String> arguments = null;

        if (!input.isNull("script")) {
            return Response.status(Response.Status.BAD_REQUEST).
                    header(ERROR_HEADER_KEY, "script is required").
                    build();
        } else {
            script = input.getString("script");

        }
        if (input.isNull("keys")) {
            keys = Collections.EMPTY_SET;
        } else {
            JsonArray keysArray = input.getJsonArray("keys");
            keys = convertJsonArrayToSet(keysArray);
        }

        if (input.isNull(name)) {
            arguments = Collections.EMPTY_SET;
        } else {
            JsonArray argsArray = input.getJsonArray("arguments");
            arguments = convertJsonArrayToSet(argsArray);
        }

        Map<String, EntryProcessorResult<String>> result = executor.execute(name, script, keys, arguments);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        try {
            result.entrySet().stream().forEach(e -> builder.add(e.getKey(), e.getValue().get()));
        } catch (EntryProcessorException e) {
            return Response.serverError().header(ERROR_HEADER_KEY, "EntryProcessorException").
                    header("detail", e.getMessage()).build();
        }
        final JsonObject payload = builder.build();
        return Response.ok(payload).build();
    }

    Set<String> convertJsonArrayToSet(JsonArray input) {
        List<JsonString> values = input.getValuesAs(JsonString.class);
        return values.stream().map(v -> v.getString()).collect(Collectors.toSet());

    }

}
