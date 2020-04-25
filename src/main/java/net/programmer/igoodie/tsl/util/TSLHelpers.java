package net.programmer.igoodie.tsl.util;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.LinkedList;
import java.util.List;

public class TSLHelpers {

    public static List<List<TSLToken>> splitTokens(List<TSLToken> tokens, String delimiter)
            throws TSLSyntaxError {
        List<List<TSLToken>> splits = new LinkedList<>();
        List<TSLToken> accumulation = new LinkedList<>();

        for (int i = 0; i < tokens.size(); i++) {
            boolean lastToken = (i == tokens.size() - 1);
            TSLToken token = tokens.get(i);
            String tokenRaw = token.getRaw();

            if (lastToken) {
                if (tokenRaw.equalsIgnoreCase(delimiter)) {
                    throw new TSLSyntaxError("Unexpected delimiter token at the end -> " + delimiter);
                }
                accumulation.add(token);
            }

            if (tokenRaw.equalsIgnoreCase(delimiter) || lastToken) {
                if (accumulation.isEmpty()) {
                    throw new TSLSyntaxError("Expected an action after the delimiter -> " + delimiter);
                }

                splits.add(accumulation);
                accumulation = new LinkedList<>();
                continue;
            }

            accumulation.add(token);
        }

        return splits;
    }

}
