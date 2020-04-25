package net.programmer.igoodie.tsl.meta.comparator;

public class TSLComparatorLessThanOrEqual extends TSLComparator {

    public TSLComparatorLessThanOrEqual() {
        super("<=");
    }

    @Override
    public boolean compare(Object left, String right) {
        if (left instanceof Number) {
            return ((Number) left).doubleValue() <= parseNumber(right);
        }

        return false;
    }

}
