package org.infinispan.persistence.generic.store.utils;

import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * Created by gabriel on 2/20/14.
 */
public class ThreadPoolFactory {
    private static ThreadPoolFactory factory;
    private GenericObjectPool<Runnable> pool;

    private ThreadPoolFactory(Integer maxActive, Runnable runnable){
        pool = new GenericObjectPool<Runnable>(new ThreadPoolableObjectFactory(runnable));

        pool.setMaxActive(maxActive);
        pool.setMaxIdle(maxActive);
        pool.setMinIdle(maxActive);
    }

    public static ThreadPoolFactory getInstance(Integer maxActive, Runnable runnable){
        if(factory==null){
            factory = new ThreadPoolFactory(maxActive, runnable);
        }
        return factory;
    }

    public GenericObjectPool<Runnable> getPool(){
        return pool;
    }
}
