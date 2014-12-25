package com.airhacks.headlands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static javafx.application.Platform.runLater;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javax.annotation.PostConstruct;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

/**
 *
 * @author airhacks.com
 */
public class CacheAccessor {

    private Cache<String, String> currentCache;
    private CachingProvider cachingProvider;
    private CacheManager cacheManager;

    private SimpleBooleanProperty cacheStarted;

    private ExecutorService executor;

    @PostConstruct
    public void initialize() {
        System.setProperty("hazelcast.jcache.provider.type", "server");
        this.cacheStarted = new SimpleBooleanProperty();
        this.executor = Executors.newCachedThreadPool();
    }

    public void store(String keyString, String valueString) {
        this.currentCache.put(keyString, valueString);
    }

    public List<String> getCacheNames() {
        List<String> caches = new ArrayList<>();
        Iterable<String> names = this.cacheManager.getCacheNames();
        names.forEach(caches::add);
        return caches;
    }

    public void start() {
        this.executor.submit(() -> {
            this.cachingProvider = Caching.getCachingProvider();
            this.cacheManager = cachingProvider.getCacheManager();
            runLater(() -> this.cacheStarted.set(true));
        }
        );
    }

    public BooleanProperty isStarted() {
        return cacheStarted;
    }

    public void stop() {
        this.executor.submit(() -> {
            this.currentCache.close();
            this.cacheManager.close();
            this.cachingProvider.close();
            runLater(() -> this.cacheStarted.set(false));

        });

    }

    public void createCache(String cacheName) {
        MutableConfiguration<String, String> configuration = new MutableConfiguration<>();

        configuration.setStoreByValue(false).
                setTypes(String.class, String.class).
                setManagementEnabled(true).
                setStoreByValue(true);

        this.currentCache = cacheManager.
                createCache(cacheName, configuration);

    }

    public String getValue(String key) {
        return this.currentCache.get(key);
    }

    public void remove(String key) {
        this.currentCache.remove(key);
    }

}
