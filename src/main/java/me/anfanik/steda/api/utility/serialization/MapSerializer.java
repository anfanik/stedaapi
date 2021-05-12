package me.anfanik.steda.api.utility.serialization;

import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
public class MapSerializer<K, V> {

    private final Function<K, String> keySerializer;
    private final Function<V, String> valueSerializer;
    private final Function<String, K> keyDeserializer;
    private final Function<String, V> valueDeserializer;

    public String serialize(Map<K, V> map) {
        return serialize(map, keySerializer, valueSerializer);
    }

    public Map<K, V> deserialize(String serialized) {
        return deserialize(serialized, keyDeserializer, valueDeserializer);
    }

    public static <K, V> MapSerializer<K, V> getSerializer(Function<K, String> keySerializer, Function<V, String> valueSerializer, Function<String, K> keyDeserializer, Function<String, V> valueDeserializer) {
        return new MapSerializer<K, V>(keySerializer, valueSerializer, keyDeserializer, valueDeserializer);
    }

    public static <K, V> String serialize(Map<K, V> map, Function<K, String> keySerializer, Function<V, String> valueSerializer) {
        StringBuilder builder = new StringBuilder("[");

        boolean first = true;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (first) {
                first = false;
            } else {
                builder.append(';');
            }

            K key = entry.getKey();
            V value = entry.getValue();

            builder.append('"').append(keySerializer.apply(key)
                    .replace(":", "\\:")
                    .replace(";", "\\;")).append('"')
                    .append(":")
                    .append('"').append(valueSerializer.apply(value)
                    .replace(":", "\\:")
                    .replace(";", "\\;")).append('"');
        }

        return builder.append(']').toString();
    }

    public static <K, V> Map<K, V> deserialize(String serialised, Function<String, K> keyDeserializer, Function<String, V> valueDeserializer) {
        Map<K, V> map = new LinkedHashMap<>();
        if (serialised == null) {
            return map;
        }
        serialised = serialised.substring(1, serialised.length() - 1);
        for (String keyValue : serialised.split("(?<!\\\\);")) {
            if (keyValue.isEmpty()) {
                continue;
            }
            String[] parts = keyValue.split("(?<!\\\\):");

            String keyRaw = parts[0];
            keyRaw = keyRaw.replace("\\:", ":");
            keyRaw = keyRaw.replace("\\;", ";");
            keyRaw = keyRaw.substring(1, keyRaw.length() - 1);
            K key = keyDeserializer.apply(keyRaw);

            String valueRaw = parts[1];
            valueRaw = valueRaw.replace("\\:", ":");
            valueRaw = valueRaw.replace("\\;", ";");
            valueRaw = valueRaw.substring(1, valueRaw.length() - 1);
            V value = valueDeserializer.apply(valueRaw);

            map.put(key, value);
        }
        return map;
    }

}
