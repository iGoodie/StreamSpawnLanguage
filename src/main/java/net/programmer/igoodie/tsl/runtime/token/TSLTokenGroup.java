package net.programmer.igoodie.tsl.runtime.token;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLTokenizer;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TSLTokenGroup extends TSLToken {

    private List<TSLToken> tokens;

    public TSLTokenGroup(TSLToken... tokens) {
        this(Arrays.asList(tokens));
    }

    public TSLTokenGroup(List<TSLToken> tokens) {
        this.tokens = Collections.unmodifiableList(tokens);
    }

    public void validateExpressions(TSLContext context) throws TSLSyntaxError {
        for (TSLToken token : tokens) {
            if (token instanceof TSLExpression) {
                ((TSLExpression) token).validate(context);
            }
        }
    }

    @Override
    public String getRaw() {
        return tokens.stream()
                .map(TSLToken::getRaw)
                .collect(Collectors.joining(" ",
                        String.valueOf(TSLTokenizer.GROUPING),
                        String.valueOf(TSLTokenizer.GROUPING)));
    }

    @Override
    public String getValue(TSLContext context) {
        return tokens.stream()
                .map(token -> token.getValue(context))
                .collect(Collectors.joining(String.valueOf(TSLTokenizer.SPACE)));
    }

}
