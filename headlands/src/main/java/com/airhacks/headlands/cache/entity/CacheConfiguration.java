package com.airhacks.headlands.cache.entity;

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

    boolean storeByValue;
    boolean managementEnabled;
    boolean statisticsEnabled;
    boolean readThrough;
    boolean writeThrough;

    public CacheConfiguration() {
    }

    public CacheConfiguration(boolean storeByValue, boolean managementEnabled, boolean statisticsEnabled, boolean readThrough, boolean writeThrough) {
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

}
