package net.programmer.igoodie.tsl.runtime.node;

import net.programmer.igoodie.tsl.TwitchSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLEventDefinition;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.context.TSLEventArguments;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TSLEventNode extends TSLFlowNode {

    private List<TSLFlowNode> childNodes;

    public TSLEventNode(TSLEventDefinition definition, List<TSLToken> tokens) {
        super(definition, tokens);
        this.childNodes = new LinkedList<>();
    }

    public TSLEventDefinition getDefinition() {
        return (TSLEventDefinition) definition;
    }

    /* -------------------------------- */

    public List<TSLFlowNode> getChildren() {
        return childNodes;
    }

    public List<TSLActionNode> getActions() {
        List<TSLActionNode> actionNodes = new LinkedList<>();

        nodeLoop:
        for (TSLFlowNode childNode : childNodes) {
            TSLFlowNode node = childNode;

            while (!(node instanceof TSLActionNode)) {
                if (node instanceof TSLPredicateNode) {
                    node = ((TSLPredicateNode) node).getNextNode();
                } else {
                    continue nodeLoop;
                }
            }

            actionNodes.add((TSLActionNode) node);

        }
        return actionNodes;
    }

    /* -------------------------------- */

    @Override
    public TSLFlowNode chain(TSLFlowNode next) {
        childNodes.add(next);
        return next;
    }

    @Override
    public boolean process(TSLContext context) {
        TwitchSpawnLanguage.LOGGER.trace("Processing -> %s", tokens);

        context.setEventDefinition(this.getDefinition());

        Iterator<TSLFlowNode> nodeIterator = childNodes.iterator();
        boolean success = true;

        while (nodeIterator.hasNext()) {
            success = nodeIterator.next().process(context);
            if (success) break; // Stop if handled successfully once
        }

        return success;
    }

}
