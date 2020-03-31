package net.programmer.igoodie.tsl;

import net.programmer.igoodie.exampleplugin.ExamplePlugin;
import net.programmer.igoodie.tsl.definition.TSLPrintAction;
import net.programmer.igoodie.tsl.definition.TSLTestEvent;
import net.programmer.igoodie.tsl.exception.TSLError;
import net.programmer.igoodie.tsl.exception.TSLPluginError;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.parser.TSLTokenizer;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.context.TSLDecorator;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;
import net.programmer.igoodie.tsl.util.Resources;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        System.out.println("Parsed " + ruleset);
    }

}
