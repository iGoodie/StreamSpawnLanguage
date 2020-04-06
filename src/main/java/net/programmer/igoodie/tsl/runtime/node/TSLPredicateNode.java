package net.programmer.igoodie.tsl.runtime.node;

import net.programmer.igoodie.tsl.StreamSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLPredicateDefinition;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;

public final class TSLPredicateNode extends TSLFlowNode {

    public TSLPredicateNode(TSLPredicateDefinition definition, List<TSLToken> tokens) {
        super(definition, tokens);
    }

    @Override
    public TSLPredicateDefinition getDefinition() {
        return (TSLPredicateDefinition) definition;
    }

    /* -------------------------------- */

    @Override
    public TSLFlowNode chain(TSLFlowNode next) {
        this.next = next;
        return next;
    }

    @Override
    public boolean process(TSLContext context) {
        StreamSpawnLanguage.LOGGER.trace("Processing -> %s", tokens);

        TSLPredicateDefinition definition = this.getDefinition();

        context.addPredicateDefinition(definition);

        if (definition.satisfies(tokens, context)) {
            return next.process(context);
        }

        return false;
    }

}
