package software.aoc.day07.b;

import java.util.List;

public class TachyonManifold {
    private final List<String> grid;
    private final int rows;
    private final int cols;

    private TachyonManifold(List<String> grid) {
        this.grid = List.copyOf(grid);
        this.rows = grid.size();
        this.cols = grid.isEmpty() ? 0 : grid.get(0).length();
    }

    public static TachyonManifold from(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("El diagrama del colector no puede estar vacio");
        }
        return new TachyonManifold(lines);
    }

    public long countQuantumTimelines() {
        if (rows == 0) return 0;

        int startRow = -1;
        int startCol = -1;

        outer:
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid.get(r).charAt(c) == 'S') {
                    startRow = r;
                    startCol = c;
                    break outer;
                }
            }
        }

        long[] timelines = new long[cols];
        timelines[startCol] = 1;

        for (int r = startRow + 1; r < rows; r++) {
            long[] nextTimelines = new long[cols];
            String currentRow = grid.get(r);

            for (int c = 0; c < cols; c++) {
                if (timelines[c] > 0) {
                    if (currentRow.charAt(c) == '^') {
                        if (c - 1 >= 0) nextTimelines[c - 1] += timelines[c];
                        if (c + 1 < cols) nextTimelines[c + 1] += timelines[c];
                    } else {
                        nextTimelines[c] += timelines[c];
                    }
                }
            }

            timelines = nextTimelines;
        }
        long totalTimelines = 0;
        for (long count : timelines) {
            totalTimelines += count;
        }

        return totalTimelines;
    }
}