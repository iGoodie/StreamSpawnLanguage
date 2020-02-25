package net.programmer.igoodie.tsl.runtime.context;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TSLArguments {

    private Map<String, Object> data;

    public TSLArguments() {
        this.data = new HashMap<>();
    }

    public TSLArguments with(String property, Object value) {
        put(property, value);
        return this;
    }

    public void put(String property, Object value) {
        data.put(property, value);

    }

    public Map<String, Object> getMap() {
        return data;
    }

    /* -------------------------------------- */

    public <T> T get(String property, Class<T> type, T defaultValue) {
        Object value = data.get(property);

        if (!type.isInstance(value))
            return defaultValue;

        return type.cast(value);
    }

    public String getString(String property) {
        return get(property, String.class, null);
    }

    public Number getNumber(String property) {
        return get(property, Number.class, 0);
    }

    public int getInteger(String property) {
        return getNumber(property).intValue();
    }

    public long getLong(String property) {
        return getNumber(property).longValue();
    }

    public float getFloat(String property) {
        return getNumber(property).floatValue();
    }

    public double getDouble(String property) {
        return getNumber(property).doubleValue();
    }

    /* -------------------------------------- */

    @Override
    public String toString() {
        return data.entrySet().stream()
                .map(entry -> String.format("\"%s\": %s", entry.getKey(), valueToString(entry.getValue())))
                .collect(Collectors.joining(", ", "{ ", " }"));
    }

    private String valueToString(Object value) {
        if (value instanceof String)
            return '"' + value.toString() + '"';

        return value.toString();
    }

}
