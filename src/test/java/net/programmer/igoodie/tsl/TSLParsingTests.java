package net.programmer.igoodie.tsl;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLTokenizer;
import net.programmer.igoodie.tsl.runtime.context.TSLActionArguments;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.context.TSLEventArguments;
import net.programmer.igoodie.tsl.runtime.token.TSLExpression;
import net.programmer.igoodie.tsl.util.Resources;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class TSLParsingTests {

    @Test
    public void foo() throws IOException, TSLSyntaxError {
        String tsl = Resources.readTSL("rules.somebody.tsl");

        TSLTokenizer tokenizer = new TSLTokenizer(tsl);
        List<String> rules = tokenizer.intoRules();

        System.out.println("Parsed rules:");
        rules.stream().map(rule -> " - " + rule)
                .forEach(System.out::println);

        System.out.println();

        for (int i = 0; i < tokenizer.ruleCount(); i++) {
            List<Object> tokens = tokenizer.intoTokens(i);
            for (Object token : tokens) {
                if (token instanceof TSLExpression) {
                    TSLContext context = new TSLContext();
                    context.setActionArguments(new TSLActionArguments()
                            .with("mobName", "Zombie")
                            .with("loopCount", 5));
                    context.setEventArguments(new TSLEventArguments()
                            .with("viewerCount", 105));

                    String eval = ((TSLExpression) token).getValue(context);
                    System.out.println("EVALUATED: " + eval);
                }
            }
        }
    }

}
