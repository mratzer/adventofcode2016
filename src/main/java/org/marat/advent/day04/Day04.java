package org.marat.advent.day04;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.marat.advent.common.Util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;

public class Day04 {

    public static void main(String[] args) {
        Pattern delimiter = Pattern.compile("\\s+");

        List<String> keyWords = Arrays.asList("north", "pole");

        int sectorId = Util.readElementsWithDelimiter("day04/input.txt", delimiter)
                .map(StringUtils::trim)
                .filter(StringUtils::isNotEmpty)
                .map(Door::new)
                .filter(Door::isValid)
                .filter(door -> keyWords.stream().allMatch(keyWord -> door.getName().contains(keyWord)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Could not find root with key words'%s'", keyWords)))
                .getSectorId();

        System.out.println(sectorId);
    }

    @Data
    static class Door {

        private static final Pattern PATTERN = Pattern.compile("([a-z\\-]+)-(\\d+)\\[([a-z]+)]");

        private final String encryptedName;
        private final int sectorId;
        private final String givenChecksum;
        private final String checksum;
        private final String name;

        public Door(String input) {
            Matcher matcher = PATTERN.matcher(input);

            if (matcher.matches()) {
                encryptedName = matcher.group(1);
                sectorId = Integer.parseInt(matcher.group(2));
                givenChecksum = matcher.group(3);

                checksum = computeChecksum(encryptedName, givenChecksum.length());
                name = decrypt(encryptedName, sectorId);
            } else {
                throw new IllegalArgumentException(String.format("Cannot parse input as room: '%s'", input));
            }
        }

        public boolean isValid() {
            return checksum.equals(givenChecksum);
        }

        private static String decrypt(String encryptedName, int sectorId) {
            return encryptedName.chars()
                    .mapToObj(i -> (char) i)
                    .map(c -> shift(c, sectorId))
                    .collect(Collector.of(
                            StringBuilder::new,
                            StringBuilder::append,
                            StringBuilder::append,
                            StringBuilder::toString));
        }

        private static char shift(char c, int sectorId) {
            if (c == '-') {
                return ' ';
            } else {
                int shift = sectorId % 26;

                return (char) ('a' + ((c - 'a' + shift) % 26));
            }
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
