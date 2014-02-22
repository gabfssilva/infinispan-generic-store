package org.infinispan.persistence.generic.store.utils;

import org.apache.commons.pool.BasePoolableObjectFactory;

import java.lang.reflect.Method;

/**
 * Created by gabriel on 2/20/14.
 */
public class ThreadPoolableObjectFactory extends BasePoolableObjectFactory<Runnable> {
    public Runnable runnable;

    public ThreadPoolableObjectFactory(Runnable runnable){
        this.runnable = runnable;
    }

    @Override
    public Runnable makeObject() throws Exception {
        return runnable;
    }
}
