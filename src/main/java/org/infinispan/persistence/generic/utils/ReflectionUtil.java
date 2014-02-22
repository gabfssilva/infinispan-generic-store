package org.infinispan.persistence.generic.utils;

import java.lang.reflect.Constructor;

/**
 * @author gabriel
 */
public class ReflectionUtil {
    public static <T> T instantiate(Class<T> clazz, Class<?>[] parametersType, Object... parameters) {
        try {
            Constructor<T> constructor = clazz.getConstructor(parametersType);
            return constructor.newInstance(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
