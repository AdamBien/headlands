package com.airhacks.headlands.processors.boundary;

import com.airhacks.headlands.cache.control.Initializer;
import com.airhacks.headlands.engine.control.NashornEngine;
import java.util.HashSet;
import java.util.Map;
import javax.cache.Cache;
import javax.cache.processor.EntryProcessorResult;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author airhacks.com
 */
public class EntryProcessorExecutorTest {

    EntryProcessorExecutor cut;
    String existingCache = "dukes";
    private Cache cache;

    @Before
    public void init() {
        NashornEngine engine = new NashornEngine();
        engine.initializeEngine();
        this.cut = new EntryProcessorExecutor();
        this.cut.engine = engine;
        this.cut.discoverer = mock(Initializer.class);
        this.cache = mock(Cache.class);
        when(this.cut.discoverer.getCache(existingCache)).thenReturn(cache);
    }

    @Test
    public void createValidProcessorFromScript() {
        String expected = "works";
        String processorScript = "function process(entry,args){ return \"" + expected + "\"}";
        Map<String, EntryProcessorResult<String>> result = this.cut.execute(existingCache, processorScript, new HashSet<>(), new HashSet<>());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
