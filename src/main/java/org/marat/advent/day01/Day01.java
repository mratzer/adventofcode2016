package org.marat.advent.day01;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.marat.advent.common.Util;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day01 {

    public static void main(String[] args) throws IOException {
        Pattern delimiter = Pattern.compile(",\\W?");

        List<Instruction> instructions = Util.readElementsWithDelimiter("day01/input.txt", delimiter)
                .map(StringUtils::trim)
                .filter(StringUtils::isNotEmpty)
                .map(Instruction::fromInput)
                .collect(Collectors.toList());

        Block startBlock = new Block(Direction.NORTH, 0, 0);

        Block lastBlock = getLastBlock(startBlock, instructions);

        System.out.println(startBlock.getManhattanDistance(lastBlock));
    }

    private static Block getLastBlock(Block startBlock, List<Instruction> instructions) {
        Block block = startBlock;

        Set<Block> visitedBlocks = new HashSet<>();

        visitedBlocks.add(block);

        for (Instruction instruction : instructions) {
            Direction direction = block.getDirection().turn(instruction.getTurn());

            for (int step = 0; step < instruction.getSteps(); step++) {
                block = block.move(direction);

                if (!visitedBlocks.add(block)) {
                    return block;
                }
            }
        }

        return block;
    }

    @Data
    @EqualsAndHashCode(exclude = "direction")
    static class Block {

        private final Direction direction;
        private final int x;
        private final int y;

        public Block move(Direction direction) {
            switch (direction) {
                case NORTH:
                    return new Block(direction, x, y + 1);
                case EAST:
                    return new Block(direction, x + 1, y);
                case SOUTH:
                    return new Block(direction, x, y - 1);
                case WEST:
                    return new Block(direction, x - 1, y);
                default:
                    throw new IllegalArgumentException(String.format("Unknown direction '%s'.", direction));
            }
        }

        public int getManhattanDistance(Block block) {
            return Math.abs(block.x - x) + Math.abs(block.y - y);
        }

    }

    enum Direction {

        NORTH {
            @Override
            public Direction turnLeft() {
                return WEST;
            }

            @Override
            public Direction turnRight() {
                return EAST;
            }
        },

        EAST {
            @Override
            public Direction turnLeft() {
                return NORTH;
            }

            @Override
            public Direction turnRight() {
                return SOUTH;
            }
        },

        SOUTH {
            @Override
            public Direction turnLeft() {
                return EAST;
            }

            @Override
            public Direction turnRight() {
                return WEST;
            }
        },

        WEST {
            @Override
            public Direction turnLeft() {
                return SOUTH;
            }

            @Override
            public Direction turnRight() {
                return NORTH;
            }
        };

        protected abstract Direction turnLeft();

        protected abstract Direction turnRight();

        public Direction turn(Turn turn) {
            switch (turn) {
                case LEFT:
                    return turnLeft();
                case RIGHT:
                    return turnRight();
                default:
                    throw new IllegalArgumentException(String.format("Unknown turn '%s'.", turn));
            }

        }
    }

    enum Turn {

        LEFT("L"),
        RIGHT("R");

        private final String input;

        Turn(String input) {
            this.input = input;
        }

        public static Turn fromInput(String input) {
            for (Turn turn : values()) {
                if (turn.input.equals(input)) {
                    return turn;
                }
            }

            throw new IllegalArgumentException(String.format("Unknown turn '%s'.", input));
        }

    }

    @Data
    static class Instruction {

        private static final Pattern PATTERN = Pattern.compile("(R|L)(\\d+)");

        private final Turn turn;

        private final int steps;

        public static Instruction fromInput(String input) {
            Matcher matcher = PATTERN.matcher(input);

            if (matcher.matches()) {
                return new Instruction(
                        Turn.fromInput(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)));
            } else {
                throw new IllegalArgumentException(
                        String.format("Input '%s' does not match with pattern '%s'.", input, PATTERN.toString()));
            }
        }
    }


}
