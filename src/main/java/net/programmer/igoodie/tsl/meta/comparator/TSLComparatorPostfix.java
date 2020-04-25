package net.programmer.igoodie.tsl.meta.comparator;

public class TSLComparatorPostfix extends TSLComparator {

    public TSLComparatorPostfix() {
        super("POSTFIX");
    }

    @Override
    public boolean compare(Object left, String right) {
        if (left instanceof String) {
            return right.toLowerCase().trim()
                    .endsWith(((String) left).toLowerCase().trim());
        }

        return false;
    }

}
