package net.programmer.igoodie.tsl.meta.comparator;

public class TSLComparatorPrefix extends TSLComparator {

    public TSLComparatorPrefix() {
        super("PREFIX");
    }

    @Override
    public boolean compare(Object left, String right) {
        if (left instanceof String) {
            return right.toLowerCase().trim()
                    .startsWith(((String) left).toLowerCase().trim());
        }

        return false;
    }

}
