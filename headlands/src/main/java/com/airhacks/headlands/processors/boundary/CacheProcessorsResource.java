package com.airhacks.headlands.processors.boundary;

import com.airhacks.porcupine.execution.boundary.Dedicated;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
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
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

/**
 *
 * @author airhacks.com
 */
@Stateless
@Path("cache-processors")
public class CacheProcessorsResource {

    @Inject
    CacheProcessorExecutor executor;

    static final String ERROR_HEADER_KEY = "error";
    static final String SCRIPT_KEY = "script";
    static final String KEYS_KEY = "keys";
    static final String ARGUMENTS_KEY = "arguments";
    static final String DETAILS_KEY = "details";

    @Inject
    @Dedicated
    ExecutorService executorService;

    @POST
    @Path("{cache}")
    public void execute(@PathParam("cache") String name, String script, @Suspended AsyncResponse response) {
        Map<String, String> result = null;

        CompletableFuture.supplyAsync(() -> execute(name, script), executorService).
                thenAccept(response::resume).handle((t, u) -> {
            response.resume(convert(u));
            return null;
        });
    }

    Response execute(String name, String script) {
        Map<String, String> result = executor.execute(name, script);
        if (result.isEmpty()) {
            return Response.noContent().
                    header(DETAILS_KEY, "EntryProcessor returned null").
                    build();
        }
        JsonObject converted = convert(result);
        return Response.ok(converted).build();
    }

    Response convert(Throwable t) {
        return Response.status(Response.Status.BAD_REQUEST).
                header(ERROR_HEADER_KEY, t.getCause()).
                header(DETAILS_KEY, t.getMessage()).
                build();
    }

    JsonObject convert(Map<String, String> input) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        input.entrySet().stream().forEach(e -> builder.add(e.getKey(), e.getValue()));
        return builder.build();
    }

    Set<String> convertJsonArrayToSet(JsonArray input) {
        List<JsonString> values = input.getValuesAs(JsonString.class);
        return values.stream().map(v -> v.getString()).collect(Collectors.toSet());
    }

}
