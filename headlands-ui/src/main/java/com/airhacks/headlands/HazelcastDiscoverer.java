package com.airhacks.headlands;

import com.hazelcast.cache.impl.CacheDistributedObject;
import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author airhacks.com
 */
public class HazelcastDiscoverer {

    public List<String> getMapNames() {
        Set<HazelcastInstance> allHazelcastInstances = Hazelcast.getAllHazelcastInstances();
        return allHazelcastInstances.stream().map(i -> i.getDistributedObjects()).
                map(this::convert).flatMap(list -> list.stream()).collect(Collectors.toList());
    }

    List<String> convert(Collection<DistributedObject> distributedObjects) {
        return distributedObjects.stream().map(this::extractCacheName).
                flatMap(list -> list.stream()).
                collect(Collectors.toList());
    }

    List<String> extractCacheName(DistributedObject distributedObject) {
        CacheDistributedObject cdo = (CacheDistributedObject) distributedObject;
        return cdo.getService().getCacheConfigs().
                stream().map(c -> c.getName()).
                collect(Collectors.toList());

    }

}
