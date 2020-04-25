package net.programmer.igoodie.tsl.meta.comparator;

public abstract class TSLComparator {

    private String symbol;

    public TSLComparator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public abstract boolean compare(Object left, String right);

    /* ---------------------------------------------- */

    public double parseNumber(String string) {
        try {
            return Double.parseDouble(string);

        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

}
