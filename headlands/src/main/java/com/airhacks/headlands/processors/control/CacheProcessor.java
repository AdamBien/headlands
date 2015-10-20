package com.airhacks.headlands.processors.control;

import java.util.Map;
import javax.cache.Cache;

/**
 *
 * @author airhacks.com
 */
public interface CacheProcessor {

    Map<String, String> process(Cache<String, String> cache);

}
