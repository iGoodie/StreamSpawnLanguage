package net.programmer.igoodie.tsl.runtime.pubsub;

import net.programmer.igoodie.tsl.TwitchSpawnLanguage;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.node.TSLActionNode;

import java.util.LinkedList;
import java.util.List;

public class TSLPublisher {

    private List<TSLListener> listeners;

    public TSLPublisher() {
        this.listeners = new LinkedList<>();
    }

    public void publishAction(TSLContext context, TSLActionNode action) {
        for (TSLListener listener : listeners) {
        TwitchSpawnLanguage.LOGGER.trace("Publishing action to -> %s", listener);
            listener.onAction(context, action);
        }
    }

    public void subscribe(TSLListener subscriber) {
        this.listeners.add(subscriber);
    }

}
