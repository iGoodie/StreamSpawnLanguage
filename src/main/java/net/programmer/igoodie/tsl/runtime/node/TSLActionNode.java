package net.programmer.igoodie.tsl.runtime.node;

import net.programmer.igoodie.tsl.TwitchSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLActionDefinition;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;

public final class TSLActionNode extends TSLFlowNode {

    public TSLActionNode(TSLActionDefinition definition, List<TSLToken> tokens) {
        super(definition, tokens);
    }

    @Override
    public TSLActionDefinition getDefinition() {
        return (TSLActionDefinition) definition;
    }

    /* -------------------------------- */

    @Override
    public final boolean process(TSLContext context) {
        TwitchSpawnLanguage.LOGGER.trace("Processing -> %s", tokens);

        List<TSLToken> notificationPart = TSLLexer.notificationPart(tokens);
        if (notificationPart != null) {
            TwitchSpawnLanguage.LOGGER.trace("Lexed notification -> %s", notificationPart);
            context.getActionArguments().put("notification", notificationPart);
        }

        List<TSLToken> actionPart = TSLLexer.actionPart(tokens);
        TwitchSpawnLanguage.LOGGER.trace("Publishing action -> %s", actionPart);
        context.getAssociatedRule().getAssociatedRuleset().publishAction(context, this);

        return true;
    }

    @Override
    public String toString() {
        return getDefinition().getName();
    }
}
