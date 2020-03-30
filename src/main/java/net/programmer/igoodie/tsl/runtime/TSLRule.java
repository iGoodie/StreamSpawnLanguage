package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.context.TSLDecorator;
import net.programmer.igoodie.tsl.runtime.node.TSLFlowNode;

import java.util.List;

public class TSLRule {

    private TSLFlowNode headFlowNode;
    private List<TSLDecorator> decorators;

    public TSLRule(TSLFlowNode headFlowNode, List<TSLDecorator> decorators) {

    }

    /* ------------------------------------------- */

    public boolean hasDecorator(String name) {
        return getDecorator(name) != null;
    }

    public TSLDecorator getDecorator(String name) {
        return decorators.stream()
                .filter(decorator -> decorator.getDefinition().getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /* ------------------------------------------- */

    public void perform(TSLContext context) {
        context.setAssociatedRule(this);
        // TODO
        System.out.println("TODO: TSLRule:perform(context)");
    }

}
