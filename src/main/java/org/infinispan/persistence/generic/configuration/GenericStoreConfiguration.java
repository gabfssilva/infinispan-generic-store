package org.infinispan.persistence.generic.configuration;

import org.infinispan.commons.configuration.BuiltBy;
import org.infinispan.commons.configuration.ConfigurationFor;
import org.infinispan.configuration.cache.AbstractStoreConfiguration;
import org.infinispan.configuration.cache.AsyncStoreConfiguration;
import org.infinispan.configuration.cache.SingletonStoreConfiguration;
import org.infinispan.persistence.generic.cache.GenericCache;
import org.infinispan.persistence.generic.cache.builders.CacheConfigurationBuilder;
import org.infinispan.persistence.generic.store.GenericStore;

import java.util.Properties;

/**
 * This class represents the GenericStore configuration
 *
 * @author Gabriel F.
 */
@BuiltBy(GenericStoreConfigurationBuilder.class)
@ConfigurationFor(GenericStore.class)
@SuppressWarnings("rawtypes")
public class GenericStoreConfiguration extends AbstractStoreConfiguration {
    protected Class<? extends GenericCache> cache;
    private CacheConfigurationBuilder cacheConfiguration;

    public GenericStoreConfiguration(Class<? extends GenericCache> cache, CacheConfigurationBuilder cacheConfiguration, boolean purgeOnStartup, boolean fetchPersistentState, boolean ignoreModifications,
                                     AsyncStoreConfiguration async, SingletonStoreConfiguration singletonStore, boolean preload, boolean shared, Properties properties) {
        super(purgeOnStartup, fetchPersistentState, ignoreModifications, async, singletonStore, preload, shared, properties);
        this.cache = cache;
        this.cacheConfiguration = cacheConfiguration;
    }

    public Class<? extends GenericCache> getCache() {
        return cache;
    }


    public CacheConfigurationBuilder getCacheConfigurationBuilder() {
        return cacheConfiguration;
    }

    public String getProperty(String key) {
        return (String) properties().get(key);
    }
}
