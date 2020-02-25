package net.programmer.igoodie.exampleplugin.events;

import net.programmer.igoodie.tsl.definition.TSLEventDefinition;
import net.programmer.igoodie.tsl.runtime.context.TSLEventArguments;

public class ExampleEvent extends TSLEventDefinition {

    public ExampleEvent() {
        super("Example Event");
    }

    @Override
    public TSLEventArguments getSampleArguments() {
        return new TSLEventArguments()
                .with("numberArgument1", 1)
                .with("numberArgument2", 2);
    }

}
