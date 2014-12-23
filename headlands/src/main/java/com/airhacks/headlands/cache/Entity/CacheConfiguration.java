package com.airhacks.headlands.cache.Entity;

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

    public boolean isStoreByValue() {
        return storeByValue;
    }

    public boolean isManagementEnabled() {
        return managementEnabled;
    }

    public boolean isStatisticsEnabled() {
        return statisticsEnabled;
    }

}
