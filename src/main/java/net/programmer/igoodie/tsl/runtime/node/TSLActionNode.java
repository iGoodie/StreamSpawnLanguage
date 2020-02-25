package net.programmer.igoodie.tsl.runtime.node;

import net.programmer.igoodie.tsl.TwitchSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLActionDefinition;
import net.programmer.igoodie.tsl.definition.TSLDefinition;
import net.programmer.igoodie.tsl.runtime.context.TSLActionArguments;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.context.TSLEventArguments;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;

public abstract class TSLActionNode extends TSLFlowNode {

    public TSLActionNode(TSLActionDefinition definition, List<TSLToken> tokens) {
        super(definition, tokens);
    }

    @Override
    public TSLActionDefinition getDefinition() {
        return (TSLActionDefinition) definition;
    }

    /* -------------------------------- */

    @Override
    public final TSLFlowNode chain(TSLFlowNode next) {
        return super.chain(next);
    }

    @Override
    public final boolean process(TSLContext context) {
        TwitchSpawnLanguage.LOGGER.trace("Processing -> %s", tokens);
        return true; // TODO
    }

}
