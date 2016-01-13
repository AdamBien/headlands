package com.airhacks.headlands.cache.control;

import com.airhacks.headlands.cache.entity.CacheConfiguration;
import java.util.concurrent.TimeUnit;
import javax.cache.Cache;
import javax.cache.configuration.CompleteConfiguration;
import javax.cache.configuration.Factory;
import javax.cache.expiry.Duration;
import javax.cache.expiry.ExpiryPolicy;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
public class ConfigurationProvider {

    @Inject
    Initializer initializer;

    public CacheConfiguration getConfiguration(String cacheName) {
        CompleteConfiguration configuration = getCompleteConfiguration(cacheName);
        if (configuration == null) {
            return null;
        }
        boolean storeByValue = configuration.isStoreByValue();
        boolean managementEnabled = configuration.isManagementEnabled();
        boolean readThrough = configuration.isReadThrough();
        boolean writeThrough = configuration.isWriteThrough();
        boolean statisticsEnabled = configuration.isStatisticsEnabled();
        ExpiryPolicy expiryPolicy = getCacheEntryExpiration(configuration);
        long expiryForAccess = convertToMs(expiryPolicy.getExpiryForAccess());
        long expiryForCreation = convertToMs(expiryPolicy.getExpiryForCreation());
        long expiryForUpdate = convertToMs(expiryPolicy.getExpiryForUpdate());
        return new CacheConfiguration(expiryForAccess, expiryForCreation, expiryForUpdate, storeByValue, managementEnabled, statisticsEnabled, readThrough, writeThrough);
    }

    CompleteConfiguration getCompleteConfiguration(String cacheName) {
        Cache<String, String> cache = this.initializer.getCache(cacheName);
        if (cache == null) {
            return null;
        }
        CompleteConfiguration configuration = cache.getConfiguration(CompleteConfiguration.class);
        if (configuration == null) {
            return null;
        }
        return configuration;
    }

    ExpiryPolicy getCacheEntryExpiration(CompleteConfiguration configuration) {
        Factory<ExpiryPolicy> expiryPolicyFactory = configuration.getExpiryPolicyFactory();
        return expiryPolicyFactory.create();
    }

    long convertToMs(Duration duration) {
        if (duration == null) {
            return 0;
        }
        TimeUnit timeUnit = duration.getTimeUnit();
        if (timeUnit == null) {
            return 0;
        }
        return timeUnit.convert(duration.getDurationAmount(), TimeUnit.MILLISECONDS);
    }

}
