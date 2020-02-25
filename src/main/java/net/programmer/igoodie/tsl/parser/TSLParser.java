package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.TwitchSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLEventDefinition;
import net.programmer.igoodie.tsl.definition.TSLPredicateDefinition;
import net.programmer.igoodie.tsl.exception.TSLParsingError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.node.TSLActionNode;
import net.programmer.igoodie.tsl.runtime.node.TSLEventNode;
import net.programmer.igoodie.tsl.runtime.node.TSLFlowNode;
import net.programmer.igoodie.tsl.runtime.node.TSLPredicateNode;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TSLParser {

    private String tsl;
    private TSLRuleset ruleset;

    public TSLParser(String tsl) {
        this.ruleset = new TSLRuleset();
        this.tsl = tsl;
    }

    public TSLRuleset parse() throws TSLParsingError {
        TSLTokenizer tokenizer = new TSLTokenizer(tsl);
        List<TSLSyntaxError> syntaxErrors = new LinkedList<>();

        // Unavailable to tokenize into individual rules
        try { tokenizer.intoRules(); } catch (TSLSyntaxError syntaxError) {
            throw new TSLParsingError(syntaxError);
        }

        // Go for each rule, accumulating possible syntax errors
        for (int i = 0; i < tokenizer.ruleCount(); i++) {
            try {
                List<TSLToken> tokens = tokenizer.intoTokens(i);
                TSLEventNode eventNode = parseRule(tokens);
                ruleset.addRule(eventNode);

            } catch (TSLSyntaxError syntaxError) {
                syntaxErrors.add(syntaxError);
            }
        }

        if (!syntaxErrors.isEmpty()) {
            throw new TSLParsingError(syntaxErrors);
        }

        return ruleset;
    }

    private TSLEventNode parseRule(List<TSLToken> tokens) throws TSLSyntaxError {
        TSLLexer lexer = new TSLLexer(tokens);
        lexer.intoParts();

        TSLContext validationContext = new TSLContext();

        TSLEventNode eventNode = parseEvent(lexer.getEventTokens(), validationContext);

        List<TSLPredicateNode> predicateNodes = new LinkedList<>();
        for (List<TSLToken> predicateTokenList : lexer.getPredicateTokens()) {
            predicateNodes.add(parsePredicate(predicateTokenList, validationContext));
        }

        // TODO: Parse action

        chainAll(eventNode, predicateNodes, null); // TODO: <- Replace null with action node!

        return eventNode;
    }

    /* ---------------------------- */

    public static TSLEventNode parseEvent(List<TSLToken> eventTokens,
                                          TSLContext validationContext) throws TSLSyntaxError {
        if (!eventTokens.stream().allMatch(TSLToken::isPlain)) {
            throw new TSLSyntaxError(
                    "Expected all event tokens to be a plain token.",
                    TSLSyntaxError.causedNear(eventTokens)
            );
        }

        String eventName = eventTokens.stream()
                .map(token -> token.getValue(validationContext))
                .collect(Collectors.joining(" "));

        TSLEventDefinition eventDefinition = TwitchSpawnLanguage.getEventDefinition(eventName);

        if (eventDefinition == null) {
            throw new TSLSyntaxError(
                    "Unexpected event name: " + eventName,
                    TSLSyntaxError.causedNear(eventTokens)
            );
        }

        eventDefinition.validate(eventTokens, validationContext);

        validationContext.setEventDefinition(eventDefinition);
        validationContext.setEventArguments(eventDefinition.getSampleArguments());
        return new TSLEventNode(eventDefinition, eventTokens);
    }

    public static TSLPredicateNode parsePredicate(List<TSLToken> predicateTokens,
                                                  TSLContext validationContext) throws TSLSyntaxError {
        if (predicateTokens.size() < 1) {
            throw new TSLSyntaxError("Unexpected count of tokens");
        }

        TSLToken predicateName = predicateTokens.get(0);

        if (!predicateName.isPlain()) {
            throw new TSLSyntaxError(
                    "Expected predicate name to be a plain token",
                    TSLSyntaxError.causedNear(predicateTokens)
            );
        }

        TSLPredicateDefinition predicateDefinition = TwitchSpawnLanguage.getPredicateDefinition(predicateName.getRaw());

        if (predicateDefinition == null) {
            throw new TSLSyntaxError(
                    "Unexpected predicate begin: " + predicateName,
                    TSLSyntaxError.causedNear(predicateTokens)
            );
        }

        predicateDefinition.validate(predicateTokens, validationContext);

        validationContext.addPredicateDefinition(predicateDefinition);
        return new TSLPredicateNode(predicateDefinition, predicateTokens);
    }

    private static void chainAll(TSLEventNode eventNode,
                                 List<TSLPredicateNode> predicateNodes,
                                 TSLActionNode actionNode) {
        TSLFlowNode currentNode = eventNode;

        for (TSLPredicateNode predicateNode : predicateNodes) {
            currentNode = currentNode.chain(predicateNode);
        }

        currentNode.chain(actionNode);
    }

}
