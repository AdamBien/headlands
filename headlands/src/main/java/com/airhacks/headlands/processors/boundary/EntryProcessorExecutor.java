package com.airhacks.headlands.processors.boundary;

import com.airhacks.headlands.cache.boundary.CacheDiscoverer;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.cache.Cache;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorResult;
import javax.inject.Inject;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author airhacks.com
 */
public class EntryProcessorExecutor {

    @Inject
    CacheDiscoverer discoverer;

    private Invocable invocable;
    private ScriptEngine scriptEngine;

    @PostConstruct
    public void initializeEngine() {
        ScriptEngineManager manager = new ScriptEngineManager();
        this.scriptEngine = manager.getEngineByName("javascript");
        this.invocable = (Invocable) scriptEngine;
    }

    public Map<String, EntryProcessorResult<String>> execute(String cacheName, String script, Set<String> keys, Set<String> arguments) {
        Cache<String, String> cache = discoverer.getCache(cacheName);
        if (cache == null) {
            throw new IllegalArgumentException("Cache " + cacheName + " does not exist!");
        }
        Object[] args = arguments.toArray();
        EntryProcessor<String, String, String> processor = createProcessorFromScript(script);
        return cache.invokeAll(keys, processor, args);
    }

    EntryProcessor<String, String, String> createProcessorFromScript(String script) {
        try {
            scriptEngine.eval(script);
        } catch (ScriptException ex) {
            Logger.getLogger(EntryProcessorExecutor.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("Cannot interpret: " + script
                    + " Problem in line: " + ex.getLineNumber()
                    + " Column: " + ex.getColumnNumber(), ex);
        }
        return invocable.getInterface(EntryProcessor.class);
    }

    public boolean cacheExists(String name) {
        List<String> cacheNames = this.discoverer.cacheNames();
        return cacheNames.contains(name);
    }

}
