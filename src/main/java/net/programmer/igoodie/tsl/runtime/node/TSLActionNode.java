package net.programmer.igoodie.tsl.runtime.node;

import net.programmer.igoodie.tsl.TwitchSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLActionDefinition;
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
        definition.satisfies(tokens, context);
        System.out.println("Action Arguments: " + context.getActionArguments());
        System.out.println("Event Arguments: " + context.getEventArguments());
        return true;
//        return definition.satisfies(tokens, context);
    }

}
