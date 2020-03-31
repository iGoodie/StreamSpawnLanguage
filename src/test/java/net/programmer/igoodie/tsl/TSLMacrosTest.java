package net.programmer.igoodie.tsl;

import net.programmer.igoodie.exampleplugin.ExamplePlugin;
import net.programmer.igoodie.tsl.exception.TSLError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.parser.TSLTokenizer;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.context.TSLDecorator;
import net.programmer.igoodie.tsl.runtime.context.TSLEventArguments;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;
import net.programmer.igoodie.tsl.util.Resources;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TSLMacrosTest {

    @BeforeAll
    public static void bootstrapTwitchSpawnLanguage() {
        TwitchSpawnLanguage.bootstrap();
        ExamplePlugin.initialize();
    }

    //    @Test
    @DisplayName("should lexe macros")
    public void macrosTest() throws TSLError {
        String script = Resources.readTSL("rules.at-rules.tsl");

        TSLTokenizer tokenizer = new TSLTokenizer(script);
        tokenizer.intoRules();

        for (int i = 0; i < tokenizer.ruleCount(); i++) {
            System.out.println();

            List<TSLToken> tokens = tokenizer.intoTokens(i);
            System.out.println("Tokens: " + tokens);

            TSLLexer lexer = new TSLLexer(tokens);
            lexer.intoParts();

            if (!lexer.getDecoratorTokens().isEmpty()) { // has decorators
                for (TSLToken decoratorToken : lexer.getDecoratorTokens()) {
                    TSLDecorator decorator = TSLParser.parseDecorator(decoratorToken);
                    System.out.println(decorator);
                }
            }
        }
    }

    @Test
    @DisplayName("should parse macros")
    public void macroParseTest() throws TSLError {
        String script = Resources.readTSL("rules.at-rules.tsl");
        TSLRuleset ruleset = new TSLParser(script).parse();

        TSLEventArguments eventArguments = new TSLEventArguments()
                .with("actor", "TestBoii");

        ruleset.dispatch("Example Event", eventArguments);

        // Try replacing captures and then evaluating them
//        TSLRule firstRule = ruleset.getRules().get(0);
//        System.out.println(firstRule.getTokens());
//        System.out.println(ruleset.replaceCaptures(firstRule.getTokens()));
//        System.out.println(ruleset.replaceCaptures(firstRule.getTokens()).stream()
//                .map(token -> {
//                    try {
//                        token.validate(new TSLContext());
//                    } catch (TSLSyntaxError tslSyntaxError) {
//                        tslSyntaxError.printStackTrace();
//                    }
//                    return token.getValue(new TSLContext());
//                }).collect(Collectors.toList()));
    }

}
