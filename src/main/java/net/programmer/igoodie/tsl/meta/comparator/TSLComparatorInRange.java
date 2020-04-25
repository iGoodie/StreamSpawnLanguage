package net.programmer.igoodie.tsl.meta.comparator;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TSLComparatorInRange extends TSLComparator {

    public static Pattern RANGE_PATTERN = Pattern.compile("^\\[(?<min>.+),(?<max>.+)\\]$");

    public TSLComparatorInRange() {
        super("IN RANGE");
    }

    @Override
    public boolean compare(Object left, String right) throws TSLSyntaxError {
        Matcher matcher = RANGE_PATTERN.matcher(right);

        if (!matcher.find()) {
            throw new TSLSyntaxError(
                    "Expected format like [1.0,2.0], found -> " + right
            );
        }

        double min = parseNumber(matcher.group("min"));
        double max = parseNumber(matcher.group("max"));

        if (Double.isNaN(max) || Double.isNaN(min)) {
            throw new TSLSyntaxError(
                    "Expected valid numbers, found -> "
                            + matcher.group("min") + " and " + matcher.group("max")
            );
        }

        if (min > max) {
            throw new TSLSyntaxError(
                    "Expected first value to be less than the second value"
            );
        }

        if (left instanceof Number) {
            double numericValue = ((Number) left).doubleValue();
            return min <= numericValue && numericValue <= max;
        }

        return false;
    }

}
