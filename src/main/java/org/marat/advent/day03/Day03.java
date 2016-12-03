package org.marat.advent.day03;

import org.apache.commons.lang3.StringUtils;
import org.marat.advent.common.Util;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day03 {

    public static void main(String[] args) {
        Pattern delimiter = Pattern.compile("\n+");

        List<String> triangles = Util.readElementsWithDelimiter("day03/input.txt", delimiter)
                .map(StringUtils::trim)
                .filter(StringUtils::isNotEmpty)
                .map(Day03::toTriangle)
                .collect(Collectors.toList());

        System.out.println(triangles);
    }

    private static String toTriangle(String string) {
        return string;
    }

}
