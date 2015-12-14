package com.airhacks.headlands.processors.boundary;

import com.airhacks.headlands.cache.control.Initializer;
import com.airhacks.headlands.engine.control.NashornEngine;
import com.airhacks.headlands.processors.control.CacheProcessor;
import java.util.HashMap;
import java.util.Map;
import javax.cache.Cache;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
public class CacheProcessorExecutor {

    @Inject
    Initializer discoverer;

    @Inject
    NashornEngine engine;

    public Map<String, String> execute(String cacheName, String script) {
        Cache<String, String> cache = discoverer.getCache(cacheName);
        if (cache == null) {
            throw new IllegalArgumentException("Cache " + cacheName + " does not exist!");
        }
        CacheProcessor processor = engine.evalScript(script, CacheProcessor.class);
        return processor.process(cache, new HashMap<>());
    }

}
