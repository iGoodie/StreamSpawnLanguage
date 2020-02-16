package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.runtime.context.TSLActionArguments;

public abstract class TSLActionDefinition extends TSLDefinition {

    public TSLActionDefinition(String name) {
        super(name);
    }

    /* ----------------------------------------- */

    @Override
    public abstract TSLActionArguments getSampleArguments();

}
