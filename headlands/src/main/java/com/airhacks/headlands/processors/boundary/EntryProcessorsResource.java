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
@Path("entry-processors")
public class EntryProcessorsResource {

    @Inject
    EntryProcessorExecutor executor;

    static final String ERROR_HEADER_KEY = "error";
    static final String SCRIPT_KEY = "script";
    static final String KEYS_KEY = "keys";
    static final String ARGUMENTS_KEY = "arguments";
    static final String DETAILS_KEY = "details";

    @POST
    @Path("{cache}")
    public Response execute(@PathParam("cache") String name, JsonObject input) {
        String script = null;
        Set<String> keys = null;
        Set<String> arguments = null;

        if (!executor.cacheExists(name)) {
            return Response.status(Response.Status.BAD_REQUEST).
                    header(ERROR_HEADER_KEY, "Cache: " + name + " does not exist").
                    build();
        }

        if (!input.containsKey(SCRIPT_KEY) || input.isNull(SCRIPT_KEY)) {
            return Response.status(Response.Status.BAD_REQUEST).
                    header(ERROR_HEADER_KEY, "script is required").
                    build();
        } else {
            script = input.getString(SCRIPT_KEY);

        }
        if (!input.containsKey(KEYS_KEY)) {
            keys = Collections.EMPTY_SET;
        } else {
            JsonArray keysArray = input.getJsonArray(KEYS_KEY);
            keys = convertJsonArrayToSet(keysArray);
        }

        if (!input.containsKey(ARGUMENTS_KEY)) {
            arguments = Collections.EMPTY_SET;
        } else {
            JsonArray argsArray = input.getJsonArray(ARGUMENTS_KEY);
            arguments = convertJsonArrayToSet(argsArray);
        }

        Map<String, EntryProcessorResult<String>> result = null;
        try {
            result = executor.execute(name, script, keys, arguments);
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.BAD_REQUEST).
                    header(ERROR_HEADER_KEY, e.getCause().getMessage()).
                    header(DETAILS_KEY, e.getMessage()).
                    build();
        }
        if (result.isEmpty()) {
            return Response.noContent().
                    header(DETAILS_KEY, "EntryProcessor returned null").
                    build();
        }
        JsonObjectBuilder builder = Json.createObjectBuilder();
        try {
            result.entrySet().stream().forEach(e -> builder.add(e.getKey(), e.getValue().get()));
        } catch (EntryProcessorException e) {
            return Response.serverError().header(ERROR_HEADER_KEY, "EntryProcessorException").
                    header(DETAILS_KEY, e.getMessage()).
                    build();
        }
        final JsonObject payload = builder.build();
        return Response.ok(payload).build();
    }

    Set<String> convertJsonArrayToSet(JsonArray input) {
        List<JsonString> values = input.getValuesAs(JsonString.class);
        return values.stream().map(v -> v.getString()).collect(Collectors.toSet());
    }

}
