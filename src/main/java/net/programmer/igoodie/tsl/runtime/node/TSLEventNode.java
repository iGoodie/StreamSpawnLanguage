package net.programmer.igoodie.tsl.runtime.node;

import net.programmer.igoodie.tsl.definition.TSLEventDefinition;
import net.programmer.igoodie.tsl.runtime.context.TSLEventArguments;

public class TSLEventNode implements FlowNode {

    private TSLEventDefinition definition;

    public TSLEventNode(TSLEventDefinition definition) {
        this.definition = definition;
    }

    public TSLEventDefinition getDefinition() {
        return definition;
    }

    /* -------------------------------- */

    @Override
    public boolean process(TSLEventArguments arguments) {
        return false; // TODO
    }

    @Override
    public FlowNode chain(FlowNode next) {
        return null;
    }

}
