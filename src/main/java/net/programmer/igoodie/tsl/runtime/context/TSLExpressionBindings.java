package net.programmer.igoodie.tsl.runtime.context;

import net.programmer.igoodie.tsl.exception.TSLPluginError;
import net.programmer.igoodie.tsl.lambda.TSLBindingProvider;
import net.programmer.igoodie.tsl.lambda.TSLStringProvider;

import javax.script.Bindings;
import java.util.*;

public class TSLExpressionBindings implements Bindings {

    private static final Map<String, TSLBindingProvider> BINDING_PROVIDERS = new HashMap<>();
    private static final Map<String, Object> GLOBAL_BINDINGS = new HashMap<>();

    static {
        GLOBAL_BINDINGS.put("version", "0.0.1");
        GLOBAL_BINDINGS.put("getVersion", (TSLStringProvider) (() -> "0.0.1"));
    }

    /* ------------------------------------------ */

    private Map<String, Object> bindingMap;
    private List<Map<String, Object>> argumentList;

    public TSLExpressionBindings() {
        this.bindingMap = new HashMap<>();
        this.argumentList = new LinkedList<>();
    }

    public void attachContext(TSLContext context) {
        TSLEventArguments eventArguments = context.getEventArguments();
        if (eventArguments != null) attachArguments(eventArguments.getMap());

        TSLActionArguments actionArguments = context.getActionArguments();
        if (actionArguments != null) attachArguments(actionArguments.getMap());
    }

    public void attachArguments(Map<String, Object> arguments) {
        this.argumentList.add(arguments);
    }

    public void deattachContext() {
        this.argumentList.clear();
    }

    @Override
    public Object put(String name, Object value) {
        return bindingMap.put(name, value);
    }

    @Override
    public void putAll(Map<? extends String, ?> toMerge) {
        bindingMap.putAll(toMerge);
    }

    @Override
    public void clear() {
        bindingMap.clear();
    }

    @Override
    public Set<String> keySet() {
        return bindingMap.keySet();
    }

    @Override
    public Collection<Object> values() {
        return bindingMap.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return bindingMap.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public int size() {
        return bindingMap.size()
                + GLOBAL_BINDINGS.size()
                + argumentList.stream().mapToInt(Map::size).sum();
    }

    @Override
    public boolean isEmpty() {
        return bindingMap.isEmpty()
                && GLOBAL_BINDINGS.isEmpty()
                && argumentList.stream().allMatch(Map::isEmpty);
    }

    @Override
    public boolean containsKey(Object key) {
        return bindingMap.containsKey(key)
                || GLOBAL_BINDINGS.containsKey(key)
                || argumentList.stream().anyMatch(map -> map.containsKey(key));
    }

    @Override
    public boolean containsValue(Object value) {
        return bindingMap.containsValue(value)
                || GLOBAL_BINDINGS.containsValue(value)
                || argumentList.stream().anyMatch(map -> map.containsValue(value));
    }

    @Override
    public Object get(Object key) {
        Object metaValue = GLOBAL_BINDINGS.get(key);
        if (metaValue != null) return metaValue;

        Object bindingValue = bindingMap.get(key);
        if (bindingValue != null) return bindingValue;

        for (Map<String, Object> arguments : argumentList) {
            Object argumentValue = arguments.get(key);
            if (argumentValue != null) return argumentValue;
        }

        return null;
    }

    @Override
    public Object remove(Object key) {
        return bindingMap.remove(key);
    }

    /* ------------------------------------------ */

    public static void registerBindingProvider(String name, TSLBindingProvider provider) throws TSLPluginError {
        if (GLOBAL_BINDINGS.containsKey(name)) {
            throw new TSLPluginError("Cannot register a global binding: " + name);
        }

        BINDING_PROVIDERS.put(name, provider);
    }

    public static void updateBinding(TSLExpressionBindings bindings) {
        BINDING_PROVIDERS.forEach((name, provider)
                -> bindings.put(name, provider.provideBind()));
    }

}
