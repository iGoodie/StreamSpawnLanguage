package net.programmer.igoodie.tsl.runtime.context;

import java.util.HashMap;
import java.util.Map;

public abstract class TSLArguments {

    private Map<String, Object> data;

    public TSLArguments() {
        this.data = new HashMap<>();
    }

    public TSLArguments with(String property, Object value) {
        this.data.put(property, value);
        return this;
    }

    public Map<String, Object> getMap() {
        return data;
    }

}
