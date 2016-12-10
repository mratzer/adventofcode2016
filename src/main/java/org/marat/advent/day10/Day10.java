package org.marat.advent.day10;

import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.marat.advent.common.Util;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day10 {

    public static void main(String[] args) {
        Pattern delimiter = Pattern.compile("\\n+");

        List<String> lines = Util.readElementsWithDelimiter("day10/input.txt", delimiter)
                .map(StringUtils::trim)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());

        List<BotConfiguration> botConfigurations = lines.stream()
                .filter(BotConfiguration::matchesConfigurationPattern)
                .map(BotConfiguration::new)
                .collect(Collectors.toList());

        Factory factory = new Factory(botConfigurations);

        lines.stream()
                .filter(Command::matchesCommandPattern)
                .map(command -> new Command(factory, command))
                .forEach(Command::execute);

        Comparision comparisionToSearch = new Comparision(new Chip(17), new Chip(61));

        Bot comparingBot = factory.getBots()
                .stream()
                .filter(bot -> bot.getComparisions().contains(comparisionToSearch))
                .findFirst()
                .orElseThrow(IllegalStateException::new);

        System.out.println(comparingBot.getId());

        int result = Stream.of(
                factory.getOutputsById().get(0),
                factory.getOutputsById().get(1),
                factory.getOutputsById().get(2))
                .mapToInt(output -> output.getChips().stream().mapToInt(Chip::getValue).sum())
                .reduce(1, (x, y) -> x * y);

        System.out.println(result);
    }

    static class Command {

        private static final Pattern COMMAND_PATTERN = Pattern.compile("value (\\d+) goes to bot (\\d+)");

        private final Bot bot;

        private final Chip chip;

        Command(Factory factory, String input) {
            Matcher matcher = COMMAND_PATTERN.matcher(input);

            if (matcher.matches()) {
                chip = new Chip(Integer.parseInt(matcher.group(1)));
                bot = factory.getBotsById().get(Integer.valueOf(matcher.group(2)));
            } else {
                throw new IllegalArgumentException(String.format("Invalid command: '%s'.", input));
            }
        }

        void execute() {
            bot.accept(chip);
        }


        static boolean matchesCommandPattern(String input) {
            return COMMAND_PATTERN.matcher(input).matches();
        }

    }

    @Data
    static class Bot implements Destination {

        private final int id;
        private Destination low;
        private Destination high;

        private final TreeSet<Chip> chips = new TreeSet<>();
        private final Set<Comparision> comparisions = new HashSet<>();

        @Override
        public void accept(Chip chip) {
            chips.add(chip);

            if (chips.size() == 2) {
                comparisions.add(new Comparision(chips.first(), chips.last()));

                low.accept(chips.first());
                high.accept(chips.last());
                chips.clear();
            }
        }
    }

    @Data
    static class Output implements Destination {

        private final int id;

        private final List<Chip> chips = new ArrayList<>();

        @Override
        public void accept(Chip chip) {
            chips.add(chip);
        }
    }

    @Data
    static class Chip implements Comparable<Chip> {

        private final int value;

        @Override
        public int compareTo(Chip o) {
            return Integer.compare(value, o.value);
        }
    }

    @Data
    static class Comparision {
        private final Chip low;
        private final Chip high;
    }

    public static class Factory {

        private final Map<Integer, BotConfiguration> botDescriptions;

        private final Map<Integer, Bot> bots = new HashMap<>();

        private final Map<Integer, Output> outputs = new HashMap<>();

        Factory(List<BotConfiguration> botConfigurations) {
            botDescriptions = botConfigurations.stream()
                    .collect(Collectors.toMap(BotConfiguration::getId, Function.identity()));

            createOutputs();
            createBots();
            linkDestinations();
            verify();
        }

        Collection<Bot> getBots() {
            return bots.values();
        }

        Map<Integer, Bot> getBotsById() {
            return Collections.unmodifiableMap(bots);
        }

        Map<Integer, Output> getOutputsById() {
            return Collections.unmodifiableMap(outputs);
        }

        private void createOutputs() {
            botDescriptions.forEach((id, description) -> {
                if (description.getLowOutputId() > -1) {
                    outputs.computeIfAbsent(description.getLowOutputId(), Output::new);
                }
                if (description.getHighOutputId() > -1) {
                    outputs.computeIfAbsent(description.getHighOutputId(), Output::new);
                }
            });
        }

        private void createBots() {
            botDescriptions.forEach((id, description) -> {
                bots.computeIfAbsent(id, Bot::new);

                if (description.getLowBotId() > -1) {
                    bots.computeIfAbsent(description.getLowBotId(), Bot::new);
                }

                if (description.getHighBotId() > -1) {
                    bots.computeIfAbsent(description.getHighBotId(), Bot::new);
                }
            });
        }

        private void linkDestinations() {
            botDescriptions.forEach((id, description) -> {
                final Destination low;
                final Destination high;

                if (description.getLowOutputId() > -1) {
                    low = outputs.get(description.getLowOutputId());
                } else {
                    low = bots.get(description.getLowBotId());
                }


                if (description.getHighOutputId() > -1) {
                    high = outputs.get(description.getHighOutputId());
                } else {
                    high = bots.get(description.getHighBotId());
                }

                bots.get(id).setLow(low);
                bots.get(id).setHigh(high);
            });
        }


        private void verify() {
            bots.values().forEach(bot -> {
                if (bot.getLow() == null) {
                    throw new IllegalStateException(String.format("Bot with id %d has no low destination.", bot.getId()));
                }

                if (bot.getHigh() == null) {
                    throw new IllegalStateException(String.format("Bot with id %d has no low destination.", bot.getId()));
                }
            });
        }


    }


    @Getter
    static class BotConfiguration {

        private static final Pattern CONFIGURATION_PATTERN = Pattern.compile("bot (\\d+) gives low to (bot|output) (\\d+) and high to (bot|output) (\\d+)");

        private final int id;

        private final int lowBotId;

        private final int highBotId;

        private final int lowOutputId;

        private final int highOutputId;

        BotConfiguration(String input) {
            Matcher matcher = CONFIGURATION_PATTERN.matcher(input);

            if (matcher.matches()) {
                id = Integer.parseInt(matcher.group(1));

                switch (matcher.group(2)) {
                    case "bot":
                        lowBotId = Integer.parseInt(matcher.group(3));
                        lowOutputId = -1;
                        break;
                    case "output":
                        lowBotId = -1;
                        lowOutputId = Integer.parseInt(matcher.group(3));
                        break;
                    default:
                        throw new IllegalArgumentException();
                }

                switch (matcher.group(4)) {
                    case "bot":
                        highBotId = Integer.parseInt(matcher.group(5));
                        highOutputId = -1;
                        break;
                    case "output":
                        highBotId = -1;
                        highOutputId = Integer.parseInt(matcher.group(5));
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            } else {
                throw new IllegalArgumentException(String.format("Invalid bot configuration: '%s'.", input));
            }
        }

        static boolean matchesConfigurationPattern(String input) {
            return CONFIGURATION_PATTERN.matcher(input).matches();
        }

    }

    public interface Destination {

        void accept(Chip chip);

    }


}
