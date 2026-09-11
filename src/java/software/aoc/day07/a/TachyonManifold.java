package software.aoc.day07.a;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public long countTotalSplits() {
        if (rows == 0) return 0;

        Set<Integer> activeBeams = new HashSet<>();
        int startRow = 0;

        outer:
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid.get(r).charAt(c) == 'S') {
                    activeBeams.add(c);
                    startRow = r;
                    break outer;
                }
            }
        }

        long totalSplits = 0;

        for (int r = startRow + 1; r < rows; r++) {
            Set<Integer> nextBeams = new HashSet<>();
            String currentRow = grid.get(r);

            for (int beamCol : activeBeams) {
                if (beamCol >= 0 && beamCol < cols) {
                    if (currentRow.charAt(beamCol) == '^') {
                        totalSplits++;
                        nextBeams.add(beamCol - 1);
                        nextBeams.add(beamCol + 1);
                    } else {
                        nextBeams.add(beamCol);
                    }
                }
            }

            activeBeams = nextBeams;
        }

        return totalSplits;
    }
}