package net.programmer.igoodie.tsl.meta.action;

import net.programmer.igoodie.tsl.runtime.context.TSLActionArguments;
import net.programmer.igoodie.tsl.runtime.context.TSLEventArguments;
import net.programmer.igoodie.tsl.runtime.node.TSLActionNode;

public class NothingAction extends TSLActionNode {

    @Override
    public boolean process(TSLEventArguments arguments) {
        return super.process(arguments);
    }

    @Override
    public TSLActionArguments getActionArguments() {
        return null;
    }

}
