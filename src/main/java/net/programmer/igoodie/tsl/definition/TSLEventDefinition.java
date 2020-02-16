package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.context.TSLEventArguments;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.*;

public abstract class TSLEventDefinition extends TSLDefinition {

    private Set<String> acceptedProperties;

    public TSLEventDefinition(String name) {
        super(name);
        this.acceptedProperties = Collections.unmodifiableSet(getSampleArguments().getMap().keySet());
    }

    public boolean acceptsProperty(String property) {
        return acceptedProperties.contains(property);
    }

    public Set<String> getAcceptedProperties() {
        return acceptedProperties;
    }

    /* ----------------------------------------- */

    @Override
    public final void validate(List<TSLToken> tokens, TSLContext context) {
        // A TSL Event definition is always valid, once its name is matched
    }

    @Override
    public final boolean satisfies(List<TSLToken> tokens, TSLContext context) {
        return true; // A TSL Event definition always satisfies the context, once its name is matched
    }

    @Override
    public abstract TSLEventArguments getSampleArguments();

}
