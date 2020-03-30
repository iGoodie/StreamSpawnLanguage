package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;

public abstract class TSLDecoratorDefinition {

    private String name;

    public TSLDecoratorDefinition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void validateArguments(String[] arguments) throws TSLSyntaxError;

}
