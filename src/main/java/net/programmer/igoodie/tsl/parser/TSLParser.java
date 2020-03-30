package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.TwitchSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLActionDefinition;
import net.programmer.igoodie.tsl.definition.TSLDecoratorDefinition;
import net.programmer.igoodie.tsl.definition.TSLEventDefinition;
import net.programmer.igoodie.tsl.definition.TSLPredicateDefinition;
import net.programmer.igoodie.tsl.exception.TSLParsingError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.context.TSLDecorator;
import net.programmer.igoodie.tsl.runtime.context.TSLExpressionBindings;
import net.programmer.igoodie.tsl.runtime.node.TSLActionNode;
import net.programmer.igoodie.tsl.runtime.node.TSLEventNode;
import net.programmer.igoodie.tsl.runtime.node.TSLFlowNode;
import net.programmer.igoodie.tsl.runtime.node.TSLPredicateNode;
import net.programmer.igoodie.tsl.runtime.token.TSLExpression;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TSLParser {

    private static Pattern DECORATOR_PATTERN = Pattern.compile("@((?<name>[^\\s\\(\\)]*)\\((?<args>.*)\\)|([^\\s\\(\\)]*))");

    private String tsl;
    private TSLRuleset ruleset;

    public TSLParser(String tsl) {
        this.ruleset = new TSLRuleset();
        this.tsl = tsl;
    }

    public TSLRuleset parse() throws TSLParsingError {
        TwitchSpawnLanguage.LOGGER.trace("Parsing tsl script -> %s", tsl);

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

        TwitchSpawnLanguage.LOGGER.debug("Parsed ruleset with %d rule(s)", tokenizer.ruleCount());

        return ruleset;
    }

    private TSLEventNode parseRule(List<TSLToken> tokens) throws TSLSyntaxError {
        TwitchSpawnLanguage.LOGGER.debug("Parsing rule -> %s", tokens);

        // Lexe tokens into parts
        TSLLexer lexer = new TSLLexer(tokens);
        lexer.intoParts();

        // Create an empty context. It'll be filled as nodes are parsed
        TSLContext validationContext = new TSLContext();
        validationContext.setStreamer("TestGuy123");
        TSLExpressionBindings.updateBinding(TSLExpression.BINDINGS);

        // Parse nodes
        TSLEventNode eventNode = parseEvent(lexer.getEventTokens(), validationContext);
        TSLExpressionBindings.updateBinding(TSLExpression.BINDINGS);
        TwitchSpawnLanguage.LOGGER.debug("Parsed event -> %s", eventNode.getDefinition().getName());

        List<TSLPredicateNode> predicateNodes = new LinkedList<>();
        for (List<TSLToken> predicateTokenList : lexer.getPredicateTokens()) {
            TSLPredicateNode predicateNode = parsePredicate(predicateTokenList, validationContext);
            predicateNodes.add(predicateNode);
            TSLExpressionBindings.updateBinding(TSLExpression.BINDINGS);
            TwitchSpawnLanguage.LOGGER.debug("Parsed predicate -> %s", predicateNode.getDefinition().getName());
        }

        TSLActionNode actionNode = parseAction(lexer.getActionTokens(), validationContext);
        TSLExpressionBindings.updateBinding(TSLExpression.BINDINGS);
        TwitchSpawnLanguage.LOGGER.debug("Parsed action -> %s", actionNode.getDefinition().getName());

        // Chain all and return
        chainAll(eventNode, predicateNodes, actionNode);
        return eventNode;
    }

    /* ---------------------------- */

    public static TSLDecorator parseDecorator(TSLToken decoratorToken) throws TSLSyntaxError {
        TwitchSpawnLanguage.LOGGER.trace("Parsing decorator with token -> %s", decoratorToken);

        Matcher matcher = DECORATOR_PATTERN.matcher(decoratorToken.getRaw());

        if (!matcher.matches()) {
            throw new TSLSyntaxError(
                    "Invalid decorator pattern.",
                    decoratorToken.getRaw()
            );
        }

        String name = matcher.group("args") != null
                ? matcher.group("name")
                : matcher.group(1);

        TSLDecoratorDefinition definition = TwitchSpawnLanguage.getDecoratorDefinition(name);

        if (definition == null) {
            throw new TSLSyntaxError(
                    String.format("Unknown decorator name -> %s", name),
                    decoratorToken.getRaw()
            );
        }

        String[] args = matcher.group("args") != null
                ? matcher.group("args").split(",\\s*")
                : new String[0];

        definition.validateArguments(args);
        return new TSLDecorator(definition, args);
    }

    public static TSLEventNode parseEvent(List<TSLToken> eventTokens,
                                          TSLContext validationContext) throws TSLSyntaxError {
        TwitchSpawnLanguage.LOGGER.trace("Parsing event with tokens -> %s", eventTokens);

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

        for (TSLToken eventToken : eventTokens) {
            if (!eventToken.validate(validationContext)) {
                throw new TSLSyntaxError(
                        "Invalid token -> " + eventToken.getRaw(),
                        TSLSyntaxError.causedNear(eventTokens)
                );
            }
        }

        eventDefinition.validateSyntax(eventTokens, validationContext);

        validationContext.setEventDefinition(eventDefinition);
        validationContext.setEventArguments(eventDefinition.getSampleArguments());
        return new TSLEventNode(eventDefinition, eventTokens);
    }

    public static TSLPredicateNode parsePredicate(List<TSLToken> predicateTokens,
                                                  TSLContext validationContext) throws TSLSyntaxError {
        TwitchSpawnLanguage.LOGGER.trace("Parsing predicate with tokens -> %s", predicateTokens);

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

        for (TSLToken predicateToken : predicateTokens) {
            if (!predicateToken.validate(validationContext)) {
                throw new TSLSyntaxError(
                        "Invalid token -> " + predicateToken.getRaw(),
                        TSLSyntaxError.causedNear(predicateTokens)
                );
            }
        }

        predicateDefinition.validateSyntax(predicateTokens, validationContext);

        validationContext.addPredicateDefinition(predicateDefinition);
        return new TSLPredicateNode(predicateDefinition, predicateTokens);
    }

    public static TSLActionNode parseAction(List<TSLToken> actionTokens,
                                            TSLContext validationContext) throws TSLSyntaxError {
        TwitchSpawnLanguage.LOGGER.trace("Parsing action with tokens -> %s", actionTokens);

        TSLToken actionName = actionTokens.get(0);

        if (!actionName.isPlain()) {
            throw new TSLSyntaxError(
                    "Expected action name to be a plain token",
                    TSLSyntaxError.causedNear(actionTokens)
            );
        }

        TSLActionDefinition actionDefinition = TwitchSpawnLanguage.getActionDefinition(actionName.getRaw());

        if (actionDefinition == null) {
            throw new TSLSyntaxError(
                    "Unexpected action name: " + actionName,
                    TSLSyntaxError.causedNear(actionTokens)
            );
        }

        for (TSLToken actionToken : actionTokens) {
            if (!actionToken.validate(validationContext)) {
                throw new TSLSyntaxError(
                        "Invalid token -> " + actionToken.getRaw(),
                        TSLSyntaxError.causedNear(actionTokens)
                );
            }
        }

        actionDefinition.validateSyntax(actionTokens, validationContext);

        return new TSLActionNode(actionDefinition, actionTokens);
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
