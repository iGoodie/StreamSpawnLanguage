package net.programmer.igoodie.tsl.runtime.token;

import net.programmer.igoodie.tsl.parser.TSLTokenizer;
import net.programmer.igoodie.tsl.runtime.context.TSLContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TSLTokenGroup extends TSLToken {

    private List<TSLToken> tokens;

    public TSLTokenGroup(TSLToken... tokens) {
        this.tokens = Collections.unmodifiableList(Arrays.asList(tokens));
    }

    @Override
    public String getValue(TSLContext context) {
        return tokens.stream()
                .map(token -> token.getValue(context))
                .collect(Collectors.joining(String.valueOf(TSLTokenizer.SPACE)));
    }

}
