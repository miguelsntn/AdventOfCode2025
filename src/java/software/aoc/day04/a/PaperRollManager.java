package software.aoc.day04.a;

import software.aoc.day04.PaperGrid;
import java.util.stream.IntStream;

public class PaperRollManager {

    public int countAccessibleRolls(PaperGrid grid) {
        return IntStream.range(0, grid.rows())
                .map(r -> (int) IntStream.range(0, grid.cols())
                        .filter(c -> grid.isPaperRoll(r, c) && countAdjacent(grid.grid(), r, c, grid.rows(), grid.cols()) < 4)
                        .count())
                .sum();
    }

    private int countAdjacent(boolean[][] grid, int r, int c, int rows, int cols) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int nr = r + dr;
                int nc = c + dc;
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc]) {
                    count++;
                }
            }
        }
        return count;
    }
}