package com.airhacks.headlands.processors.boundary;

import java.util.List;
import javax.cache.processor.MutableEntry;

/**
 *
 * @author airhacks.com
 */
public interface StringEntryProcessor {

    String process(MutableEntry<String, String> entry, List<String> arguments);
}
