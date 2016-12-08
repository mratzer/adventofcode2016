package org.marat.advent.day08;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.marat.advent.common.Util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day08 {

    public static void main(String[] args) {
        Pattern delimiter = Pattern.compile("\\n+");

        Screen screen = new Screen(50, 6);

        Util.readElementsWithDelimiter("day08/input.txt", delimiter)
                .map(StringUtils::trim)
                .filter(StringUtils::isNotEmpty)
                .map(CommandPatterns::getCommand)
//                .peek(command -> System.out.println(command.asString()))
                .forEach(command -> command.execute(screen));

        System.out.println(screen);

        System.out.println(screen.getCount(true));
    }

    static class Screen {

        private final int width;
        private final int height;
        private final boolean[][] pixels;

        public Screen(int width, int height) {
            this.width = width;
            this.height = height;

            pixels = new boolean[width][height];
        }

        public void turnOn(int x, int y) {
            pixels[x][y] = true;
        }

        public void turnOff(int x, int y) {
            pixels[x][y] = false;
        }

        public boolean isOn(int x, int y) {
            return pixels[x][y];
        }

        public int getCount(boolean state) {
            int count = 0;

            for (int x = 0; x < pixels.length; x++) {
                for (int y = 0; y < pixels[x].length; y++) {
                    if (pixels[x][y]) {
                        count++;
                    }
                }
            }

            return count;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    if (pixels[col][row]) {
                        stringBuilder.append('#');
                    } else {
                        stringBuilder.append('.');
                    }
                }

                if (row < height - 1) {
                    stringBuilder.append('\n');
                }
            }

            return stringBuilder.toString();
        }
    }

    interface Command {

        void execute(Screen screen);

    }

    interface CommandPattern {
        boolean matches(String input);

        Command createCommand(String input);
    }

    static class CommandPatterns {

        private static final List<CommandPattern> KNOWN_COMMAND_PATTERNS = Arrays.asList(
                new RectCommandPattern(),
                new RotateRowCommandPattern(),
                new RotateColumnCommandPattern());

        public static Command getCommand(String input) {
            return KNOWN_COMMAND_PATTERNS.stream()
                    .filter(commandPattern -> commandPattern.matches(input))
                    .findFirst()
                    .map(commandPattern -> commandPattern.createCommand(input))
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Unsupported input: '%s'.", input)));
        }

    }

    static class RectCommandPattern implements CommandPattern {

        private static final Pattern PATTERN = Pattern.compile("rect (\\d+)x(\\d+)");

        @Override
        public boolean matches(String input) {
            return PATTERN.matcher(input).matches();
        }

        @Override
        public Command createCommand(String input) {
            Matcher matcher = PATTERN.matcher(input);

            if (matcher.matches()) {
                return new RectCommand(
                        Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)));
            } else {
                throw new IllegalArgumentException(String.format("Invalid rect command: '%s'.", input));
            }
        }
    }

    static class RotateRowCommandPattern implements CommandPattern {

        private static final Pattern PATTERN = Pattern.compile("rotate row y=(\\d+) by (\\d+)");

        @Override
        public boolean matches(String input) {
            return PATTERN.matcher(input).matches();
        }

        @Override
        public Command createCommand(String input) {
            Matcher matcher = PATTERN.matcher(input);

            if (matcher.matches()) {
                return new RotateRowCommand(
                        Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)));
            } else {
                throw new IllegalArgumentException(String.format("Invalid rect command: '%s'.", input));
            }
        }
    }

    static class RotateColumnCommandPattern implements CommandPattern {

        private static final Pattern PATTERN = Pattern.compile("rotate column x=(\\d+) by (\\d+)");

        @Override
        public boolean matches(String input) {
            return PATTERN.matcher(input).matches();
        }

        @Override
        public Command createCommand(String input) {
            Matcher matcher = PATTERN.matcher(input);

            if (matcher.matches()) {
                return new RotateColumnCommand(
                        Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)));
            } else {
                throw new IllegalArgumentException(String.format("Invalid rect command: '%s'.", input));
            }
        }
    }

    @RequiredArgsConstructor
    static class RectCommand implements Command {

        private final int width;
        private final int height;

        @Override
        public void execute(Screen screen) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    screen.turnOn(x, y);
                }
            }
        }

    }

    @RequiredArgsConstructor
    static class RotateRowCommand implements Command {

        private final int row;
        private final int count;

        @Override
        public void execute(Screen screen) {
            boolean[] rowPixels = new boolean[screen.getWidth()];

            for (int i = 0; i < screen.getWidth(); i++) {
                rowPixels[(i + count) % screen.getWidth()] = screen.isOn(i, row);
            }

            for (int i = 0; i < screen.getWidth(); i++) {
                if (rowPixels[i]) {
                    screen.turnOn(i, row);
                } else {
                    screen.turnOff(i, row);
                }
            }
        }

    }

    @RequiredArgsConstructor
    static class RotateColumnCommand implements Command {

        private final int column;
        private final int count;

        @Override
        public void execute(Screen screen) {
            boolean[] columnPixels = new boolean[screen.getHeight()];

            for (int i = 0; i < screen.getHeight(); i++) {
                columnPixels[(i + count) % screen.getHeight()] = screen.isOn(column, i);
            }

            for (int i = 0; i < screen.getHeight(); i++) {
                if (columnPixels[i]) {
                    screen.turnOn(column, i);
                } else {
                    screen.turnOff(column, i);
                }
            }
        }

    }


}
