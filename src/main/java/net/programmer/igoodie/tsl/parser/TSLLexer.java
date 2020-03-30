package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.TwitchSpawnLanguage;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.runtime.token.TSLPlainToken;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class TSLLexer {

    public static final String EVENT_BEGIN = "ON";
    public static final String NOTIFICATION_BEGIN = "DISPLAYING";

    private List<TSLToken> tokens;
    private List<TSLToken> accumulator;

    private String captureName;
    private List<TSLToken> capturedSnippet;

    private List<TSLToken> decoratorTokens;
    private List<TSLToken> actionTokens;
    private List<TSLToken> eventTokens;
    private List<List<TSLToken>> predicateTokens;

    public TSLLexer(List<TSLToken> tokens) {
        this.tokens = tokens;
        this.accumulator = new LinkedList<>();
        this.decoratorTokens = new LinkedList<>();
        this.actionTokens = new LinkedList<>();
        this.eventTokens = new LinkedList<>();
        this.predicateTokens = new LinkedList<>();
    }

    public boolean isCapture() {
        return tokens.get(0).isCaptureName();
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

    private void pushPart() {
        if (accumulator.get(0).getRaw().equalsIgnoreCase(EVENT_BEGIN)) {
            eventTokens = extractTokens(accumulator, 1, accumulator.size() - 1);
        } else {
            predicateTokens.add(extractTokens(accumulator, 0, accumulator.size() - 1));
        }
    }

    /* ---------------------------------------------- */

    public void intoParts() throws TSLSyntaxError {
        if (isCapture()) {
            captureName = tokens.get(0).getRaw().substring(1);
            capturedSnippet = extractTokens(1, tokens.size() - 1);
            if (captureName.isEmpty()) {
                throw new TSLSyntaxError("Capture missing a name");
            }
            if (capturedSnippet.isEmpty()) {
                throw new TSLSyntaxError("Captures cannot be empty");
            }
            return;
        }

        int beginningOfEventPart = indexOf(EVENT_BEGIN);

        if (beginningOfEventPart == -1) {
            throw new TSLSyntaxError("Missing event statement.");
        }

        int beginningOfActionPart = 0;

        while (beginningOfActionPart < beginningOfEventPart) {
            TSLToken token = tokens.get(beginningOfActionPart);
            if (!token.isDecorator()) break;
            decoratorTokens.add(token);
            beginningOfActionPart++;
        }

        actionTokens = extractTokens(beginningOfActionPart, indexOf(EVENT_BEGIN) - 1);

        for (int i = beginningOfEventPart; i < tokens.size(); i++) {
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

    public String getCaptureName() {
        return captureName;
    }

    public List<TSLToken> getCapturedSnippet() {
        return capturedSnippet;
    }

    public List<TSLToken> getDecoratorTokens() {
        return decoratorTokens;
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

    /* ---------------------------------------------- */

    public static List<TSLToken> actionPart(List<TSLToken> actionTokens) {
        return actionPart(actionTokens, NOTIFICATION_BEGIN);
    }

    public static List<TSLToken> actionPart(List<TSLToken> actionTokens, String until) {
        List<TSLToken> actionPart = new LinkedList<>();

        // Does not include the action name
        for (int i = 1; i < actionTokens.size(); i++) {
            TSLToken actionToken = actionTokens.get(i);
            if (actionToken.getRaw().equalsIgnoreCase(until))
                break;
            actionPart.add(actionToken);
        }

        return actionPart;
    }

    public static List<TSLToken> notificationPart(List<TSLToken> actionTokens) {
        int beginIndex = IntStream.range(0, actionTokens.size())
                .filter(i -> actionTokens.get(i).getRaw().equalsIgnoreCase(NOTIFICATION_BEGIN))
                .max().orElse(-1);

        // No notification begin token, assuming no notification
        if (beginIndex == -1)
            return null;

        List<TSLToken> notificationPart = new LinkedList<>();
        for (int i = beginIndex + 1; i < actionTokens.size(); i++) {
            notificationPart.add(actionTokens.get(i));
        }
        return notificationPart;
    }

}
