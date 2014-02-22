package org.infinispan.persistence.generic.cache.builders;

import org.infinispan.configuration.cache.AbstractPersistenceConfigurationChildBuilder;
import org.infinispan.configuration.cache.PersistenceConfigurationBuilder;
import org.infinispan.persistence.generic.cache.GenericCacheConfiguration;

public abstract class GenericCacheConfigurationBuilder<T extends GenericCacheConfiguration, LB>  extends AbstractPersistenceConfigurationChildBuilder implements CacheConfigurationBuilder<T>{
	public GenericCacheConfigurationBuilder(PersistenceConfigurationBuilder builder) {
		super(builder);
	}
}
