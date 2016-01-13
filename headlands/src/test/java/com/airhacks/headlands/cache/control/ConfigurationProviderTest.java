package com.airhacks.headlands.cache.control;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class ConfigurationProviderTest {

    ConfigurationProvider cut;

    @Before
    public void init() {
        this.cut = new ConfigurationProvider();
    }

    @Test
    public void convertNullToMs() {
        long actual = this.cut.convertToMs(null);
        assertThat(actual, is(0l));
    }

}
