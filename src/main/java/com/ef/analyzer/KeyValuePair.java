package com.ef.analyzer;
/**
 * Generic key/value pair.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public class KeyValuePair<K, V> {


    private K key;
    private V value;

    public KeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
