package net.programmer.igoodie.tsl.meta.action;

import net.programmer.igoodie.tsl.definition.TSLActionDefinition;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;

public class TSLNothingAction extends TSLActionDefinition {

    public TSLNothingAction() {
        super("NOTHING");
    }

    @Override
    public void validateSyntax(List<TSLToken> tokens, TSLContext context) throws TSLSyntaxError {
        if (tokens.size() != 1) {
            throw new TSLSyntaxError(
                    "NOTHING action does not accept any parameters"
            );
        }
    }

    @Override
    public boolean perform(List<TSLToken> tokens, TSLContext context) {
        return true;
    }

}
