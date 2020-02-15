package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.runtime.token.TSLExpression;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.token.TSLPlainToken;
import net.programmer.igoodie.tsl.runtime.token.TSLTokenGroup;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class TSLTokenizer {

    public static final String MULTI_LINE_COMMENT_BEGIN = "#*";
    public static final String MULTI_LINE_COMMENT_END = "*#";
    public static final String EXPR_BEGIN = "${";
    public static final char EXPR_END = '}';
    public static final char COMMENT = '#';
    public static final char SPACE = ' ';
    public static final char GROUPING = '%';
    public static final char ESCAPE = '\\';

    private String script;
    private List<String> rules;

    private boolean escaping = false;
    private boolean grouping = false;
    private boolean expressing = false;

    private StringBuilder accumulator = new StringBuilder();
    private List<TSLToken> grouppedTokens = new LinkedList<>();
    private List<TSLToken> tokens = new LinkedList<>();

    public TSLTokenizer(String script) {
        this.script = script;
    }

    /* ------------------------------------------- */

    private void assertRuleTokenization() {
        if (rules == null) {
            throw new IllegalStateException("Cannot get rule count before rule tokenization");
        }
    }

    private String quoteRegex(char character) {
        return "\\" + character;
    }

    private String quoteRegex(String string) {
        return Pattern.quote(string);
    }

    private boolean lookahead(String source, int offset, String match) {
        for (int i = 0; i < match.length(); i++) {
            int sourceIndex = offset + i;

            if (sourceIndex < 0 || source.length() <= sourceIndex)
                return false;

            char sourceCharacter = source.charAt(sourceIndex);
            char targetCharacter = match.charAt(i);

            if (sourceCharacter != targetCharacter)
                return false;
        }

        return true;
    }

    private boolean isCommentLine(String line) {
        return line.matches("^[ \t]*" + quoteRegex(COMMENT) + ".*$");
    }

    private boolean isEmptyLine(String line) {
        return line.matches("\\s*");
    }

    private boolean isIndentedLine(String line) {
        return line.matches("^[ \t].*$");
    }

    private String trimMultilineComments(String script) throws TSLSyntaxError {
        String trimmed = script.replaceAll("(?s)"
                + quoteRegex(MULTI_LINE_COMMENT_BEGIN)
                + ".*?"
                + quoteRegex(MULTI_LINE_COMMENT_END), "");

        // Begun a multiline comment, but did not close
        int lonelyBeginIndex = trimmed.indexOf(MULTI_LINE_COMMENT_BEGIN);
        if (lonelyBeginIndex != -1) {
            throw new TSLSyntaxError(
                    "Unclosed multiline comment",
                    TSLSyntaxError.causedNear(script, lonelyBeginIndex)
            );
        }

        // Did not begin a multiline comment, but closed anyways
        int lonelyEndIndex = trimmed.indexOf(MULTI_LINE_COMMENT_END);
        if (lonelyEndIndex != -1) {
            throw new TSLSyntaxError(
                    "Unexpected comment closing",
                    TSLSyntaxError.causedNear(trimmed, lonelyEndIndex)
            );
        }

        return trimmed;
    }

    private String trimInlineComment(String script) {
        boolean escaping = false;
        boolean grouping = false;
        boolean expressing = false;

        for (int i = 0; i < script.length(); i++) {
            char character = script.charAt(i);

            if (character == COMMENT) {
                if (!grouping && !escaping && !expressing)
                    return script.substring(0, i);
            }

            if (character == GROUPING) {
                if (!escaping && !expressing)
                    grouping = !grouping;
            }

            if (lookahead(script, i, EXPR_BEGIN)) {
                expressing = true;
            }

            if (character == EXPR_END) {
                if (expressing)
                    expressing = false;
            }

            if (character == ESCAPE) {
                escaping = !escaping;
                continue;
            }

            escaping = false;
        }

        return script;
    }

    /* ------------------------------------------- */

    public int ruleCount() {
        assertRuleTokenization();
        return rules.size();
    }

    public String getRule(int index) {
        assertRuleTokenization();
        if (index < 0 || rules.size() <= index)
            throw new IndexOutOfBoundsException();
        return rules.get(index);
    }

    /* ------------------------------------------- */

    private void accumulate(Object accumulation) {
        accumulator.append(accumulation);
    }

    private void pushToken() {
        if (accumulator.length() == 0)
            return;

        List<TSLToken> associatedBucket = grouping ? grouppedTokens : tokens;
        String accumulation = accumulator.toString();

        associatedBucket.add(expressing
                ? new TSLExpression(accumulation)
                : new TSLPlainToken(accumulation));

        accumulator.setLength(0);
    }

    private void pushTokenGroup() {
        if (grouppedTokens.size() == 0)
            return;

        tokens.add(new TSLTokenGroup(new LinkedList<>(grouppedTokens)));

        grouppedTokens.clear();
    }

    /* ------------------------------------------- */

    public List<String> intoRules() throws TSLSyntaxError {
        this.rules = new LinkedList<>();

        String[] lines = trimMultilineComments(script).split("\\R");

        StringBuilder ruleAccumulator = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            // Skip comment lines
            if (isCommentLine(line)) continue;

            // Trim inline comments
            line = trimInlineComment(line);

            // Get to the next rule on empty line(s)
            if (isEmptyLine(line)) {
                if (ruleAccumulator.length() != 0)
                    rules.add(ruleAccumulator.toString());
                ruleAccumulator.setLength(0); // Reset
                continue;
            }

            // Append with a space on indented lines
            if (isIndentedLine(line)) {
                if (ruleAccumulator.length() == 0) {
                    throw new TSLSyntaxError(
                            "Invalid indent at line " + (i + 1),
                            TSLSyntaxError.causedNear(line, 0)
                    );
                }
                ruleAccumulator.append(SPACE).append(line.trim());
                continue;
            }

            // Not indented nor empty, expected indent
            if (ruleAccumulator.length() != 0) {
                throw new TSLSyntaxError(
                        "Missing indent at line " + (i + 1),
                        TSLSyntaxError.causedNear(line, 0)
                );
            }

            // Reset accumulation
            ruleAccumulator.setLength(0);
            ruleAccumulator.append(line.trim());
        }

        // Add last accumulation, if not empty
        if (ruleAccumulator.length() != 0) {
            rules.add(ruleAccumulator.toString());
        }

        return rules;
    }

    public List<TSLToken> intoTokens(int ruleIndex) throws TSLSyntaxError {
        assertRuleTokenization();
        return intoTokens(getRule(ruleIndex));
    }

    private List<TSLToken> intoTokens(String rule) throws TSLSyntaxError {
        for (int i = 0; i < rule.length(); i++) {
            char character = rule.charAt(i);

            if (!expressing && lookahead(rule, i, EXPR_BEGIN)) {
                if (escaping) {
                    accumulate(EXPR_BEGIN.charAt(0));
                    escaping = false;
                    continue;
                }

                pushToken();

                expressing = true;
                i += EXPR_BEGIN.length() - 1;
                continue;
            }

            if (expressing) {
                if (character == EXPR_END) {
                    pushToken();
                    expressing = false;
                    continue;
                }

                accumulate(character);
                continue;
            }

            if (character == SPACE) {
                if (escaping) {
                    throw new TSLSyntaxError(
                            "Unexpected escaping on space character",
                            TSLSyntaxError.causedNear(rule, i)
                    );
                }

                pushToken();
                continue;
            }

            if (character == GROUPING) {
                if (escaping) {
                    accumulate(GROUPING);
                    escaping = false;
                    continue;
                }

                if (grouping) {
                    pushToken();
                    pushTokenGroup();
                }

                grouping = !grouping;
                continue;
            }

            if (character == ESCAPE) {
                if (escaping) {
                    accumulate(ESCAPE);
                    continue;
                }

                escaping = !escaping;
                continue;
            }

            accumulate(character);
        }

        // Push token for the latest accumulation
        pushToken();

        if (escaping) {
            throw new TSLSyntaxError(
                    "Found incomplete escaping",
                    TSLSyntaxError.causedNear(rule, rule.length() - 1)
            );
        }

        if (grouping) {
            throw new TSLSyntaxError(
                    "Found incomplete grouping sequence",
                    TSLSyntaxError.causedNear(rule, rule.length() - 1)
            );
        }

        if (expressing) {
            throw new TSLSyntaxError(
                    "Found incomplete expression sequence",
                    TSLSyntaxError.causedNear(rule, rule.length() - 1)
            );
        }

        return tokens;
    }

    /* ------------------------------------------- */

    public static List<TSLToken> tokenize(String rule) throws TSLSyntaxError {
        TSLTokenizer tokenizer = new TSLTokenizer(rule);
        return tokenizer.intoTokens(rule);
    }

}
