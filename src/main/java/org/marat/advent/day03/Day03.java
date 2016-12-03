package org.marat.advent.day03;

import org.apache.commons.lang3.StringUtils;
import org.marat.advent.common.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day03 {

    public static void main(String[] args) {
        Pattern delimiter = Pattern.compile("\\W+");

        String[] sides = Util.readElementsWithDelimiter("day03/input.txt", delimiter)
                .map(StringUtils::trim)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList())
                .toArray(new String[0]);

        if (sides.length % 3 != 0) {
            throw new IllegalArgumentException();
        }

        List<Triangle> triangles = new ArrayList<>(sides.length / 3);

        for (int i = 0; i < sides.length; i += 9) {
            for (int j = 0; j < 3; j++) {
                triangles.add(new Triangle(
                        Integer.parseInt(sides[i + j]),
                        Integer.parseInt(sides[i + j + 3]),
                        Integer.parseInt(sides[i + j + 6])
                ));
            }
        }

        long count = triangles.stream().filter(Triangle::isValid).count();

        System.out.println(count);
    }

    static class Triangle {

        private final int a;
        private final int b;
        private final int c;

        public Triangle(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public boolean isValid() {
            return (a + b) > c && (b + c) > a && (a + c) > b;
        }

    }

}
