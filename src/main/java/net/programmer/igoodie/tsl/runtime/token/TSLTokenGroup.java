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

    @Override
    public boolean validate(TSLContext context) throws TSLSyntaxError {
        boolean valid = true;

        for (TSLToken token : tokens) {
            valid &= token.validate(context);
        }

        return valid;
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
        boolean includeDelimiter = true;

        StringBuilder value = new StringBuilder();

        for (TSLToken token : tokens) {
            if (token.isExpression()) {
                value.append(token.getValue(context));
                includeDelimiter = false;
                continue;
            }

            if (includeDelimiter && value.length() != 0)
                value.append(TSLTokenizer.SPACE);

            value.append(token.getValue(context));

            includeDelimiter = true;
        }

        return value.toString();
    }

}
