package net.programmer.igoodie.tsl.runtime.pubsub;

import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.node.TSLActionNode;

public abstract class TSLListener {

    public abstract void onAction(TSLContext context, TSLActionNode action);

}
