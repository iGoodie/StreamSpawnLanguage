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

    /**
     * Validates given syntax for the definition. If it is not valid, throws and exception
     *
     * @param tokens  Tokens to be interpreted and validated
     * @param context Sample context to test validation
     * @throws TSLSyntaxError if the syntax is not valid
     */
    public abstract void validateSyntax(List<TSLToken> tokens, TSLContext context) throws TSLSyntaxError;

    /**
     * Calculates whether given tokens satisfies the context or not,
     * meaning whether the flow process shall continue to the next node or stop execution here.
     *
     * @param tokens  Tokens to be checked whether they satisfy or not
     * @param context Actual context to be checked with
     * @return A flag representing whether the flow process shall continue to the next node or not
     */
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
