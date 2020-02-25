package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;
import java.util.stream.Collectors;

public class TSLPrintAction extends TSLActionDefinition {

    public static final TSLPrintAction INSTANCE = new TSLPrintAction();

    public TSLPrintAction() {
        super("PRINT");
    }

    @Override
    public void validateSyntax(List<TSLToken> tokens, TSLContext context) throws TSLSyntaxError {
        if (tokens.size() == 0) {
            throw new TSLSyntaxError(
                    "At least one token is expected",
                    TSLSyntaxError.causedNear(tokens)
            );
        }
    }

    @Override
    public boolean perform(List<TSLToken> tokens, TSLContext context) {
        String printThis = tokens.stream()
                .map(token -> token.getValue(context))
                .collect(Collectors.joining(" "));

        System.out.println("ACTION PRINTS: " + printThis);

        return true;
    }

    @Override
    public boolean displayNotification(List<TSLToken> tokens, TSLContext context) {
        String displayThat = tokens.stream()
                .map(token -> token.getValue(context))
                .collect(Collectors.joining(" "));

        System.out.println("ACTION DISPLAYS: " + displayThat);

        return true;
    }

}
