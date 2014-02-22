package org.infinispan.persistence.generic.store.entry;

import org.infinispan.commons.io.ByteBuffer;

import java.util.Date;

public class CacheEntryBuilder<K, V> {
    private SerializableByteBuffer keyByteBuffer;
    private SerializableByteBuffer valueByteBuffer;
    private SerializableByteBuffer metadataByteBuffer;

    private KeyEntry<K> key;
    private V value;

    private Date expiration;

    public CacheEntryBuilder<K, V> expiration(Long expiration) {
        this.expiration = new Date(expiration);
        return this;
    }

    public CacheEntryBuilder<K, V> expiration(Date expiration) {
        this.expiration = expiration;
        return this;
    }

    public CacheEntryBuilder<K, V> value(V value) {
        this.value = value;
        return this;
    }

    public CacheEntryBuilder<K, V> key(K key) {
        this.key = new KeyEntry<K>(key);
        return this;
    }

    public CacheEntryBuilder<K, V> keyByteBuffer(ByteBuffer byteBuffer) {
        keyByteBuffer = new SerializableByteBuffer(byteBuffer.getBuf(), byteBuffer.getLength(), byteBuffer.getOffset());
        return this;
    }

    public CacheEntryBuilder<K, V> valueByteBuffer(ByteBuffer byteBuffer) {
        valueByteBuffer = new SerializableByteBuffer(byteBuffer.getBuf(), byteBuffer.getLength(), byteBuffer.getOffset());
        return this;
    }

    public CacheEntryBuilder<K, V> metadataByteBuffer(ByteBuffer byteBuffer) {
        metadataByteBuffer = new SerializableByteBuffer(byteBuffer.getBuf(), byteBuffer.getLength(), byteBuffer.getOffset());
        return this;
    }

    public CacheEntry<K, V> create() {
        return new CacheEntry<K, V>(keyByteBuffer, valueByteBuffer, metadataByteBuffer, key, value, expiration);
    }
}