package net.programmer.igoodie.tsl.runtime.token;

import net.programmer.igoodie.tsl.runtime.context.TSLContext;

public class TSLTokenPlain extends TSLToken {

    private String token;

    public TSLTokenPlain(String token) {
        this.token = token;
    }

    @Override
    public String getValue(TSLContext context) {
        return token;
    }

}
