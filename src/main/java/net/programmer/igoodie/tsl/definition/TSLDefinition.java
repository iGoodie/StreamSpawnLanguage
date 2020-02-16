package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.runtime.context.TSLArguments;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;
import java.util.Objects;

public abstract class TSLDefinition {

    private String name;

    public TSLDefinition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /* ----------------------------------------- */

    public abstract void validate(List<TSLToken> tokens, TSLContext context) throws TSLSyntaxError;

    public abstract boolean satisfies(List<TSLToken> tokens, TSLContext context);

    public abstract TSLArguments getSampleArguments();

    /* ----------------------------------------- */

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;

        if (other == null || this.getClass() != other.getClass())
            return false;

        TSLDefinition that = (TSLDefinition) other;
        return Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
