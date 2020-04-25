package net.programmer.igoodie.tsl.meta.comparator;

public class TSLComparatorLessThan extends TSLComparator {

    public TSLComparatorLessThan() {
        super("<");
    }

    @Override
    public boolean compare(Object left, String right) {
        if (left instanceof Number) {
            return ((Number) left).doubleValue() < parseNumber(right);
        }

        return false;
    }

}
