package org.marat.advent.day02;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.marat.advent.common.Util;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day02 {

    public static void main(String[] args) {
        Pattern delimiter = Pattern.compile("\\W+");

        List<List<Direction>> allDirections = Util.readElementsWithDelimiter("day02/input.txt", delimiter)
                .map(StringUtils::trim)
                .filter(StringUtils::isNotEmpty)
                .map(Day02::toDirections)
                .collect(Collectors.toList());

        List<Button> combination = new ArrayList<>(allDirections.size());

        Button lastButton = getButtonField();

        for (List<Direction> directions : allDirections) {
            for (Direction direction : directions) {
                if (lastButton.hasNeighbor(direction)) {
                    lastButton = lastButton.getNeighbor(direction);
                }
            }

            combination.add(lastButton);
        }

        combination.stream().mapToInt(Button::getValue).forEach(value -> System.out.format("%X", value));
    }

    private static List<Direction> toDirections(String string) {
        return string.chars().mapToObj(Direction::fromInput).collect(Collectors.toList());
    }

    private static Button getButtonField() {
        Button button1 = new Button(0x01);
        Button button2 = new Button(0x02);
        Button button3 = new Button(0x03);
        Button button4 = new Button(0x04);
        Button button5 = new Button(0x05);
        Button button6 = new Button(0x06);
        Button button7 = new Button(0x07);
        Button button8 = new Button(0x08);
        Button button9 = new Button(0x09);
        Button buttonA = new Button(0x0A);
        Button buttonB = new Button(0x0B);
        Button buttonC = new Button(0x0C);
        Button buttonD = new Button(0x0D);

        button3.setNeighbor(Direction.UP, button1);
        button3.setNeighbor(Direction.LEFT, button2);
        button3.setNeighbor(Direction.RIGHT, button4);
        button3.setNeighbor(Direction.DOWN, button7);

        button6.setNeighbor(Direction.UP, button2);
        button6.setNeighbor(Direction.LEFT, button5);
        button6.setNeighbor(Direction.RIGHT, button7);
        button6.setNeighbor(Direction.DOWN, buttonA);

        button8.setNeighbor(Direction.UP, button4);
        button8.setNeighbor(Direction.LEFT, button7);
        button8.setNeighbor(Direction.RIGHT, button9);
        button8.setNeighbor(Direction.DOWN, buttonC);

        buttonB.setNeighbor(Direction.UP, button7);
        buttonB.setNeighbor(Direction.LEFT, buttonA);
        buttonB.setNeighbor(Direction.RIGHT, buttonC);
        buttonB.setNeighbor(Direction.DOWN, buttonD);

        return button5;
    }

    enum Direction {

        UP('U') {
            @Override
            public Direction getOpposite() {
                return DOWN;
            }
        },
        RIGHT('R') {
            @Override
            public Direction getOpposite() {
                return LEFT;
            }
        },
        DOWN('D') {
            @Override
            public Direction getOpposite() {
                return UP;
            }
        },
        LEFT('L') {
            @Override
            public Direction getOpposite() {
                return RIGHT;
            }
        };

        private final int input;

        Direction(int input) {
            this.input = input;
        }

        public abstract Direction getOpposite();

        public static Direction fromInput(int input) {
            for (Direction direction : Direction.values()) {
                if (direction.input == input) {
                    return direction;
                }
            }

            throw new IllegalArgumentException(String.format("Unknown direction '%c'.", input));
        }
    }

    @Data
    @ToString(exclude = "neighbors")
    static class Button {

        private final int value;

        private final Map<Direction, Button> neighbors = new EnumMap<>(Direction.class);

        public boolean hasNeighbor(Direction direction) {
            return neighbors.containsKey(direction);
        }

        public void setNeighbor(Direction direction, Button button) {
            neighbors.put(direction, button);
            button.neighbors.put(direction.getOpposite(), this);
        }

        public Button getNeighbor(Direction direction) {
            return neighbors.get(direction);
        }

    }

}
