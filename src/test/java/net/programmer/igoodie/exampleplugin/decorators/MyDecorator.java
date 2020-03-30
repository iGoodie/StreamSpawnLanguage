package net.programmer.igoodie.exampleplugin.decorators;

import net.programmer.igoodie.tsl.definition.TSLDecoratorDefinition;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;

public class MyDecorator extends TSLDecoratorDefinition {

    public MyDecorator() {
        super("my_decorator");
    }

    @Override
    public void validateArguments(String[] arguments) throws TSLSyntaxError {
        if (arguments.length != 0)
            throw new TSLSyntaxError(getName() + " does not accept any arguments.");
    }

}
