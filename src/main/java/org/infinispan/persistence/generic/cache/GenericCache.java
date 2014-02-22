package org.infinispan.persistence.generic.cache;

import org.infinispan.persistence.generic.cache.exception.GenericCacheException;

import java.util.Map;

/**
 * @param <K>
 * @param <V>
 * @author gabriel
 */
public interface GenericCache<K, V> extends Map<K, V> {
    void init() throws GenericCacheException;

    void removeExpiredData() throws GenericCacheException;
}
