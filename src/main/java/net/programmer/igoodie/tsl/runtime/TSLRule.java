package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.context.TSLDecorator;
import net.programmer.igoodie.tsl.runtime.node.TSLFlowNode;

import java.util.List;

public class TSLRule {

    private TSLFlowNode headFlowNode;
    private List<TSLDecorator> decorators;

    public void perform(String streamer, TSLContext context) {
        // TODO
        System.out.println("TODO: TSLRule:perform(streamer, context)");
    }

}
