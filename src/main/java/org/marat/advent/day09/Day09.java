package org.marat.advent.day09;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.marat.advent.common.Util;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day09 {

    public static void main(String[] args) {
        Pattern delimiter = Pattern.compile("\\s+");

        String input = Util.readElementsWithDelimiter("day09/input.txt", delimiter)
                .map(StringUtils::trim)
                .filter(StringUtils::isNotEmpty)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        System.out.println(uncompress("ADVENT"));
        System.out.println(uncompress("A(1x5)BC"));
        System.out.println(uncompress("(3x3)XYZ"));
        System.out.println(uncompress("A(2x2)BCD(2x2)EFG"));
        System.out.println(uncompress("(6x1)(1x3)A"));
        System.out.println(uncompress("X(8x2)(3x3)ABCY"));

        System.out.println(uncompress(input).length());
    }

    public static String uncompress(String input) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < input.length(); ) {
            if (input.charAt(i) == '(') {
                StringBuilder markerBuilder = new StringBuilder();

                do {
                    markerBuilder.append(input.charAt(i));
                } while (input.charAt(i++) != ')');

                Marker marker = new Marker(markerBuilder.toString());

                marker.uncompress(input.substring(i), sb::append);

                i += marker.getLength();
            } else {
                sb.append(input.charAt(i++));
            }
        }

        return sb.toString();
    }

    @Getter
    static class Marker {

        private static final Pattern MARKER_PATTERN = Pattern.compile("\\((\\d+)x(\\d+)\\)");

        private final int length;
        private final int count;

        public Marker(String input) {
            Matcher matcher = MARKER_PATTERN.matcher(input);

            if (matcher.matches()) {
                length = Integer.parseInt(matcher.group(1));
                count = Integer.parseInt(matcher.group(2));
            } else {
                throw new IllegalArgumentException(String.format("Invalid marker: '%s'.", input));
            }
        }

        public void uncompress(String input, Consumer<String> consumer) {
            String part = input.substring(0, getLength());

            for (int c = 0; c < getCount(); c++) {
                consumer.accept(part);
            }
        }

    }

}
