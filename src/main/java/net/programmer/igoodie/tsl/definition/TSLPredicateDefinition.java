package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.runtime.context.TSLArguments;

public abstract class TSLPredicateDefinition extends TSLDefinition {

    public TSLPredicateDefinition(String name) {
        super(name);
    }

    /* ----------------------------------------- */

    @Override
    public TSLArguments getSampleArguments() {
        return new TSLArguments(); // By default, no argument is serialized
    }

}
