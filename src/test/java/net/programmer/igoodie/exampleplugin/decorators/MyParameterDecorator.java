package net.programmer.igoodie.exampleplugin.decorators;

import net.programmer.igoodie.tsl.definition.TSLDecoratorDefinition;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;

public class MyParameterDecorator extends TSLDecoratorDefinition {

    public MyParameterDecorator() {
        super("my_param_decorator");
    }

    @Override
    public void validateArguments(String[] arguments) throws TSLSyntaxError {}

}
