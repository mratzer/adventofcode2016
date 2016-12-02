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

        combination.stream().mapToInt(Button::getValue).forEach(System.out::print);
    }

    private static List<Direction> toDirections(String string) {
        return string.chars().mapToObj(Direction::fromInput).collect(Collectors.toList());
    }

    private static Button getButtonField() {
        Button button1 = new Button(1);
        Button button2 = new Button(2);
        Button button3 = new Button(3);
        Button button4 = new Button(4);
        Button button5 = new Button(5);
        Button button6 = new Button(6);
        Button button7 = new Button(7);
        Button button8 = new Button(8);
        Button button9 = new Button(9);

        button2.setNeighbor(Direction.LEFT, button1);
        button2.setNeighbor(Direction.RIGHT, button3);
        button2.setNeighbor(Direction.DOWN, button5);

        button4.setNeighbor(Direction.UP, button1);
        button4.setNeighbor(Direction.RIGHT, button5);
        button4.setNeighbor(Direction.DOWN, button7);

        button6.setNeighbor(Direction.UP, button3);
        button6.setNeighbor(Direction.LEFT, button5);
        button6.setNeighbor(Direction.DOWN, button9);

        button8.setNeighbor(Direction.UP, button5);
        button8.setNeighbor(Direction.LEFT, button7);
        button8.setNeighbor(Direction.RIGHT, button9);

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
