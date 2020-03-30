package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;

public abstract class TSLDecoratorDefinition extends TSLDefinition {

    public TSLDecoratorDefinition(String name) {
        super(name);
    }

    @Override
    public final void validateSyntax(List<TSLToken> tokens, TSLContext context) throws TSLSyntaxError {
        // TODO
    }

    public abstract void validateParameters(List<String> tokens);

    @Override
    public boolean satisfies(List<TSLToken> tokens, TSLContext context) {
        return false;
    }

}
