package org.marat.advent.day08;

import org.apache.commons.lang3.StringUtils;
import org.marat.advent.common.Util;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day08 {

    public static void main(String[] args) {
        Pattern delimiter = Pattern.compile("\\n+");

        List<String> commands = Util.readElementsWithDelimiter("day08/input.txt", delimiter)
                .map(StringUtils::trim)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());

        System.out.println(commands);
    }

}
