package net.programmer.igoodie.tsl.runtime.node;

import net.programmer.igoodie.tsl.TwitchSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLEventDefinition;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.context.TSLEventArguments;
import net.programmer.igoodie.tsl.runtime.context.TSLExpressionBindings;
import net.programmer.igoodie.tsl.runtime.token.TSLExpression;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class TSLEventNode extends TSLFlowNode {

    public TSLEventNode(TSLEventDefinition definition, List<TSLToken> tokens) {
        super(definition, tokens);
    }

    public TSLEventDefinition getDefinition() {
        return (TSLEventDefinition) definition;
    }

    /* -------------------------------- */

    @Override
    public TSLFlowNode chain(TSLFlowNode next) {
        this.next = next;
        return next;
    }

    @Override
    public boolean process(TSLContext context) {
        TwitchSpawnLanguage.LOGGER.trace("Processing -> %s", tokens);

        TSLExpressionBindings.updateBinding(TSLExpression.BINDINGS);

        context.setEventDefinition(this.getDefinition());

        return next.process(context);
    }

}
