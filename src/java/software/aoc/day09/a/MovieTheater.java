package software.aoc.day09.a;

import java.util.ArrayList;
import java.util.List;

public class MovieTheater {
    private final List<Coordinate> redTiles;

    private MovieTheater(List<Coordinate> redTiles) {
        this.redTiles = List.copyOf(redTiles);
    }

    public static MovieTheater from(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("La lista de coordenadas no puede estar vacia");
        }

        List<Coordinate> parsedTiles = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",");
            long x = Long.parseLong(parts[0].trim());
            long y = Long.parseLong(parts[1].trim());
            parsedTiles.add(new Coordinate(x, y));
        }

        return new MovieTheater(parsedTiles);
    }

    public long findLargestRectangleArea() {
        long maxArea = 0;

        for (int i = 0; i < redTiles.size(); i++) {
            for (int j = i + 1; j < redTiles.size(); j++) {
                long currentArea = calculateArea(redTiles.get(i), redTiles.get(j));
                if (currentArea > maxArea) {
                    maxArea = currentArea;
                }
            }
        }

        return maxArea;
    }

    private long calculateArea(Coordinate p1, Coordinate p2) {
        long width = Math.abs(p1.x - p2.x) + 1;
        long height = Math.abs(p1.y - p2.y) + 1;
        return width * height;
    }

    private static class Coordinate {
        final long x;
        final long y;

        Coordinate(long x, long y) {
            this.x = x;
            this.y = y;
        }
    }
}