package org.infinispan.persistence.generic.cache;

import java.util.Map;

import org.infinispan.persistence.generic.cache.exception.GenericCacheException;

/**
 * 
 * @author gabriel
 *
 * @param <K>
 * @param <V>
 */
public interface GenericCache<K, V> extends Map<K, V> {
	void init() throws GenericCacheException;
	void removeExpiredData() throws GenericCacheException;
}
