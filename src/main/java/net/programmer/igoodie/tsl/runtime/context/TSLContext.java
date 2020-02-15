package net.programmer.igoodie.tsl.runtime.context;

public class TSLContext {

    private TSLEventArguments eventArguments;
    private TSLActionArguments actionArguments;

    public TSLContext() {}

    public void setEventArguments(TSLEventArguments eventArguments) {
        this.eventArguments = eventArguments;
    }

    public void setActionArguments(TSLActionArguments actionArguments) {
        this.actionArguments = actionArguments;
    }

    public TSLEventArguments getEventArguments() {
        return eventArguments;
    }

    public TSLActionArguments getActionArguments() {
        return actionArguments;
    }

}
