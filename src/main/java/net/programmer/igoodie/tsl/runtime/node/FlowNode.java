package net.programmer.igoodie.tsl.runtime.node;

import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.context.TSLEventArguments;

public interface FlowNode {

    /**
     * Chains given node to this one
     *
     * @param next Next node
     * @return The next node if chained successfully.
     * Returns null otherwise
     */
    default FlowNode chain(FlowNode next, TSLContext context) {
        throw new UnsupportedOperationException("FlowNode::chain is not meant to be used on"
                + getClass().getSimpleName());
    }

    /**
     * Processes given event arguments.
     * Passes them to next flow node(s) if necessary
     *
     * @return True if successfully processed given args
     */
    boolean process(TSLEventArguments arguments);

}
