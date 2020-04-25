package net.programmer.igoodie.tsl.meta.comparator;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;

public abstract class TSLComparator {

    private String symbol;

    public TSLComparator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public abstract boolean compare(Object left, String right) throws TSLSyntaxError;

    /* ---------------------------------------------- */

    public double parseNumber(String string) {
        try {
            return Double.parseDouble(string);

        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

}
