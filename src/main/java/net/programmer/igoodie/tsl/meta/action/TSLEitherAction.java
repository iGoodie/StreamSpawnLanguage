package net.programmer.igoodie.tsl.meta.action;

import net.programmer.igoodie.tsl.definition.TSLActionDefinition;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.node.TSLActionNode;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;
import net.programmer.igoodie.tsl.util.TSLHelpers;

import java.util.List;

public class TSLEitherAction extends TSLActionDefinition {

    public TSLEitherAction() {
        super("EITHER");
    }

    @Override
    public void validateSyntax(List<TSLToken> tokens, TSLContext context) throws TSLSyntaxError {
        List<TSLToken> actionArguments = tokens.subList(1, tokens.size());

        for (List<TSLToken> actionTokens : TSLHelpers.splitTokens(actionArguments, "OR")) {
            TSLActionNode actionNode = TSLParser.parseAction(actionTokens, context);
            actionNode.getDefinition().validateSyntax(actionTokens, context);
        }
    }

    @Override
    public boolean perform(List<TSLToken> tokens, TSLContext context) {
        List<TSLToken> actionArguments = tokens.subList(1, tokens.size());

        try {
            List<List<TSLToken>> actionTokensList = TSLHelpers.splitTokens(actionArguments, "OR");

            // TODO: Select random, parse it and perform it

        } catch (TSLSyntaxError e) {
            // Should not happen, in theory
            throw new InternalError("Failed to split tokens on perform()");
        }

        return false;
    }

}
