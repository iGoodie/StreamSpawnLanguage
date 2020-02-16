package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.runtime.context.TSLEventArguments;

import java.util.*;

public abstract class TSLEventDefinition {

    private String name;
    private Set<String> acceptedProperties;

    public TSLEventDefinition(String name) {
        this.name = name;
        this.acceptedProperties = Collections.unmodifiableSet(getSampleArguments().getMap().keySet());
    }

    public String getName() {
        return name;
    }

    public boolean acceptsProperty(String property) {
        return acceptedProperties.contains(property);
    }

    public Set<String> getAcceptedProperties() {
        return acceptedProperties;
    }

    public abstract TSLEventArguments getSampleArguments();

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;

        if (!(other instanceof TSLEventDefinition))
            return false;

        TSLEventDefinition that = (TSLEventDefinition) other;
        return this.name.equalsIgnoreCase(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
