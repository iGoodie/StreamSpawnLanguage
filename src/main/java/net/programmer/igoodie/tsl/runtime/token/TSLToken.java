package net.programmer.igoodie.tsl.runtime.token;

import net.programmer.igoodie.tsl.runtime.context.TSLContext;

public abstract class TSLToken {

    public abstract String getValue(TSLContext context);

}
