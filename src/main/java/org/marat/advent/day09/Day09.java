package org.marat.advent.day09;

import org.apache.commons.lang3.StringUtils;
import org.marat.advent.common.Util;

import java.util.regex.Pattern;

public class Day09 {

    public static void main(String[] args) {
        Pattern delimiter = Pattern.compile("\\s+");

        String input = Util.readElementsWithDelimiter("day09/input.txt", delimiter)
                .map(StringUtils::trim)
                .filter(StringUtils::isNotEmpty)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        System.out.println(input);
    }

}
