package net.programmer.igoodie.tsl.meta.comparator;

public class TSLComparatorEquals extends TSLComparator {

    public TSLComparatorEquals() {
        super("=");
    }

    @Override
    public boolean compare(Object left, String right) {
        if (left instanceof Number) {
            return ((Number) left).doubleValue() == parseNumber(right);
        }

        if (left instanceof Boolean) {
            return left.toString().equalsIgnoreCase(right);
        }

        return left.equals(right);
    }

}
