package org.marat.advent.day03;

import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.marat.advent.common.Util;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day03 {

    public static void main(String[] args) {
        Pattern delimiter = Pattern.compile("\n+");

        List<Triangle> triangles = Util.readElementsWithDelimiter("day03/input.txt", delimiter)
                .map(StringUtils::trim)
                .filter(StringUtils::isNotEmpty)
                .map(Triangle::new)
                .filter(Triangle::isValid)
                .collect(Collectors.toList());

        System.out.println(triangles.size());
    }

    @ToString
    static class Triangle {

        private final int a;
        private final int b;
        private final int c;

        public Triangle(String input) {
            String[] sides = StringUtils.split(input, ' ');

            if (sides.length != 3) {
                throw new IllegalArgumentException(String.format("Not a triangle, shape has %d sides.", sides.length));
            }

            a = Integer.parseInt(sides[0]);
            b = Integer.parseInt(sides[1]);
            c = Integer.parseInt(sides[2]);
        }

        public boolean isValid() {
            return (a + b) > c && (b + c) > a && (a + c) > b;
        }

    }

}
