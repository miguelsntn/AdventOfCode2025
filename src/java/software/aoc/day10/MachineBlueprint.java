package software.aoc.day10;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record MachineBlueprint(List<Integer> lightTargets, List<Integer> joltageTargets, List<List<Integer>> buttons) {

    public static MachineBlueprint parse(String line) {
        return new MachineBlueprint(
                parseLights(line),
                parseJoltages(line),
                parseButtons(line)
        );
    }

    private static List<Integer> parseLights(String line) {
        int start = line.indexOf('[');
        int end = line.indexOf(']');
        if (start == -1 || end == -1) return List.of();

        List<Integer> lights = new ArrayList<>();
        for (char c : line.substring(start + 1, end).toCharArray()) {
            lights.add(c == '#' ? 1 : 0);
        }
        return List.copyOf(lights);
    }

    private static List<Integer> parseJoltages(String line) {
        int start = line.indexOf('{');
        int end = line.indexOf('}');
        if (start == -1 || end == -1) return List.of();

        return Arrays.stream(line.substring(start + 1, end).split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();
    }

    private static List<List<Integer>> parseButtons(String line) {
        List<List<Integer>> buttons = new ArrayList<>();

        Matcher matcher = Pattern.compile("\\(([^)]+)\\)").matcher(line);
        while (matcher.find()) {
            String content = matcher.group(1).trim();
            if (!content.isEmpty()) {
                List<Integer> buttonWiring = Arrays.stream(content.split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .toList();
                buttons.add(buttonWiring);
            }
        }
        return List.copyOf(buttons);
    }
}