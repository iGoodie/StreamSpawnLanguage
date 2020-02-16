package net.programmer.igoodie.tsl.runtime.token;

import net.programmer.igoodie.tsl.runtime.context.TSLContext;

public abstract class TSLToken {

    public abstract String getRaw();

    public abstract String getValue(TSLContext context);

    @Override
    public String toString() {
        return getRaw();
    }
}
