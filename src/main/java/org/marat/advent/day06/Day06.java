package org.marat.advent.day06;

import org.apache.commons.lang3.StringUtils;
import org.marat.advent.common.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class Day06 {

    public static void main(String[] args) {
        Pattern delimiter = Pattern.compile("\\s+");

        final int length = 8;

        List<Map<Character, AtomicInteger>> counts = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            counts.add(new TreeMap<>());
        }

        Util.readElementsWithDelimiter("day06/input.txt", delimiter)
                .map(StringUtils::trim)
                .filter(StringUtils::isNotEmpty)
                .forEach(line -> {
                    for (int i = 0; i < length; i++) {
                        counts.get(i).computeIfAbsent(line.charAt(i), c -> new AtomicInteger()).getAndIncrement();
                    }
                });

        char[] chars = new char[length];

        for (int i = 0; i < length; i++) {
            Character key = counts.get(i).entrySet().stream()
                    .sorted((o1, o2) -> o1.getValue().get() - o2.getValue().get())
                    .findFirst()
                    .orElseThrow(IllegalStateException::new)
                    .getKey();

            chars[i] = key;
        }

        System.out.println(new String(chars));
    }

}
