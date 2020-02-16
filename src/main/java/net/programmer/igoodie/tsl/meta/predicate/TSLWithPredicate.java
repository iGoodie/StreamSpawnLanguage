package net.programmer.igoodie.tsl.meta.predicate;

import net.programmer.igoodie.tsl.definition.TSLEventDefinition;
import net.programmer.igoodie.tsl.definition.TSLPredicateDefinition;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.runtime.context.TSLArguments;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;

public class TSLWithPredicate extends TSLPredicateDefinition {

    public TSLWithPredicate() {
        super("WITH");
    }

    @Override
    public void validate(List<TSLToken> tokens, TSLContext context) throws TSLSyntaxError {
        if (tokens.size() < 3) {
            throw new TSLSyntaxError(
                    "Expected at least 3 tokens",
                    TSLSyntaxError.causedNear(tokens)
            );
        }

        TSLToken propertyToken = tokens.get(1);

        if (!propertyToken.isPlain()) {
            throw new TSLSyntaxError(
                    "Expected first token to be a plain token, found " + propertyToken.getType() + "instead",
                    TSLSyntaxError.causedNear(tokens)
            );
        }

        TSLEventDefinition eventDefinition = context.getEventDefinition();
        String propertyTokenValue = propertyToken.getValue(context);

        if (!eventDefinition.acceptsProperty(propertyTokenValue)) {
            throw new TSLSyntaxError(
                    "The property '" + propertyTokenValue + "' is not accepted by " + eventDefinition.getName(),
                    TSLSyntaxError.causedNear(tokens)
            );
        }

        // TODO: Check if last token is a value (?)
        // TODO: Implement comparator logic, and check if remaining tokens form a comparator
    }

    @Override
    public boolean satisfies(List<TSLToken> tokens, TSLContext context) {
        return true; // TODO
    }

    @Override
    public TSLArguments getSampleArguments() {
        return new TSLArguments(); // TODO
    }

}
