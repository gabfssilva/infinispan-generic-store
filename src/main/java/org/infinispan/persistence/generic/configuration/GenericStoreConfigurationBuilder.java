package org.infinispan.persistence.generic.configuration;

import org.infinispan.commons.configuration.Builder;
import org.infinispan.commons.util.TypedProperties;
import org.infinispan.configuration.cache.AbstractStoreConfigurationBuilder;
import org.infinispan.configuration.cache.PersistenceConfigurationBuilder;
import org.infinispan.persistence.generic.cache.GenericCache;
import org.infinispan.persistence.generic.cache.builders.CacheConfigurationBuilder;
import org.infinispan.persistence.generic.utils.ReflectionUtil;

/**
 * 
 * @author gabriel
 *
 */
@SuppressWarnings("rawtypes")
public class GenericStoreConfigurationBuilder extends AbstractStoreConfigurationBuilder<GenericStoreConfiguration, GenericStoreConfigurationBuilder> {
	private Class<? extends GenericCache> cache;
	private CacheConfigurationBuilder cacheBuilder;
	private PersistenceConfigurationBuilder persistenceConfigurationBuilder;
	
	public GenericStoreConfigurationBuilder(PersistenceConfigurationBuilder builder) {
		super(builder);
		persistenceConfigurationBuilder = builder;
	}
	
	@Override
	public GenericStoreConfiguration create() {
		return new GenericStoreConfiguration(cache, cacheBuilder, purgeOnStartup, fetchPersistentState, ignoreModifications, this.async.create(), this.singletonStore.create(), preload, shared,
				TypedProperties.toTypedProperties(properties));
	}
	
	public GenericStoreConfigurationBuilder cache(Class<? extends GenericCache> cache){
		this.cache = cache;
		return self();
	}
	
	public <T extends CacheConfigurationBuilder> T cacheBuilder(Class<T> cacheBuilder){
		Class<?> [] parametersType = {PersistenceConfigurationBuilder.class};
		CacheConfigurationBuilder instance = ReflectionUtil.instantiate(cacheBuilder, parametersType, persistenceConfigurationBuilder);
		this.cacheBuilder = instance;
		return (T) this.cacheBuilder;
	}
	
	@Override
	public Builder<?> read(GenericStoreConfiguration template) {
		this.fetchPersistentState = template.fetchPersistentState();
		this.ignoreModifications = template.ignoreModifications();
		this.properties = template.properties();
		this.purgeOnStartup = template.purgeOnStartup();
		this.cache = template.getCache();

		this.async.read(template.async());
		this.singletonStore.read(template.singletonStore());
		this.cacheBuilder = template.getCacheConfigurationBuilder();

		return self();
	}

	@Override
	public GenericStoreConfigurationBuilder self() {
		return this;
	}
}
