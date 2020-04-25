package net.programmer.igoodie.exampleplugin.predicates;

import net.programmer.igoodie.tsl.definition.TSLPredicateDefinition;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;

public class ExampleIfPredicate extends TSLPredicateDefinition {

    public ExampleIfPredicate() {
        super("IF");
    }

    @Override
    public void validateSyntax(List<TSLToken> tokens, TSLContext context) throws TSLSyntaxError {
        if (tokens.size() != 2 && !tokens.get(1).isExpression()) {
            throw new TSLSyntaxError(
                    getName() + " predicate requires exactly one argument, which must be an expression token!",
                    TSLSyntaxError.causedNear(tokens)
            );
        }

        TSLToken predicateExpression = tokens.get(1);
        String value = predicateExpression.calculateValue(context);

        if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
            throw new TSLSyntaxError(
                    predicateExpression.getRaw() + " must be a boolean expression!",
                    TSLSyntaxError.causedNear(tokens)
            );
        }
    }

    @Override
    public boolean satisfies(List<TSLToken> tokens, TSLContext context) {
        return tokens.get(1).calculateValue(context).equalsIgnoreCase("true");
    }

}
