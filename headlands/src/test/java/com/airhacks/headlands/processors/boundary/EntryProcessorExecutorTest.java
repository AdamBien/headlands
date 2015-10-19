package com.airhacks.headlands.processors.boundary;

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
public class EntryProcessorExecutorTest {

    EntryProcessorExecutor cut;

    @Before
    public void init() {
        this.cut = new EntryProcessorExecutor();
        this.cut.initializeEngine();
    }

    @Test
    public void createProcessorFromEmptyScript() {
        EntryProcessor<String, String, String> invalid = this.cut.createProcessorFromScript("");
        assertNull(invalid);
    }

    @Test(expected = IllegalStateException.class)
    public void createProcessorFromInvalidScript() {
        EntryProcessor<String, String, String> invalid = this.cut.createProcessorFromScript("INVALID");
        assertNull(invalid);
    }

    @Test
    public void createValidProcessorFromScript() {
        String expected = "works";
        String processorScript = "function process(entry,args){ return \"" + expected + "\"}";
        EntryProcessor<String, String, String> processor = this.cut.createProcessorFromScript(processorScript);
        assertNotNull(processor);
        String result = processor.process(null);
        assertThat(result, is(expected));
    }

}
