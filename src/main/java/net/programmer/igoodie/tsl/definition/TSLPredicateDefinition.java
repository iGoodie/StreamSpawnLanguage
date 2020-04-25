package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.runtime.context.TSLPredicateArguments;

public abstract class TSLPredicateDefinition extends TSLDefinition {

    public TSLPredicateDefinition(String name) {
        super(name);
    }

    /* ----------------------------------------- */

    @Override
    public TSLPredicateArguments getShape() {
        return new TSLPredicateArguments(); // By default, no argument is serialized
    }

}
