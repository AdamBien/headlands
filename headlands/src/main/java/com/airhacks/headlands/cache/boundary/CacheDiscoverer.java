package com.airhacks.headlands.cache.boundary;

import com.airhacks.headlands.cache.Entity.CacheConfiguration;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author airhacks.com
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class CacheDiscoverer {

    private CachingProvider cachingProvider;
    private CacheManager cacheManager;

    @PostConstruct
    public void boot() {
        System.setProperty("hazelcast.jcache.provider.type", "server");
        this.cachingProvider = Caching.getCachingProvider();
        this.cacheManager = cachingProvider.getCacheManager();
    }

    public List<String> cacheNames() {
        List<String> caches = new ArrayList<>();
        Iterable<String> names = this.cacheManager.getCacheNames();
        names.forEach(caches::add);
        return caches;
    }

    @PreDestroy
    public void shutdown() {
        this.cacheManager.close();
        this.cachingProvider.close();
    }

    /**
     *
     * @param cacheName -- the name of the cache.
     * @param configuration - configuration used for the creation of
     * MutableConfiguration
     * @return true - cache created, false - cache updated
     */
    public boolean createCache(String cacheName, CacheConfiguration configuration) {
        MutableConfiguration<String, String> mutableConfiguration = new MutableConfiguration<>();
        if (cacheNames().contains(cacheName)) {
            return false;
        }
        mutableConfiguration.setStoreByValue(configuration.isStoreByValue()).
                setTypes(String.class, String.class).
                setManagementEnabled(configuration.isManagementEnabled()).
                setStatisticsEnabled(configuration.isStatisticsEnabled());

        this.cacheManager.createCache(cacheName, mutableConfiguration);
        return true;
    }

}
