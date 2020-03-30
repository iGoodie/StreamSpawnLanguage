package net.programmer.igoodie.tsl;

import net.programmer.igoodie.tsl.definition.TSLPrintAction;
import net.programmer.igoodie.tsl.definition.TSLTestEvent;
import net.programmer.igoodie.tsl.exception.TSLError;
import net.programmer.igoodie.tsl.exception.TSLPluginError;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.parser.TSLTokenizer;
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

        try {
            TwitchSpawnLanguage.registerEventDefinition(TSLTestEvent.INSTANCE);
            TwitchSpawnLanguage.registerActionDefinition(TSLPrintAction.INSTANCE);

        } catch (TSLPluginError tslPluginError) {
            tslPluginError.printStackTrace();
        }
    }

    @Test
    @DisplayName("should parse macros")
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
            printParts(lexer);


        }
    }

    private static void printParts(TSLLexer lexer) {
        if (lexer.isCapture()) {
            System.out.println("Capture Name: " + lexer.getCaptureName());
            System.out.println("Captured Snippet: " + lexer.getCapturedSnippet());
        } else {
            System.out.println("Decorators: " + lexer.getDecoratorTokens());
            System.out.println("Action: " + lexer.getActionTokens());
            System.out.println("Event: " + lexer.getEventTokens());
            System.out.println("Predicates: " + lexer.getPredicateTokens());
        }
    }

}
