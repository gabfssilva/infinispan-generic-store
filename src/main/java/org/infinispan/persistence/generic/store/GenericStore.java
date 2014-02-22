package org.infinispan.persistence.generic.store;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import net.jcip.annotations.ThreadSafe;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.infinispan.commons.io.ByteBuffer;
import org.infinispan.executors.ExecutorAllCompletionService;
import org.infinispan.marshall.core.MarshalledEntry;
import org.infinispan.metadata.InternalMetadata;
import org.infinispan.persistence.TaskContextImpl;
import org.infinispan.persistence.generic.cache.GenericCache;
import org.infinispan.persistence.generic.cache.exception.GenericCacheException;
import org.infinispan.persistence.generic.configuration.GenericStoreConfiguration;
import org.infinispan.persistence.generic.store.entry.CacheEntry;
import org.infinispan.persistence.generic.store.entry.CacheEntryBuilder;
import org.infinispan.persistence.generic.store.entry.KeyEntry;
import org.infinispan.persistence.generic.store.utils.ThreadPoolFactory;
import org.infinispan.persistence.generic.utils.ReflectionUtil;
import org.infinispan.persistence.spi.AdvancedLoadWriteStore;
import org.infinispan.persistence.spi.InitializationContext;
import org.infinispan.persistence.spi.PersistenceException;

/**
 * 
 * @author gabriel
 * 
 * @param <K>
 * @param <V>
 */
@ThreadSafe
public class GenericStore<K, V> implements AdvancedLoadWriteStore<K, V> {
	private InitializationContext context;
	private GenericStoreConfiguration configurations;
	private GenericCache<KeyEntry<K>, CacheEntry<K, V>> cache;

	@SuppressWarnings("unchecked")
	@Override
	public void init(InitializationContext ctx) {
		context = ctx;
		configurations = ctx.getConfiguration();

		Class<?> clazz[] = { GenericStoreConfiguration.class };
		cache = ReflectionUtil.instantiate(configurations.getCache(), clazz, configurations);
	}

	@Override
	public void start() {
		if (configurations.purgeOnStartup()) {
			cache.clear();
		}
	}

	@Override
	public void stop() {
	}

	@Override
	public void write(final MarshalledEntry<K, V> entry) {
        CacheEntryBuilder<K, V> builder = new CacheEntryBuilder<K, V>();

        builder.key(entry.getKey())
                .value(entry.getValue())
                .expiration(entry.getMetadata().expiryTime())
                .keyByteBuffer(entry.getKeyBytes())
                .valueByteBuffer(entry.getValueBytes())
                .metadataByteBuffer(entry.getMetadataBytes());

        CacheEntry<K, V> serializable = builder.create();

        synchronized (cache) {
            cache.put(serializable.getKey(), serializable);
        }
	}


	@Override
	public boolean delete(K key) {
		synchronized (cache) {
			return cache.remove(key) != null;
		}
	}

	@Override
	public MarshalledEntry<K, V> load(K key) {
		return load(key, false);
	}

	@SuppressWarnings("unchecked")
	public MarshalledEntry<K, V> load(K key, boolean byteBuffer) {
		CacheEntry<K, V> value = null;

		synchronized (cache) {
			value = cache.get(createKeyEntry(key));

			if (value == null) {
				return null;
			}

			ByteBuffer metadataBytes = context.getByteBufferFactory().newByteBuffer(value.getMetadataByteBuffer().getBuf(), value.getMetadataByteBuffer().getOffset(),
					value.getMetadataByteBuffer().getLength());

			try {
				InternalMetadata metadata = (InternalMetadata) context.getMarshaller().objectFromByteBuffer(metadataBytes.getBuf());
				if (byteBuffer) {
					ByteBuffer keyBuffer = context.getByteBufferFactory().newByteBuffer(value.getKeyByteBuffer().getBuf(), value.getKeyByteBuffer().getOffset(),
							value.getKeyByteBuffer().getLength());

					ByteBuffer valueBuffer = context.getByteBufferFactory().newByteBuffer(value.getValueByteBuffer().getBuf(), value.getValueByteBuffer().getOffset(),
							value.getValueByteBuffer().getLength());

					return context.getMarshalledEntryFactory().newMarshalledEntry(keyBuffer, valueBuffer, metadataBytes);
				}

				return context.getMarshalledEntryFactory().newMarshalledEntry(value.getKey(), value.getValue(), metadata);
			} catch (Exception e) {
				throw new PersistenceException("Error while loading object from cache", e);
			}
		}
	}

	@Override
	public boolean contains(K key) {
		synchronized (cache) {
			return cache.containsKey(createKeyEntry(key));
		}
	}

	@Override
	public void process(KeyFilter<K> filter, final CacheLoaderTask<K, V> task, Executor executor, final boolean fetchValue, final boolean fetchMetadata) {
        ExecutorAllCompletionService eacs = new ExecutorAllCompletionService(executor);
        synchronized (cache) {
			final TaskContextImpl taskContext = new TaskContextImpl();
			for (final KeyEntry<K> keyEntry : cache.keySet()) {
				if (filter == null || filter.shouldLoadKey(keyEntry.getKey())) {
					if (taskContext.isStopped()) {
						break;
					}
                    eacs.submit(new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            try {
                                final MarshalledEntry marshalledEntry = load(keyEntry.getKey());
                                if (marshalledEntry != null) {
                                    task.processEntry(marshalledEntry, taskContext);
                                }
                                return null;
                            } catch (Exception e) {
                                throw e;
                            }
                        }
                    });
                }
            }
		}
        eacs.waitUntilAllCompleted();
        if (eacs.isExceptionThrown()) {
            throw new PersistenceException("Execution exception!", eacs.getFirstException());
        }
	}

	@Override
	public int size() {
		synchronized (cache) {
			return cache.size();
		}
	}

	@Override
	public void clear() {
		synchronized (cache) {
			cache.clear();
		}
	}

    public KeyEntry<K> createKeyEntry(K key){
        return new KeyEntry<K>(key);
    }

	@Override
	public void purge(Executor threadPool, org.infinispan.persistence.spi.AdvancedCacheWriter.PurgeListener listener) {
		synchronized (cache) {
			System.out.println("purging...");
			try {
				cache.removeExpiredData();
			} catch (GenericCacheException e) {
				throw new PersistenceException("Error while purging cache", e);
			}
			System.out.println(cache.size());
		}
	}
}
