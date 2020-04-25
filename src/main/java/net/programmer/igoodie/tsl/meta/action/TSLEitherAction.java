package net.programmer.igoodie.tsl.meta.action;

import net.programmer.igoodie.tsl.definition.TSLActionDefinition;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.node.TSLActionNode;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;
import net.programmer.igoodie.tsl.util.TSLHelpers;
import net.programmer.igoodie.tsl.util.TSLRandomizer;

import java.util.List;

public class TSLEitherAction extends TSLActionDefinition {

    public TSLEitherAction() {
        super("EITHER", false);
    }

    private TSLRandomizer<List<TSLToken>> toRandomizer(List<TSLToken> actionArguments, TSLContext context) throws TSLSyntaxError {
        TSLRandomizer<List<TSLToken>> randomizer = new TSLRandomizer<>();
        List<List<TSLToken>> splittedParts = TSLHelpers.splitTokens(actionArguments, "OR");
        boolean chanceMode = false;

        for (List<TSLToken> actionTokens : splittedParts) {
            List<TSLToken> tokens = null;
            String percentageString = null;

            if (actionTokens.get(0).calculateValue(context).equalsIgnoreCase("CHANCE")) {
                chanceMode = true;

                if (actionTokens.size() < 3) {
                    throw new TSLSyntaxError(
                            "Unexpected CHANCE expression format",
                            TSLSyntaxError.causedNear(actionTokens)
                    );
                }

                percentageString = actionTokens.get(1).calculateValue(context);
                String percentKeyword = actionTokens.get(2).calculateValue(context);

                if (!percentKeyword.equalsIgnoreCase("PERCENT")) {
                    throw new TSLSyntaxError(
                            "Expected PERCENT keyword, instead found -> " + percentKeyword,
                            TSLSyntaxError.causedNear(actionTokens)
                    );
                }

                tokens = actionTokens.subList(3, actionTokens.size());

            } else if (chanceMode) {
                throw new TSLSyntaxError(
                        "Expected CHANCE expression",
                        TSLSyntaxError.causedNear(actionTokens)
                );

            } else {
                tokens = actionTokens;
            }

            if (percentageString == null) {
                randomizer.addElement(tokens, 100f / splittedParts.size());

            } else {
                randomizer.addElement(tokens, percentageString);
            }
        }

        System.out.println(randomizer.elements());

        int totalPercentage = randomizer.getTotalPercentage();
        if (chanceMode && totalPercentage != 100_00) {
            throw new TSLSyntaxError("Expected percentages to add up to 100%, found -> "
                    + (totalPercentage / 100f) + "%");
        }

        return randomizer;
    }

    @Override
    public void validateSyntax(List<TSLToken> tokens, TSLContext context) throws TSLSyntaxError {
        List<TSLToken> actionArguments = tokens.subList(1, tokens.size());

        for (List<TSLToken> actionTokens : toRandomizer(actionArguments, context).elements()) {
            TSLActionNode actionNode = TSLParser.parseAction(actionTokens, context);

            List<TSLToken> actionPart = actionNode.getDefinition().parsesNotification()
                    ? TSLLexer.actionPart(actionTokens)
                    : actionTokens;

            actionNode.getDefinition().validateSyntax(actionPart, context);
        }
    }

    @Override
    public boolean perform(List<TSLToken> tokens, TSLContext context) {
        List<TSLToken> actionArguments = tokens.subList(1, tokens.size());

        try {
            TSLRandomizer<List<TSLToken>> randomizer = toRandomizer(actionArguments, context);
            List<TSLToken> randomActionTokens = randomizer.randomItem();

            TSLActionNode actionNode = TSLParser.parseAction(randomActionTokens, context);

            List<TSLToken> actionPart = actionNode.getDefinition().parsesNotification()
                    ? TSLLexer.actionPart(randomActionTokens)
                    : randomActionTokens;

            actionNode.lexeNotification(context);
            actionNode.getDefinition().perform(actionPart, context);

        } catch (TSLSyntaxError e) {
            // Should not happen, in theory
            throw new InternalError("Failed to split tokens on perform()");
        }

        return false;
    }

}
