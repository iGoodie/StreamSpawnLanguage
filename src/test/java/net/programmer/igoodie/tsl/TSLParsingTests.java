package net.programmer.igoodie.tsl;

import net.programmer.igoodie.tsl.definition.TSLTestEvent;
import net.programmer.igoodie.tsl.exception.TSLParsingError;
import net.programmer.igoodie.tsl.exception.TSLPluginError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.parser.TSLTokenizer;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;
import net.programmer.igoodie.tsl.util.Resources;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TSLParsingTests {

    @BeforeAll
    public static void bootstrap() {
        StreamSpawnLanguage.bootstrap();

        try {
            StreamSpawnLanguage.registerEventDefinition(TSLTestEvent.INSTANCE);

        } catch (TSLPluginError tslPluginError) {
            tslPluginError.printStackTrace();
        }
    }

    @Test
    @DisplayName("should successfully do lexical analysis of rules")
    public void lexingTest() throws TSLSyntaxError {
        String tsl = Resources.readTSL("rules.somebody.tsl");
        List<TSLToken> tokens = TSLTokenizer.tokenize(tsl);

        System.out.println("Analyzing Tokens -> " + tokens + "\n");

        TSLLexer lexer = new TSLLexer(tokens);
        lexer.intoParts();

        System.out.println("Action tokens: " + lexer.getActionTokens());
        System.out.println("Event tokens: " + lexer.getEventTokens());
        System.out.println("Predicate tokens:");
        lexer.getPredicateTokens().forEach(predicates -> {
            System.out.println(" - " + predicates);
        });
    }

    @Test
    public void parsingTest() throws TSLParsingError {
        String tsl = Resources.readTSL("rules.somebody.tsl");

        TSLParser parser = new TSLParser(tsl);
        TSLRuleset ruleset = parser.parse();
    }

}
