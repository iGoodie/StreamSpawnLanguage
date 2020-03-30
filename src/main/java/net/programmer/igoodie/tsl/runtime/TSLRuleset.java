package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.TwitchSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLEventDefinition;
import net.programmer.igoodie.tsl.exception.TSLError;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.context.TSLEventArguments;
import net.programmer.igoodie.tsl.runtime.node.TSLEventNode;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TSLRuleset {

    private String streamer;
    private Map<TSLEventDefinition, TSLEventNode> handlers;
    private Map<String, List<TSLToken>> captures;

    public TSLRuleset() {
        this(null);
    }

    public TSLRuleset(String streamer) {
        this.streamer = streamer;
        this.handlers = new HashMap<>();
        this.captures = new HashMap<>();
    }

    public void setStreamer(String streamer) {
        this.streamer = streamer;
    }

    public String getStreamer() {
        return streamer;
    }

    public Map<TSLEventDefinition, TSLEventNode> getHandlers() {
        return handlers;
    }

    /* ----------------------------------------- RULE-RELATED */

    public void addRule(TSLEventNode eventNode) {
        this.handlers.put(eventNode.getDefinition(), eventNode);
    }

    public void addCapture(String captureName, List<TSLToken> capturedTokens) throws TSLError {
        if (captures.containsKey(captureName))
            throw new TSLError(String.format("%s's ruleset already contains capture with name -> %s",
                    streamer, captureName));
    }

    public boolean hasRuleFor(TSLEventDefinition eventDefinition) {
        return this.handlers.containsKey(eventDefinition);
    }

    public int ruleCount() {
        int count = 0;
        for (TSLEventNode eventNode : handlers.values()) {
            count += eventNode.getChildren().size();
        }
        return count;
    }

    public TSLEventNode getHandler(TSLEventDefinition eventDefinition) {
        return this.handlers.get(eventDefinition);
    }

    public List<TSLToken> getCapture(String name) {
        return captures.get(name);
    }

    /* ----------------------------------------- DISPATCHERS */

    public boolean dispatch(String eventName, TSLEventArguments eventArguments) {
        TSLEventDefinition eventDefinition = TwitchSpawnLanguage.getEventDefinition(eventName);

        if (eventDefinition == null) {
            throw new IllegalArgumentException("Unknown event name -> " + eventName);
        }

        return dispatch(eventDefinition, eventArguments);

    }

    public boolean dispatch(TSLEventDefinition eventDefinition, TSLEventArguments eventArguments) {
        TwitchSpawnLanguage.LOGGER.debug("Dispatched event -> %s, args: %s",
                eventDefinition, eventArguments);

        TSLEventNode eventNode = this.handlers.get(eventDefinition);

        TSLContext context = new TSLContext();
        context.setStreamer(streamer);
//        context.setEventDefinition(eventDefinition); // <- Set by Event Node's process call
        context.setEventArguments(eventArguments);

        return eventNode.process(context);
    }

    /* ----------------------------------------- */

    @Override
    public String toString() {
        return String.format("Streamer:%s => %d rule(s)",
                streamer, ruleCount());
    }

}
