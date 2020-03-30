package net.programmer.igoodie.tsl.runtime.context;

import net.programmer.igoodie.tsl.definition.TSLDecoratorDefinition;

import java.util.Arrays;

public class TSLDecorator {

    private TSLDecoratorDefinition definition;
    private String[] arguments;

    public TSLDecorator(TSLDecoratorDefinition definition, String[] arguments) {
        this.definition = definition;
        this.arguments = arguments;
    }

    /* ------------------------------------------ */

    public TSLDecoratorDefinition getDefinition() {
        return definition;
    }

    @Override
    public String toString() {
        return "TSLDecorator{" +
                "definition=" + definition.getName() +
                ", arguments=" + Arrays.toString(arguments) +
                '}';
    }
}
