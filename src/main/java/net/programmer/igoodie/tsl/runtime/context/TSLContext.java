package net.programmer.igoodie.tsl.runtime.context;

import net.programmer.igoodie.tsl.definition.TSLActionDefinition;
import net.programmer.igoodie.tsl.definition.TSLEventDefinition;
import net.programmer.igoodie.tsl.definition.TSLPredicateDefinition;

import java.util.LinkedList;
import java.util.List;

public class TSLContext {

    private String streamer;

    private TSLEventDefinition eventDefinition;
    private List<TSLPredicateDefinition> predicateDefinitions;
    private TSLActionDefinition actionDefinition;

    private TSLEventArguments eventArguments;
    private TSLActionArguments actionArguments;

    public TSLContext() {
        this.predicateDefinitions = new LinkedList<>();
    }

    /* ----------------------------------------- SETTERS */

    public void setStreamer(String streamer) {
        this.streamer = streamer;
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
