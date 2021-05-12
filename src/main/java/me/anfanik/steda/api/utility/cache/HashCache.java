package me.anfanik.steda.api.utility.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@RequiredArgsConstructor
public class HashCache<K, V> implements Cache<K, V> {

    @Getter
    private final Map<K, V> handle = new HashMap<>();
    private final Map<K, Long> times = new HashMap<>();

    private final long expireAfterMillis;

    public HashCache(int expireAfter, TimeUnit unit) {
        this(unit.toMillis(expireAfter));
    }

    @Override
    public V get(K key) {
        V value = handle.get(key);
        if (isExpired(key)) {
            handle.remove(key);
            return null;
        }
        return value;
    }

    @Override
    public V put(K key, V value) {
        V previous = handle.put(key, value);
        times.put(key, System.currentTimeMillis());
        if (isExpired(key)) {
            previous = null;
        }
        //TODO: time
        return previous;
    }

    @Override
    public V remove(K key) {
        V previous = handle.remove(key);
        if (isExpired(key)) {
            previous = null;
        }
        handle.remove(key);
        times.remove(key);
        return previous;
    }

    @Override
    public void clear() {
        handle.clear();
        times.clear();
    }

    @Override
    public V computeIfAbsent(K key, Function<K, ? extends V> mappingFunction) {
        V value = get(key);
        if (value == null) {
            value = mappingFunction.apply(key);
            put(key, value);
        }
        return value;
    }

    @Override
    public V computeIfPresent(K key, Function<K, ? extends V> remappingFunction) {
        return put(key, remappingFunction.apply(key));
    }

    @Override
    public boolean containsKey(K key) {
        return handle.containsKey(key) && times.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        return handle.containsValue(value);
    }

    @Override
    public boolean isExpired(K key) {
        if (!handle.containsKey(key) || !times.containsKey(key)) {
            return true;
        }

        long current = System.currentTimeMillis();
        long putted = times.get(key);
        return current - putted > expireAfterMillis;
    }

}
