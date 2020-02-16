package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.runtime.context.TSLArguments;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;
import java.util.Objects;

public abstract class TSLPredicateDefinition extends TSLDefinition {

    public TSLPredicateDefinition(String name) {
        super(name);
    }

    /* ----------------------------------------- */

    @Override
    public abstract TSLArguments getSampleArguments(); // TODO: TSLPredicateArguments

}
