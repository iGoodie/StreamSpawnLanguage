package net.programmer.igoodie.tsl.meta.predicate;

import net.programmer.igoodie.tsl.definition.TSLPredicateDefinition;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;

public class TSLWithPredicate extends TSLPredicateDefinition {

    public TSLWithPredicate() {
        super("WITH");
    }

    @Override
    public boolean satisfied(List<TSLToken> tokens, TSLContext context) {
        return false; // TODO
    }

}
