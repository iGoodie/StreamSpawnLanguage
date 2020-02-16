package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;
import java.util.Objects;

public abstract class TSLPredicateDefinition {

    private String name;

    public TSLPredicateDefinition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract boolean satisfied(List<TSLToken> tokens, TSLContext context);

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;

        if (!(other instanceof TSLPredicateDefinition))
            return false;

        TSLPredicateDefinition that = (TSLPredicateDefinition) other;
        return this.name.equalsIgnoreCase(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
