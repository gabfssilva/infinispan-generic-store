package org.infinispan.persistence.generic.cache;

import org.infinispan.persistence.generic.cache.exception.GenericCacheException;
import org.infinispan.persistence.generic.configuration.GenericStoreConfiguration;

/**
 * @param <K>
 * @param <V>
 * @author gabriel
 */
public abstract class AbstractCache<K, V> implements GenericCache<K, V> {
    protected GenericStoreConfiguration configuration;

    public AbstractCache(GenericStoreConfiguration configuration) throws GenericCacheException {
        this.configuration = configuration;
        init();
    }
}
