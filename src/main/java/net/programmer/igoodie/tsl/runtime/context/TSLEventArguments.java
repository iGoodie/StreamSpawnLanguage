package net.programmer.igoodie.tsl.runtime.context;

public class TSLEventArguments extends TSLArguments {

    @Override
    public TSLEventArguments with(String property, Object value) {
        return (TSLEventArguments) super.with(property, value);
    }

}
