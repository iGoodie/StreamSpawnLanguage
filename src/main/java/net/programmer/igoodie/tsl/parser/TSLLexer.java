package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.TwitchSpawnLanguage;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.runtime.token.TSLPlainToken;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.LinkedList;
import java.util.List;

public class TSLLexer {

    public static final String EVENT_BEGIN = "ON";

    private List<TSLToken> tokens;
    private List<TSLToken> accumulator;

    private List<TSLToken> actionTokens;
    private List<TSLToken> eventTokens;
    private List<List<TSLToken>> predicateTokens;

    public TSLLexer(List<TSLToken> tokens) {
        this.tokens = tokens;
        this.accumulator = new LinkedList<>();
        this.actionTokens = new LinkedList<>();
        this.eventTokens = new LinkedList<>();
        this.predicateTokens = new LinkedList<>();
    }

    /* ---------------------------------------------- */

    private int indexOf(String plainToken) {
        for (int i = 0; i < tokens.size(); i++) {
            TSLToken token = tokens.get(i);

            if (token instanceof TSLPlainToken && token.getRaw().equalsIgnoreCase(plainToken)) {
                return i;
            }
        }

        return -1;
    }

    private List<TSLToken> extractTokens(int firstIndex, int lastIndex) {
        return extractTokens(this.tokens, firstIndex, lastIndex);
    }

    private List<TSLToken> extractTokens(List<TSLToken> tokens, int firstIndex, int lastIndex) {
        List<TSLToken> subview = tokens.subList(firstIndex, lastIndex + 1);
        return new LinkedList<>(subview);
    }

    public void pushPart() {
        if (accumulator.get(0).getRaw().equalsIgnoreCase(EVENT_BEGIN)) {
            eventTokens = extractTokens(accumulator, 1, accumulator.size() - 1);
        } else {
            predicateTokens.add(extractTokens(accumulator, 0, accumulator.size() - 1));
        }
    }

    /* ---------------------------------------------- */

    public void intoParts() throws TSLSyntaxError {
        if (indexOf(EVENT_BEGIN) == -1) {
            throw new TSLSyntaxError("Missing event statement.");
        }

        actionTokens = extractTokens(0, indexOf(EVENT_BEGIN) - 1);

        for (int i = indexOf(EVENT_BEGIN); i < tokens.size(); i++) {
            TSLToken token = tokens.get(i);

            if (TwitchSpawnLanguage.getPredicateDefinition(token.getRaw()) != null) {
                pushPart();
                accumulator.clear();

            } else if (i == tokens.size() - 1) {
                accumulator.add(token);
                pushPart();
                break;
            }

            accumulator.add(token);
        }
    }

    public List<TSLToken> getActionTokens() {
        return actionTokens;
    }

    public List<TSLToken> getEventTokens() {
        return eventTokens;
    }

    public List<List<TSLToken>> getPredicateTokens() {
        return predicateTokens;
    }

}
