package org.infinispan.persistence.generic.cache.exception;

public class GenericCacheException extends Exception {
    private static final long serialVersionUID = 3052262406439827479L;

    public GenericCacheException() {
        super();
    }

    public GenericCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericCacheException(String message) {
        super(message);
    }

    public GenericCacheException(Throwable cause) {
        super(cause);
    }
}
