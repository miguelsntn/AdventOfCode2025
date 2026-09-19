package software.aoc.day04.a;

import software.aoc.day04.PaperGrid;
import java.util.stream.IntStream;

public class PaperRollManager {

    public int countAccessibleRolls(PaperGrid grid) {
        return IntStream.range(0, grid.rows)
                .map(r -> (int) IntStream.range(0, grid.cols)
                        .filter(c -> grid.isPaperRoll(r, c) && countAdjacentRolls(grid, r, c) < 4)
                        .count())
                .sum();
    }

    private int countAdjacentRolls(PaperGrid grid, int row, int col) {
        int count = 0;
        int[] dRow = {-1, -1, -1,  0, 0,  1, 1, 1};
        int[] dCol = {-1,  0,  1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int newRow = row + dRow[i];
            int newCol = col + dCol[i];

            if (grid.isValidPosition(newRow, newCol) && grid.isPaperRoll(newRow, newCol)) {
                count++;
            }
        }
        return count;
    }
}