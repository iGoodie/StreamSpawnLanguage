package net.programmer.igoodie.tsl.runtime.node;

import net.programmer.igoodie.tsl.runtime.context.TSLActionArguments;
import net.programmer.igoodie.tsl.runtime.context.TSLEventArguments;

public abstract class TSLActionNode implements FlowNode {

    @Override
    public FlowNode chain(FlowNode next) {
        return null;
    }

    @Override
    public boolean process(TSLEventArguments arguments) {
        return false;
    }

    public TSLActionArguments getActionArguments() {
        return new TSLActionArguments();
    }

}
