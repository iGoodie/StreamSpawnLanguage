package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.context.TSLDecorator;
import net.programmer.igoodie.tsl.runtime.node.TSLEventNode;
import net.programmer.igoodie.tsl.runtime.node.TSLFlowNode;

import java.util.List;

public class TSLRule {

    private TSLEventNode eventNode;
    private List<TSLDecorator> decorators;

    public TSLRule(TSLEventNode eventNode, List<TSLDecorator> decorators) {
        this.eventNode = eventNode;
        this.decorators = decorators;
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

    public TSLEventNode getEventNode() {
        return eventNode;
    }

    /* ------------------------------------------- */

    public void apply(TSLContext context) {
        context.setAssociatedRule(this);
        // TODO
        System.out.println("TODO: TSLRule:perform(context)");
    }

}
