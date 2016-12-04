package org.marat.advent.day04;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.marat.advent.common.Util;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;

public class Day04 {

    public static void main(String[] args) {
        Pattern delimiter = Pattern.compile("\\s+");

        int sum = Util.readElementsWithDelimiter("day04/input.txt", delimiter)
                .map(StringUtils::trim)
                .filter(StringUtils::isNotEmpty)
                .map(Door::new)
                .filter(Door::isValid)
                .mapToInt(Door::getSectorId)
                .sum();

        System.out.println(sum);
    }

    @Data
    static class Door {

        private static final Pattern PATTERN = Pattern.compile("([a-z\\-]+)-(\\d+)\\[([a-z]+)]");

        private final String name;
        private final int sectorId;
        private final String givenChecksum;
        private final String checksum;

        public Door(String input) {
            Matcher matcher = PATTERN.matcher(input);

            if (matcher.matches()) {
                name = matcher.group(1);
                sectorId = Integer.parseInt(matcher.group(2));
                givenChecksum = matcher.group(3);

                checksum = computeChecksum(name, givenChecksum.length());
            } else {
                throw new IllegalArgumentException(String.format("Cannot parse input as room: '%s'", input));
            }
        }

        public boolean isValid() {
            return checksum.equals(givenChecksum);
        }

        private static String computeChecksum(String name, int length) {
            Map<Character, AtomicInteger> counts = new TreeMap<>();

            name.chars()
                    .mapToObj(i -> (char) i)
                    .filter(c -> c != '-')
                    .forEachOrdered(c -> counts.computeIfAbsent(c, (key) -> new AtomicInteger(0)).getAndIncrement());

            return counts.entrySet().stream()
                    .sorted((o1, o2) -> o2.getValue().get() - o1.getValue().get())
                    .limit(length)
                    .map(Map.Entry::getKey)
                    .collect(Collector.of(
                            StringBuilder::new,
                            StringBuilder::append,
                            StringBuilder::append,
                            StringBuilder::toString));
        }


    }

}
