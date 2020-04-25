package net.programmer.igoodie.tsl.util;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TSLRandomizer<T> {

    private static Random RANDOM = new Random();
    private static Pattern PERCENTAGE_PATTERN = Pattern.compile("(?<decimal>\\d{1,3})(\\.(?<fraction>\\d*))?");

    private int totalPercentage;
    private List<RandomNode> elements;

    public TSLRandomizer() {
        this.totalPercentage = 0;
        this.elements = new LinkedList<>();
    }

    public int getTotalPercentage() {
        return totalPercentage;
    }

    public List<T> elements() {
        return elements.stream()
                .map(element -> element.data)
                .collect(Collectors.toList());
    }

    public void addElement(T data, float percentage) throws TSLSyntaxError {
        RandomNode randomNode = new RandomNode();
        randomNode.percentage = (int) (percentage * 100);
        randomNode.data = data;
        addElement(randomNode);
    }

    public void addElement(T data, String percentage) throws TSLSyntaxError {
        addElement(toRandomNode(data, percentage));
    }

    public void addElement(RandomNode randomNode) {
        this.totalPercentage += randomNode.percentage;
        this.elements.add(randomNode);
    }

    public T randomItem() {
        int chance = RANDOM.nextInt(100_00);

        for (RandomNode element : elements) {
            if (chance < element.percentage)
                return element.data;
            chance -= element.percentage;
        }

        return elements.get(elements.size() - 1).data;
    }

    /* ------------------------ */

    public static String toFractionalPart(String source) {
        if (source.length() == 2) return source;
        if (source.length() > 2) return source.substring(0, 2);
        return source + "0";
    }

    private RandomNode toRandomNode(T data, String percentageRaw) throws TSLSyntaxError {
        Matcher matcher = PERCENTAGE_PATTERN.matcher(percentageRaw);

        if (!matcher.matches()) {
            throw new TSLSyntaxError(
                    "Unexpected percentage format -> " + percentageRaw
            );
        }

        try {
            String decimalGroup = matcher.group("decimal");
            String fractionGroup = matcher.group("fraction");

            int decimal = Integer.parseInt(decimalGroup);
            int fraction = fractionGroup == null
                    ? 0
                    : Integer.parseInt(toFractionalPart(fractionGroup));

            RandomNode randomNode = new RandomNode();
            randomNode.percentage = decimal * 100 + fraction;
            randomNode.data = data;

            return randomNode;

        } catch (NumberFormatException e) {
            throw new TSLSyntaxError(
                    "Unexpected percentage format -> " + percentageRaw
            );
        }
    }

    public class RandomNode implements Comparable<RandomNode> {
        int percentage; // 100_00 == 100.00%
        T data;

        @Override
        public int compareTo(RandomNode that) {
            if (this.percentage == that.percentage) return 0;
            return that.percentage > this.percentage ? 1 : -1;
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

}
