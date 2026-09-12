package software.aoc.day10.a;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Machine {
    private final long targetState;
    private final List<Long> buttons;

    private Machine(long targetState, List<Long> buttons) {
        this.targetState = targetState;
        this.buttons = List.copyOf(buttons);
    }

    public static Machine parse(String line) {
        int bracketStart = line.indexOf('[');
        int bracketEnd = line.indexOf(']');
        if (bracketStart == -1 || bracketEnd == -1) {
            throw new IllegalArgumentException("Formato de maquina invalido: " + line);
        }

        String targetStr = line.substring(bracketStart + 1, bracketEnd);
        long target = 0;
        for (int i = 0; i < targetStr.length(); i++) {
            if (targetStr.charAt(i) == '#') {
                target |= (1L << i);
            }
        }

        List<Long> parsedButtons = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\(([^)]+)\\)").matcher(line);

        while (matcher.find()) {
            String content = matcher.group(1).trim();
            long mask = 0;
            if (!content.isEmpty()) {
                String[] parts = content.split(",");
                for (String part : parts) {
                    mask |= (1L << Integer.parseInt(part.trim()));
                }
            }
            parsedButtons.add(mask);
        }

        return new Machine(target, parsedButtons);
    }

    public int getMinPresses() {
        int result = findMinPresses(0, 0L, 0, Integer.MAX_VALUE);
        if (result == Integer.MAX_VALUE) {
            throw new IllegalStateException("No se encontro solucion para la maquina.");
        }
        return result;
    }

    private int findMinPresses(int buttonIndex, long currentState, int currentPresses, int bestSoFar) {
        if (currentPresses >= bestSoFar) {
            return bestSoFar;
        }

        if (buttonIndex == buttons.size()) {
            if (currentState == targetState) {
                return currentPresses;
            }
            return bestSoFar;
        }

        bestSoFar = findMinPresses(buttonIndex + 1, currentState, currentPresses, bestSoFar);

        bestSoFar = findMinPresses(buttonIndex + 1, currentState ^ buttons.get(buttonIndex), currentPresses + 1, bestSoFar);

        return bestSoFar;
    }
}