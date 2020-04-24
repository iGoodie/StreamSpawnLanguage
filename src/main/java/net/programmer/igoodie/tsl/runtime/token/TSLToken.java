package net.programmer.igoodie.tsl.runtime.token;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLTokenizer;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;

public abstract class TSLToken {

    public abstract boolean validate(TSLContext context) throws TSLSyntaxError;

    public abstract String getRaw();

    public abstract String getValue(TSLContext context);

    public boolean isPlain() {
        return this.getClass() == TSLPlainToken.class;
    }

    public boolean isGroup() {
        return this.getClass() == TSLTokenGroup.class;
    }

    public boolean isExpression() {
        return this.getClass() == TSLExpression.class;
    }

    public boolean isCaptureDeclaration() {
        return this.getClass() == TSLPlainToken.class
                && this.getRaw().startsWith(TSLTokenizer.CAPTURE_DECLARE);
    }

    public boolean isCaptureFetch() {
        return this.getClass() == TSLPlainToken.class
                && this.getRaw().startsWith(String.valueOf(TSLTokenizer.CAPTURE_FETCH));
    }

    public boolean isDecorator() {
        return getRaw().startsWith(String.valueOf(TSLTokenizer.DECORATOR));
    }

    public String getType() {
        if (isPlain()) {
            return "plain token";
        }

        if (isGroup()) {
            return "token group";
        }

        if (isExpression()) {
            return "expression";
        }

        return "unknown";
    }

    @Override
    public String toString() {
        return getRaw();
    }

}
