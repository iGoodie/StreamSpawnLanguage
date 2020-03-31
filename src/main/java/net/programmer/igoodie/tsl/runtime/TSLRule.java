package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.context.TSLDecorator;
import net.programmer.igoodie.tsl.runtime.node.TSLActionNode;
import net.programmer.igoodie.tsl.runtime.node.TSLEventNode;
import net.programmer.igoodie.tsl.runtime.node.TSLFlowNode;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;

public class TSLRule {

    private List<TSLToken> tokens;
    private TSLEventNode eventNode;
    private List<TSLDecorator> decorators;

    public TSLRule(List<TSLToken> tokens, TSLEventNode eventNode, List<TSLDecorator> decorators) {
        this.tokens = tokens;
        this.eventNode = eventNode;
        this.decorators = decorators;
    }

    /* ------------------------------------------- */

    public List<TSLToken> getTokens() {
        return tokens;
    }

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

    public TSLActionNode getActionNode() {
        TSLFlowNode node = eventNode;

        while (node != null) {
            node = node.getNext();
            if (node instanceof TSLActionNode)
                return (TSLActionNode) node;
        }

        return null;
    }

    public int getNodeLength() {
        TSLFlowNode node = eventNode;
        int length = 1;
        while ((node = node.getNext()) != null) length++;
        return length;
    }

    /* ------------------------------------------- */

    public void process(TSLContext context) {
        context.setAssociatedRule(this);
        eventNode.process(context);
    }

    /* ------------------------------------------- */

    @Override
    public String toString() {
        return "TSLRule: Node length -> " + getNodeLength();
    }

}
