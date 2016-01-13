package com.airhacks.headlands.cache.entity;

import java.time.Duration;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author airhacks.com
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CacheConfiguration {

    private boolean storeByValue;
    private boolean managementEnabled;
    private boolean statisticsEnabled;
    private boolean readThrough;
    private boolean writeThrough;
    private long expiryForAccess;
    private long expiryForCreation;
    private long expiryForUpdate;

    public CacheConfiguration() {
        this.storeByValue = false;
        this.managementEnabled = true;
        this.statisticsEnabled = true;
        this.readThrough = true;
        this.writeThrough = true;
        this.expiryForAccess = Duration.ofMinutes(5).toMillis();
    }

    public CacheConfiguration(long expiryForAccess, long expiryForCreation,
            long expiryForUpdate, boolean storeByValue,
            boolean managementEnabled, boolean statisticsEnabled,
            boolean readThrough, boolean writeThrough) {
        this.expiryForAccess = expiryForAccess;
        this.expiryForCreation = expiryForCreation;
        this.expiryForUpdate = expiryForUpdate;
        this.storeByValue = storeByValue;
        this.managementEnabled = managementEnabled;
        this.statisticsEnabled = statisticsEnabled;
        this.readThrough = readThrough;
        this.writeThrough = writeThrough;
    }

    public boolean isStoreByValue() {
        return storeByValue;
    }

    public boolean isManagementEnabled() {
        return managementEnabled;
    }

    public boolean isStatisticsEnabled() {
        return statisticsEnabled;
    }

    public boolean isReadThrough() {
        return readThrough;
    }

    public boolean isWriteThrough() {
        return writeThrough;
    }

    public long getExpiryForAccess() {
        return expiryForAccess;
    }

    public long getExpiryForCreation() {
        return expiryForCreation;
    }

    public long getExpiryForUpdate() {
        return expiryForUpdate;
    }

}
