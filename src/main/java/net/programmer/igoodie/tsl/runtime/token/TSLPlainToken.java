package net.programmer.igoodie.tsl.runtime.token;

import net.programmer.igoodie.tsl.runtime.context.TSLContext;

public class TSLPlainToken extends TSLToken {

    private String token;

    public TSLPlainToken(String token) {
        this.token = token;
    }

    @Override
    public boolean validate(TSLContext context) {
        // Good guy plain token;
        return true; // A plain token is always a valid token
    }

    @Override
    public String getRaw() {
        return token;
    }

    @Override
    public String getValue(TSLContext context) {
        return token;
    }

}
