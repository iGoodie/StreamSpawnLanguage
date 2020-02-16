package net.programmer.igoodie.tsl.runtime.node;

import net.programmer.igoodie.tsl.definition.TSLDefinition;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.context.TSLEventArguments;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;

public abstract class TSLFlowNode {

    protected TSLDefinition definition;
    protected List<TSLToken> tokens;

    public TSLFlowNode(TSLDefinition definition, List<TSLToken> tokens) {
        this.definition = definition;
        this.tokens = tokens;
    }

    public TSLDefinition getDefinition() {
        return definition;
    }

    /* ---------------------------------------- */

    /**
     * Chains given node to this one
     *
     * @param next Next node
     * @return The next node if chained successfully.
     * Returns null otherwise
     */
    public TSLFlowNode chain(TSLFlowNode next) {
        throw new UnsupportedOperationException("FlowNode::chain is not meant to be used on"
                + getClass().getSimpleName());
    }

    /**
     * Processes the node. Modifies given context if necessary
     * Then passes the context to next flow node(s) to process
     *
     * @return True if successfully processed given context
     */
    public abstract boolean process(TSLContext context);

}
