package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.definition.TSLEventDefinition;
import net.programmer.igoodie.tsl.runtime.node.TSLEventNode;

import java.util.Map;

public class TSLRuleset {

    private String streamer;
    private Map<TSLEventDefinition, TSLEventNode> handlers;

    // TODO


    public Map<TSLEventDefinition, TSLEventNode> getHandlers() {
        return handlers;
    }
}
