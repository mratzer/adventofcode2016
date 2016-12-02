package org.marat.advent.common;

import java.io.InputStream;
import java.util.Scanner;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Util {

    public static Stream<String> readElementsWithDelimiter(String resource, Pattern delimiter) {
        Scanner scanner = new Scanner(getInputStream(resource));
        scanner.useDelimiter(delimiter);
        return streamScanner(scanner);
    }

    private static InputStream getInputStream(String resource) {
        return Util.class.getClassLoader().getResourceAsStream(resource);
    }

    private static Stream<String> streamScanner(Scanner scanner) {
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(scanner, Spliterator.ORDERED | Spliterator.NONNULL), false)
                .onClose(scanner::close);
    }

    private Util() {
        throw new IllegalStateException();
    }

}
