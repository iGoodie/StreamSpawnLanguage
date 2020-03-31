package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.List;
import java.util.stream.Collectors;

public class TSLSyntaxError extends TSLError {

    private static final int ELLIPSIS_CHARS = 10; // ...? + this + 1 + this + ...?

    private String causedNear;

    public TSLSyntaxError(String message) {
        super(message);
    }

    public TSLSyntaxError(String message, String causedNear) {
        super(message);
        this.causedNear = causedNear;
    }

    public String getCausedNear() {
        return causedNear;
    }

    @Override
    public String toString() {
        return super.toString()
                + (causedNear != null ? "\tnear -> " + causedNear : "");
    }

    /* --------------------------------- */

    public static String causedNear(List<TSLToken> tokens) {
        return tokens.stream().map(TSLToken::getRaw).collect(Collectors.joining(" "));
    }

    public static String causedNear(List<TSLToken> tokens, int causedIndex) {
        String causedNearToken = tokens.get(causedIndex).getRaw();
        String script = tokens.stream().map(TSLToken::getRaw).collect(Collectors.joining(" "));
        return causedNear(script, script.indexOf(causedNearToken));
    }

    public static String causedNear(String script, int causedIndex) {
        int startIndex = Math.max(0, causedIndex - ELLIPSIS_CHARS);
        int lastIndex = Math.min(script.length() - 1, causedIndex + ELLIPSIS_CHARS);

        return (startIndex == 0 ? "" : "...")
                + script.substring(startIndex, lastIndex + 1)
                + (lastIndex == script.length() - 1 ? "" : "...");
    }

}
