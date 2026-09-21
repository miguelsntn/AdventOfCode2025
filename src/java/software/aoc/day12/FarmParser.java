package software.aoc.day12;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FarmParser {

    public static ParsedFarm parse(List<String> lines) {
        Map<Integer, PresentShape> shapes = new HashMap<>();
        List<TreeRegion> regions = new ArrayList<>();

        int currentId = -1;
        List<Point> currentPoints = new ArrayList<>();
        int rowIdx = 0;

        for (String line : lines) {
            if (line.isBlank()) continue;

            if (line.contains("x") && line.contains(":")) {
                regions.add(parseRegion(line));
            } else if (line.matches("\\d+:")) {
                if (currentId != -1) {
                    shapes.put(currentId, new PresentShape(currentId, currentPoints));
                }
                currentId = Integer.parseInt(line.substring(0, line.length() - 1));
                currentPoints = new ArrayList<>();
                rowIdx = 0;
            } else {
                for (int c = 0; c < line.length(); c++) {
                    if (line.charAt(c) == '#') {
                        currentPoints.add(new Point(rowIdx, c));
                    }
                }
                rowIdx++;
            }
        }

        if (currentId != -1) {
            shapes.put(currentId, new PresentShape(currentId, currentPoints));
        }

        return new ParsedFarm(shapes, regions);
    }

    private static TreeRegion parseRegion(String line) {
        String[] parts = line.split(":");
        String[] dims = parts[0].trim().split("x");
        int w = Integer.parseInt(dims[0]);
        int h = Integer.parseInt(dims[1]);

        String[] countTokens = parts[1].trim().split("\\s+");
        int[] counts = new int[countTokens.length];
        for (int i = 0; i < countTokens.length; i++) {
            counts[i] = Integer.parseInt(countTokens[i]);
        }
        return new TreeRegion(w, h, counts);
    }

    public record ParsedFarm(Map<Integer, PresentShape> shapes, List<TreeRegion> regions) {}
}