package net.programmer.igoodie.exampleplugin.actions;

import net.programmer.igoodie.tsl.definition.TSLActionDefinition;
import net.programmer.igoodie.tsl.definition.TSLPrintAction;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;
import java.util.stream.Collectors;

public class ExamplePrintAction extends TSLActionDefinition {

    public static final TSLPrintAction INSTANCE = new TSLPrintAction();

    public ExamplePrintAction() {
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
        String printThis = tokens.subList(1, tokens.size() - 1).stream()
                .map(token -> token.calculateValue(context))
                .collect(Collectors.joining(" "));

        System.out.println(">> " + printThis + "\n");

        return true;
    }

}