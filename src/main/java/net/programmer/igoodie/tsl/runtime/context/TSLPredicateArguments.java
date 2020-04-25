package net.programmer.igoodie.tsl.runtime.context;

public class TSLPredicateArguments extends TSLArguments {

    @Override
    public TSLPredicateArguments with(String property, Object value) {
        return (TSLPredicateArguments) super.with(property, value);
    }
}
