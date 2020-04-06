package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.StreamSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLEventDefinition;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.context.TSLEventArguments;
import net.programmer.igoodie.tsl.runtime.pubsub.TSLPublisher;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.*;

public class TSLRuleset extends TSLPublisher {

    private String streamer;
    private List<TSLRule> rules;
    private Map<String, List<TSLToken>> captures;

    public TSLRuleset() {
        this(null);
    }

    public TSLRuleset(String streamer) {
        this.streamer = streamer;
        this.rules = new ArrayList<>();
        this.captures = new HashMap<>();
    }

    public void setStreamer(String streamer) {
        this.streamer = streamer;
    }

    public String getStreamer() {
        return streamer;
    }

    public List<TSLRule> getRules() {
        return rules;
    }

    /* ----------------------------------------- RULE-RELATED */

    public void addRule(TSLRule rule) {
        rule.setAssociatedRuleset(this);
        this.rules.add(rule);
    }

    public void addCapture(String captureName, List<TSLToken> capturedTokens) throws TSLSyntaxError {
        if (captures.containsKey(captureName))
            throw new TSLSyntaxError(
                    String.format("%s's ruleset already contains capture with name -> %s",
                            streamer, captureName));

        this.captures.put(captureName, capturedTokens);
    }

    public boolean hasRuleFor(TSLEventDefinition eventDefinition) {
        return this.rules.stream()
                .map(TSLRule::getEventNode)
                .anyMatch(event -> event.getDefinition() == eventDefinition);
    }

    public int ruleCount() {
        return this.rules.size();
    }

    public List<TSLToken> getCapture(String name) {
        return captures.get(name);
    }

    public List<TSLToken> replaceCaptures(List<TSLToken> tokens) throws TSLSyntaxError {
        List<TSLToken> replaced = new LinkedList<>();

        for (int i = 0; i < tokens.size(); i++) {
            TSLToken token = tokens.get(i);

            if (token.isCaptureName()) {
                String captureName = token.getRaw().substring(1);
                List<TSLToken> capturedSnippet = this.getCapture(captureName);

                if (capturedSnippet == null) {
                    throw new TSLSyntaxError(
                            "Capture " + captureName + " is not defined.",
                            TSLSyntaxError.causedNear(tokens, i)
                    );
                }

                replaced.addAll(capturedSnippet);

            } else {
                replaced.add(token);
            }
        }

        return replaced;
    }

    /* ----------------------------------------- DISPATCHERS */

    public void dispatch(String eventName, TSLEventArguments eventArguments) {
        TSLEventDefinition eventDefinition = StreamSpawnLanguage.getEventDefinition(eventName);

        if (eventDefinition == null) {
            throw new IllegalArgumentException("Unknown event name -> " + eventName);
        }

        dispatch(eventDefinition, eventArguments);

    }

    public void dispatch(TSLEventDefinition eventDefinition, TSLEventArguments eventArguments) {
        StreamSpawnLanguage.LOGGER.debug("Dispatched event -> %s, args: %s",
                eventDefinition, eventArguments);

        for (TSLRule rule : rules) {
            TSLContext context = new TSLContext();
            context.setStreamer(streamer);
            context.setAssociatedRule(rule);
            context.setEventArguments(eventArguments);
            rule.process(context);
        }
    }

    /* ----------------------------------------- */

    @Override
    public String toString() {
        return String.format("Streamer:%s => %d rule(s). %d capture(s)",
                streamer, this.ruleCount(), this.captures.size());
    }

}
