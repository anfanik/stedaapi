package me.anfanik.steda.api.utility.cache;

import java.util.function.Function;

public interface Cache<K, V> {

    V get(K key);

    V put(K key, V value);

    V remove(K key);

    void clear();

    V computeIfAbsent(K key, Function<K, ? extends V> mappingFunction);

    V computeIfPresent(K key, Function<K, ? extends V> remappingFunction);

    boolean containsKey(K key);

    boolean containsValue(V value);

    boolean isExpired(K key);

}
