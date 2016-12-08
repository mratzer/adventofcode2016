package org.marat.advent.day07;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.marat.advent.common.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day07 {

    private static final Pattern SUPERNET_PATTERN = Pattern.compile("^([a-z]+)(.*)");
    private static final Pattern HYPERNET_PATTERN = Pattern.compile("^\\[([a-z]+)](.*)");

    public static void main(String[] args) {
        Pattern delimiter = Pattern.compile("\\s+");

        Stream<Ipv7> ipv7Stream = Util.readElementsWithDelimiter("day07/input.txt", delimiter)
                .map(StringUtils::trim)
                .filter(StringUtils::isNotEmpty)
                .map(Day07::parse)
                .filter(Ipv7::supportsSsl);

        System.out.println(ipv7Stream.count());
    }

    public static Ipv7 parse(String address) {
        return parse(address, new Ipv7(address));
    }

    public static Ipv7 parse(String addressPart, Ipv7 ipv7) {
        Matcher supernetMatcher = SUPERNET_PATTERN.matcher(addressPart);

        if (supernetMatcher.matches()) {
            ipv7.addSupernetSequence(supernetMatcher.group(1));

            return parse(supernetMatcher.group(2), ipv7);
        } else {
            Matcher hypernetMatcher = HYPERNET_PATTERN.matcher(addressPart);

            if (hypernetMatcher.matches()) {
                ipv7.addHypernetSequence(hypernetMatcher.group(1));

                return parse(hypernetMatcher.group(2), ipv7);
            } else {
                return ipv7;
            }
        }
    }

    @RequiredArgsConstructor
    @Getter
    static class Ipv7 {

        private final String complete;

        private final List<String> supernetSequences = new ArrayList<>();

        private final List<String> hypernetSequences = new ArrayList<>();

        public void addSupernetSequence(String string) {
            supernetSequences.add(string);
        }

        public List<String> getSupernetSequences() {
            return Collections.unmodifiableList(supernetSequences);
        }

        public void addHypernetSequence(String string) {
            hypernetSequences.add(string);
        }

        public List<String> getHypernetSequences() {
            return Collections.unmodifiableList(hypernetSequences);
        }

        public boolean supportsTls() {
            return supernetSequences.stream().anyMatch(this::hasAbba)
                    && hypernetSequences.stream().noneMatch(this::hasAbba);
        }

        public boolean supportsSsl() {
            Set<String> abas = supernetSequences.stream()
                    .flatMap(sequence -> getAbasAndBabs(sequence).stream())
                    .collect(Collectors.toSet());

            Set<String> babs = hypernetSequences.stream()
                    .flatMap(sequence -> getAbasAndBabs(sequence).stream())
                    .collect(Collectors.toSet());

            for (String aba : abas) {
                for (String bab : babs) {
                    if (isCorresponding(aba, bab)) {
                        return true;
                    }
                }
            }

            return false;
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

        private List<String> getAbasAndBabs(String string) {
            List<String> abasAndBabs = new ArrayList<>();
            char[] chars = new char[3];

            for (int i = 0; i <= string.length() - 3; i++) {
                string.getChars(i, i + 3, chars, 0);

                if (isAbaOrBab(chars)) {
                    abasAndBabs.add(new String(chars));
                }
            }

            return abasAndBabs;
        }

        private boolean isAbaOrBab(char[] chars) {
            assert chars.length == 3;

            return chars[0] == chars[2] && chars[0] != chars[1];
        }

        private boolean isCorresponding(String aba, String bab) {
            return aba.charAt(0) == bab.charAt(1) && aba.charAt(1) == bab.charAt(0);
        }

    }

}
