package com.airhacks.headlands.engine.control;

import com.airhacks.headlands.processors.control.CacheProcessor;
import javax.cache.processor.EntryProcessor;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class NashornEngineTest {

    private NashornEngine cut;

    @Before
    public void init() {
        this.cut = new NashornEngine();
        this.cut.initializeEngine();
    }

    @Test
    public void createProcessorFromEmptyScript() {
        CacheProcessor invalid = this.cut.evalScript("", CacheProcessor.class);
        assertNull(invalid);
    }

    @Test(expected = IllegalStateException.class)
    public void createProcessorFromInvalidScript() {
        EntryProcessor<String, String, String> invalid = this.cut.evalScript("INVALID", EntryProcessor.class);
        assertNull(invalid);
    }

    @Test
    public void createValidProcessorFromScript() {
        String expected = "works";
        String processorScript = "function process(entry,args){ return \"" + expected + "\"}";
        EntryProcessor<String, String, String> processor = this.cut.evalScript(processorScript, EntryProcessor.class);
        assertNotNull(processor);
        String result = processor.process(null);
        assertThat(result, is(expected));
    }

    @Test
    public void createValidProcessorWithArgument() {
        String expected = "works";
        String processorScript = "function process(entry,arg){ return arg[0]}";
        EntryProcessor<String, String, String> processor = this.cut.evalScript(processorScript, EntryProcessor.class);
        assertNotNull(processor);
        String result = processor.process(null, expected);
        assertThat(result, is(expected));
    }

}
