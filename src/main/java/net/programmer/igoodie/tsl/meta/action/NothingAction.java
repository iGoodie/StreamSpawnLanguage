package net.programmer.igoodie.tsl.meta.action;

import net.programmer.igoodie.tsl.definition.TSLActionDefinition;
import net.programmer.igoodie.tsl.runtime.node.TSLActionNode;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;

public class NothingAction extends TSLActionNode {

    public NothingAction(TSLActionDefinition definition, List<TSLToken> tokens) {
        super(definition, tokens);
    }

}
