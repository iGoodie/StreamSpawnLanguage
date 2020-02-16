package net.programmer.igoodie.tsl.runtime.node;

import net.programmer.igoodie.tsl.definition.TSLPredicateDefinition;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;

public class TSLPredicateNode extends TSLFlowNode {

    private TSLFlowNode nextNode;

    public TSLPredicateNode(TSLPredicateDefinition definition, List<TSLToken> tokens) {
        super(definition, tokens);
    }

    @Override
    public TSLPredicateDefinition getDefinition() {
        return (TSLPredicateDefinition) definition;
    }

    /* -------------------------------- */

    public TSLFlowNode getNextNode() {
        return nextNode;
    }

    /* -------------------------------- */

    @Override
    public TSLFlowNode chain(TSLFlowNode next) {
        this.nextNode = next;
        return next;
    }

    @Override
    public boolean process(TSLContext context) {
        TSLPredicateDefinition definition = this.getDefinition();

        context.addPredicateDefinition(definition);

        if (definition.satisfies(tokens, context)) {
            return nextNode.process(context);
        }

        return false;
    }

}
