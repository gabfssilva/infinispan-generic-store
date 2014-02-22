package org.infinispan.persistence.generic.cache.builders;

import org.infinispan.persistence.generic.cache.GenericCacheConfiguration;

/**
 * 
 * @author gabriel
 *
 */
public interface CacheConfigurationBuilder<T extends GenericCacheConfiguration> {
	T create();
}
