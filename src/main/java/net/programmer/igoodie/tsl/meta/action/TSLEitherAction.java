package net.programmer.igoodie.tsl.meta.action;

import net.programmer.igoodie.tsl.definition.TSLActionDefinition;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.node.TSLActionNode;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;
import net.programmer.igoodie.tsl.util.TSLHelpers;
import net.programmer.igoodie.tsl.util.TSLRandomizer;

import java.util.List;

public class TSLEitherAction extends TSLActionDefinition {

    public TSLEitherAction() {
        super("EITHER");
    }

    private TSLRandomizer<TSLActionNode> toRandomizer(List<TSLToken> actionArguments, TSLContext context) throws TSLSyntaxError {
        TSLRandomizer<TSLActionNode> randomizer = new TSLRandomizer<>();
        List<List<TSLToken>> splittedParts = TSLHelpers.splitTokens(actionArguments, "OR");
        boolean chanceMode = false;

        for (List<TSLToken> actionTokens : splittedParts) {
            TSLActionNode actionNode = null;
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

                actionNode = TSLParser.parseAction(actionTokens.subList(3, actionTokens.size()), context);

            } else if (chanceMode) {
                throw new TSLSyntaxError(
                        "Expected CHANCE expression",
                        TSLSyntaxError.causedNear(actionTokens)
                );

            } else {
                actionNode = TSLParser.parseAction(actionTokens, context);
            }

            if (percentageString == null) {
                randomizer.addElement(actionNode, 100f / splittedParts.size());

            } else {
                randomizer.addElement(actionNode, percentageString);
            }
        }

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

        for (TSLActionNode actionNode : toRandomizer(actionArguments, context).elements()) {
            actionNode.getDefinition().validateSyntax(actionArguments, context);
        }
    }

    @Override
    public boolean perform(List<TSLToken> tokens, TSLContext context) {
        List<TSLToken> actionArguments = tokens.subList(1, tokens.size());

        try {
            TSLRandomizer<TSLActionNode> randomizer = toRandomizer(actionArguments, context);
            TSLActionNode actionNode = randomizer.randomItem();
            actionNode.getDefinition().perform(actionNode.getTokens(), context);

        } catch (TSLSyntaxError e) {
            // Should not happen, in theory
            throw new InternalError("Failed to split tokens on perform()");
        }

        return false;
    }

}
