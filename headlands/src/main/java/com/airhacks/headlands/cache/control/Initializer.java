package com.airhacks.headlands.cache.control;

import com.airhacks.headlands.cache.entity.CacheConfiguration;
import com.airhacks.headlands.notifications.control.CacheEntryChangedListener;
import com.airhacks.headlands.notifications.entity.CacheChangedEvent;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.Factory;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.Produces;

/**
 *
 * @author airhacks.com
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class Initializer {

    CachingProvider cachingProvider;
    CacheManager cacheManager;

    @Inject
    Event<CacheChangedEvent> event;

    @PostConstruct
    public void boot() {
        System.setProperty("hazelcast.jcache.provider.type", "server");
        this.cachingProvider = Caching.getCachingProvider();
        this.cacheManager = cachingProvider.getCacheManager();
    }

    @Produces
    public CachingProvider cachingProvider() {
        return this.cachingProvider;
    }

    @Produces
    public CacheManager cacheManager() {
        return this.cacheManager;
    }

    public List<String> cacheNames() {
        List<String> caches = new ArrayList<>();
        Iterable<String> names = this.cacheManager.getCacheNames();
        names.forEach(caches::add);
        return caches;
    }

    public boolean cacheExists(String name) {
        return cacheNames().contains(name);
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
        if (configuration == null) {
            configuration = new CacheConfiguration();
        }
        MutableConfiguration<String, String> mutableConfiguration = new MutableConfiguration<>();
        if (cacheNames().contains(cacheName)) {
            this.cacheManager.enableManagement(cacheName, configuration.isManagementEnabled());
            this.cacheManager.enableStatistics(cacheName, configuration.isStatisticsEnabled());
            return false;
        }
        mutableConfiguration = mutableConfiguration.
                setStoreByValue(configuration.isStoreByValue()).
                setTypes(String.class, String.class).
                setManagementEnabled(configuration.isManagementEnabled()).
                setStatisticsEnabled(configuration.isStatisticsEnabled())
                .setExpiryPolicyFactory(
                        FactoryBuilder.factoryOf(
                                new AccessedExpiryPolicy(
                                        new Duration(
                                                TimeUnit.MILLISECONDS, configuration.getExpiryForAccess()
                                        )
                                )
                        )).
                setReadThrough(configuration.isReadThrough()).
                setWriteThrough(configuration.isWriteThrough());

        Cache<String, String> cache = this.cacheManager.createCache(cacheName, mutableConfiguration);
        registerListener(cache);
        return true;
    }

    public Cache<String, String> getCache(String cacheName) {
        return this.cacheManager.getCache(cacheName, String.class, String.class);
    }

    public void delete(String cacheName) {
        this.cacheManager.destroyCache(cacheName);
    }

    public long dumpInto(String cacheName, long maxEntries, OutputStream stream) {
        Cache<String, String> cache = getCache(cacheName);
        long counter = 0;
        try (JsonGenerator gen = Json.createGenerator(stream)) {
            gen.writeStartObject();
            for (Cache.Entry<String, String> entry : cache) {
                gen.write(entry.getKey(), entry.getValue());
                counter++;
            }
            gen.writeEnd();
        }
        return counter;
    }

    public String get(String cacheName, String key) {
        Cache<String, String> cache = getCache(cacheName);
        if (cache == null) {
            return null;
        }
        return cache.get(key);
    }

    public void put(String cacheName, String key, String value) {
        Cache<String, String> cache = getCache(cacheName);
        if (cache == null) {
            return;
        }
        cache.put(key, value);
    }

    public void remove(String cacheName, String key) {
        Cache<String, String> cache = getCache(cacheName);
        if (cache == null) {
            return;
        }
        cache.remove(key);
    }

    public void removeAll(String cacheName) {
        Cache<String, String> cache = getCache(cacheName);
        if (cache == null) {
            return;
        }
        cache.removeAll();
    }

    void registerListener(Cache<String, String> cache) {
        CacheEntryChangedListener listener = new CacheEntryChangedListener(cache.getName(), event::fire);
        Factory<CacheEntryChangedListener> listenerFactory = FactoryBuilder.factoryOf(listener);
        MutableCacheEntryListenerConfiguration configuration = new MutableCacheEntryListenerConfiguration(listenerFactory, null, false, true);
        cache.registerCacheEntryListener(configuration);
    }

}
