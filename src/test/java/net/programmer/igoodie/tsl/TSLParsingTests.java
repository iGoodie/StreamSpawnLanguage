package net.programmer.igoodie.tsl;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLTokenizer;
import net.programmer.igoodie.tsl.runtime.context.TSLActionArguments;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.context.TSLEventArguments;
import net.programmer.igoodie.tsl.runtime.token.TSLExpression;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.token.TSLTokenGroup;
import net.programmer.igoodie.tsl.util.Resources;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class TSLParsingTests {

    @BeforeAll
    public static void bootstrapTwitchSpawnLanguage() {
        TwitchSpawnLanguage.bootstrap();
    }

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
            List<TSLToken> tokens = tokenizer.intoTokens(i);
            TSLContext context = new TSLContext();
            context.setActionArguments(new TSLActionArguments()
                    .with("mobName", "Zombie")
                    .with("loopTimes", 5));
            context.setEventArguments(new TSLEventArguments()
                    .with("viewerCount", 105)
                    .with("actor", "iGoodie"));

            for (TSLToken token : tokens) {
                if (token instanceof TSLExpression) {
                    ((TSLExpression) token).validate(context);

                } else if (token instanceof TSLTokenGroup) {
                    ((TSLTokenGroup) token).validateExpressions(context);
                }

                String raw = token.getRaw();
                String eval = token.getValue(context);
                System.out.println(raw + " -> " + eval);
            }
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

}
