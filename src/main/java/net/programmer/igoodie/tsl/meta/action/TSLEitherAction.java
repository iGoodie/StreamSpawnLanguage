package net.programmer.igoodie.tsl.meta.action;

import net.programmer.igoodie.tsl.definition.TSLActionDefinition;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;

public class TSLEitherAction extends TSLActionDefinition {

    public TSLEitherAction() {
        super("EITHER");
    }

    @Override
    public void validateSyntax(List<TSLToken> tokens, TSLContext context) throws TSLSyntaxError {

    }

    @Override
    public boolean perform(List<TSLToken> tokens, TSLContext context) {
        return false;
    }

}
