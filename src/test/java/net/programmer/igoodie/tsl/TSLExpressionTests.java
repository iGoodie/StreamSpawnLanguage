package net.programmer.igoodie.tsl;

import net.programmer.igoodie.tsl.exception.TSLPluginError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.lambda.TSLStringProvider;
import net.programmer.igoodie.tsl.parser.TSLTokenizer;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.token.TSLExpression;
import net.programmer.igoodie.tsl.runtime.context.TSLExpressionBindings;
import net.programmer.igoodie.tsl.util.Resources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TSLExpressionTests {

    @BeforeAll
    public static void registerProviders() throws TSLPluginError {
        TSLExpressionBindings.registerBindingProvider("myProvidedValue",
                () -> "Value#1");
        TSLExpressionBindings.registerBindingProvider("myProvidedFunc",
                () -> (TSLStringProvider) () -> "Value#2");
    }

    @Test
    @DisplayName("should include bindings provided during script runtime")
    public void providerTest() throws TSLSyntaxError {
        String tsl = Resources.readTSL("rules.expressions.tsl");

        for (Object token : TSLTokenizer.tokenize(tsl)) {
            if (token instanceof TSLExpression) {
                TSLContext testContext = new TSLContext();

                TSLExpression expression = (TSLExpression) token;
                TSLExpression.updateBindings();

                String evaluation = expression.calculateValue(testContext);
                Assertions.assertEquals(evaluation, "Value#1 Value#2");
            }
        }
    }

}
