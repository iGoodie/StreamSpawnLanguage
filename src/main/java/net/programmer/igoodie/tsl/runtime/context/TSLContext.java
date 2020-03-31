package net.programmer.igoodie.tsl.runtime.context;

import net.programmer.igoodie.tsl.definition.TSLActionDefinition;
import net.programmer.igoodie.tsl.definition.TSLEventDefinition;
import net.programmer.igoodie.tsl.definition.TSLPredicateDefinition;
import net.programmer.igoodie.tsl.runtime.TSLRule;

import java.util.LinkedList;
import java.util.List;

public class TSLContext {

    private String streamer;

    private TSLEventDefinition eventDefinition;
    private List<TSLPredicateDefinition> predicateDefinitions;
    private TSLActionDefinition actionDefinition;

    private TSLEventArguments eventArguments;
    private TSLActionArguments actionArguments;
    private TSLRule associatedRule;

    public TSLContext() {
        this.predicateDefinitions = new LinkedList<>();
        this.actionArguments = new TSLActionArguments();
    }

    /* ----------------------------------------- SETTERS */

    public void setStreamer(String streamer) {
        this.streamer = streamer;
    }

    public void setAssociatedRule(TSLRule associatedRule) {
        this.associatedRule = associatedRule;
    }

    public void setEventDefinition(TSLEventDefinition eventDefinition) {
        this.eventDefinition = eventDefinition;
    }

    public void addPredicateDefinition(TSLPredicateDefinition predicateDefinition) {
        this.predicateDefinitions.add(predicateDefinition);
    }

    public void setActionDefinition(TSLActionDefinition actionDefinition) {
        this.actionDefinition = actionDefinition;
    }

    public void setEventArguments(TSLEventArguments eventArguments) {
        this.eventArguments = eventArguments;
    }

    public void setActionArguments(TSLActionArguments actionArguments) {
        this.actionArguments = actionArguments;
    }

    /* ----------------------------------------- GETTERS */

    public String getStreamer() {
        return streamer;
    }

    public TSLRule getAssociatedRule() {
        return associatedRule;
    }

    public TSLEventDefinition getEventDefinition() {
        return eventDefinition;
    }

    public List<TSLPredicateDefinition> getPredicateDefinitions() {
        return predicateDefinitions;
    }

    public TSLActionDefinition getActionDefinition() {
        return actionDefinition;
    }

    public TSLEventArguments getEventArguments() {
        return eventArguments;
    }

    public TSLActionArguments getActionArguments() {
        return actionArguments;
    }

}
