package com.airhacks.headlands.cache.control;

import com.airhacks.headlands.cache.entity.CacheConfiguration;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.CompleteConfiguration;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
public class ConfigurationExposer {

    @Inject
    CacheManager cacheManager;

    public CacheConfiguration getConfiguration(String cacheName) {
        CompleteConfiguration configuration = getCompleteConfiguration(cacheName);
        boolean storeByValue = configuration.isStoreByValue();
        boolean managementEnabled = configuration.isManagementEnabled();
        boolean readThrough = configuration.isReadThrough();
        boolean writeThrough = configuration.isWriteThrough();
        boolean statisticsEnabled = configuration.isStatisticsEnabled();
        return new CacheConfiguration(storeByValue, managementEnabled, statisticsEnabled, readThrough, writeThrough);
    }

    public CompleteConfiguration getCompleteConfiguration(String cacheName) {
        Cache<String, String> cache = this.cacheManager.getCache(cacheName, String.class, String.class);
        if (cache == null) {
            return null;
        }
        CompleteConfiguration configuration = cache.getConfiguration(CompleteConfiguration.class);
        if (configuration == null) {
            return null;
        }
        return configuration;
    }

}
