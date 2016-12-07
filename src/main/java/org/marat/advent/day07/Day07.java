package org.marat.advent.day07;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.marat.advent.common.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day07 {

    private static final Pattern STANDARD_PATTERN = Pattern.compile("^([a-z]+)(.*)");
    private static final Pattern HYPERNET_PATTERN = Pattern.compile("^\\[([a-z]+)](.*)");

    public static void main(String[] args) {
        Pattern delimiter = Pattern.compile("\\s+");

        Stream<Ipv7> ipv7Stream = Util.readElementsWithDelimiter("day07/input.txt", delimiter)
                .map(StringUtils::trim)
                .filter(StringUtils::isNotEmpty)
                .map(Day07::parse)
                .filter(Ipv7::supportsTls);

        System.out.println(ipv7Stream.count());
    }

    public static Ipv7 parse(String address) {
        return parse(address, new Ipv7(address));
    }

    public static Ipv7 parse(String addressPart, Ipv7 ipv7) {
        Matcher goodMatcher = STANDARD_PATTERN.matcher(addressPart);

        if (goodMatcher.matches()) {
            ipv7.addStandardSequence(goodMatcher.group(1));

            return parse(goodMatcher.group(2), ipv7);
        } else {
            Matcher evilMatcher = HYPERNET_PATTERN.matcher(addressPart);

            if (evilMatcher.matches()) {
                ipv7.addHypernetSequence(evilMatcher.group(1));

                return parse(evilMatcher.group(2), ipv7);
            } else {
                return ipv7;
            }
        }
    }

    @RequiredArgsConstructor
    @Getter
    static class Ipv7 {

        private final String complete;

        private final List<String> standardSequences = new ArrayList<>();

        private final List<String> hypernetSequences = new ArrayList<>();

        public void addStandardSequence(String string) {
            standardSequences.add(string);
        }

        public List<String> getStandardSequences() {
            return Collections.unmodifiableList(standardSequences);
        }

        public void addHypernetSequence(String string) {
            hypernetSequences.add(string);
        }

        public List<String> getHypernetSequences() {
            return Collections.unmodifiableList(hypernetSequences);
        }

        public boolean supportsTls() {
            return standardSequences.stream().anyMatch(this::hasAbba)
                    && hypernetSequences.stream().noneMatch(this::hasAbba);
        }

        private boolean hasAbba(String string) {
            char[] chars = new char[4];

            for (int i = 0; i <= string.length() - 4; i++) {
                string.getChars(i, i + 4, chars, 0);

                if (isAbba(chars)) {
                    return true;
                }
            }

            return false;
        }

        private boolean isAbba(char[] chars) {
            assert chars.length == 4;

            return chars[0] == chars[3] &&
                    chars[1] == chars[2] &&
                    chars[0] != chars[1];
        }

    }

}
