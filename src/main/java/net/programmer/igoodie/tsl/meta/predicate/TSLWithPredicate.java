package net.programmer.igoodie.tsl.meta.predicate;

import net.programmer.igoodie.tsl.definition.TSLEventDefinition;
import net.programmer.igoodie.tsl.definition.TSLPredicateDefinition;
import net.programmer.igoodie.tsl.exception.TSLPluginError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.meta.comparator.*;
import net.programmer.igoodie.tsl.runtime.context.TSLArguments;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TSLWithPredicate extends TSLPredicateDefinition {

    public TSLWithPredicate() {
        super("WITH");
    }

    private boolean isNegated(List<TSLToken> comparatorTokens) {
        return comparatorTokens.get(0).getRaw().equalsIgnoreCase("NOT");
    }

    private String getComparatorSymbol(List<TSLToken> predicateArguments, TSLContext context) {
        List<TSLToken> comparatorTokens = predicateArguments.subList(1, predicateArguments.size() - 1);

        if (isNegated(comparatorTokens)) {
            comparatorTokens = comparatorTokens.subList(1, comparatorTokens.size());
        }

        return comparatorTokens.stream()
                .map(token -> token.calculateValue(context))
                .collect(Collectors.joining(" "));
    }

    @Override
    public void validateSyntax(List<TSLToken> tokens, TSLContext context) throws TSLSyntaxError {
        List<TSLToken> predicateArguments = tokens.subList(1, tokens.size());

        if (predicateArguments.size() < 3 && predicateArguments.size() != 1) {
            throw new TSLSyntaxError(
                    "Expected exactly 1 or at least 3 tokens",
                    TSLSyntaxError.causedNear(tokens)
            );
        }

        // Expecting a boolean
        if (predicateArguments.size() == 1) {
            String condition = predicateArguments.get(0).calculateValue(context);
            if (!condition.equals("true") && !condition.equals("false")) {
                throw new TSLSyntaxError(
                        String.format("Expected a boolean (true or false) value,"
                                + " found this instead -> %s", condition),
                        TSLSyntaxError.causedNear(tokens)
                );
            }
            return;
        }

        // Fetch event argument
        TSLEventDefinition eventDefinition = context.getEventDefinition();
        String propertyTokenValue = predicateArguments.get(0).calculateValue(context);

        // Check if the argument is relevant
        if (!eventDefinition.acceptsProperty(propertyTokenValue)) {
            throw new TSLSyntaxError(
                    "The property '" + propertyTokenValue + "' is not accepted by " + eventDefinition.getName(),
                    TSLSyntaxError.causedNear(tokens)
            );
        }

        // Try to get comparator
        String comparatorSymbol = getComparatorSymbol(predicateArguments, context);
        TSLComparator comparator = COMPARATOR_MAP.get(comparatorSymbol.toUpperCase());

        if (comparator == null) {
            throw new TSLSyntaxError(
                    "Unknown comparator -> " + comparatorSymbol,
                    TSLSyntaxError.causedNear(predicateArguments)
            );
        }

        // Compare once to make sure nothing is wrong
        String propertyName = predicateArguments.get(0).calculateValue(context);
        Object left = context.getEventArguments().get(propertyName);
        String right = predicateArguments.get(predicateArguments.size() - 1).calculateValue(context);
        comparator.compare(left, right);
    }

    @Override
    public boolean satisfies(List<TSLToken> tokens, TSLContext context) {
        List<TSLToken> predicateArguments = tokens.subList(1, tokens.size());

        if (predicateArguments.size() == 1) {
            String condition = predicateArguments.get(0).calculateValue(context);
            return condition.equalsIgnoreCase("true");
        }

        String propertyName = predicateArguments.get(0).calculateValue(context);
        Object left = context.getEventArguments().get(propertyName);
        String right = predicateArguments.get(predicateArguments.size() - 1).calculateValue(context);

        boolean negated = isNegated(predicateArguments.subList(1, predicateArguments.size() - 1));
        TSLComparator comparator = COMPARATOR_MAP.get(getComparatorSymbol(predicateArguments, context).toUpperCase());

        try {
            boolean comparison = comparator.compare(left, right);
            return negated ? !comparison : comparison;

        } catch (TSLSyntaxError e) {
            // Should not happen in theory...
            throw new InternalError("Comparison error while comparing", e);
        }
    }

    /* --------------------------------------------------- */

    private static Map<String, TSLComparator> COMPARATOR_MAP = new HashMap<>();

    public static void registerComparator(TSLComparator comparator) throws TSLPluginError {
        if (COMPARATOR_MAP.containsKey(comparator.getSymbol())) {
            throw new TSLPluginError(comparator.getSymbol() + " already exists!");
        }

        COMPARATOR_MAP.put(comparator.getSymbol(), comparator);
    }

    static {
        try {
            registerComparator(new TSLComparatorEquals());
            registerComparator(new TSLComparatorGreaterThan());
            registerComparator(new TSLComparatorGreaterThanOrEqual());
            registerComparator(new TSLComparatorLessThan());
            registerComparator(new TSLComparatorLessThanOrEqual());
            registerComparator(new TSLComparatorInRange());
            registerComparator(new TSLComparatorPrefix());
            registerComparator(new TSLComparatorPostfix());

        } catch (TSLPluginError e) {
            // Should never happen in theory...
            throw new InternalError("Registered duplicate built-in comparator", e);
        }
    }

}
