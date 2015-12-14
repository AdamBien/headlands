package com.airhacks.headlands.processors.boundary;

import com.airhacks.headlands.cache.control.Initializer;
import com.airhacks.headlands.engine.control.NashornEngine;
import java.util.Map;
import java.util.Set;
import javax.cache.Cache;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorResult;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
public class EntryProcessorExecutor {

    @Inject
    Initializer discoverer;

    @Inject
    NashornEngine engine;

    public Map<String, EntryProcessorResult<String>> execute(String cacheName,
            String script, Set<String> keys, Set<String> arguments) {
        Cache<String, String> cache = discoverer.getCache(cacheName);
        if (cache == null) {
            throw new IllegalArgumentException("Cache " + cacheName + " does not exist!");
        }
        Object[] args = arguments.toArray();
        EntryProcessor<String, String, String> processor = engine.evalScript(script, EntryProcessor.class);
        return cache.invokeAll(keys, processor, args);
    }

    public boolean cacheExists(String name) {
        return discoverer.cacheExists(name);
    }

}
