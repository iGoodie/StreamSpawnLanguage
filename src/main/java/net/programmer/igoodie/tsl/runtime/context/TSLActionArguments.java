package net.programmer.igoodie.tsl.runtime.context;

public class TSLActionArguments extends TSLArguments {

    @Override
    public TSLActionArguments with(String property, Object value) {
        return (TSLActionArguments) super.with(property, value);
    }

}
