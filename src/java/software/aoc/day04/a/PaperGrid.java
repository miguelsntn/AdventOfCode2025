package software.aoc.day04.a;

import java.util.List;

public class PaperGrid {
    private final List<String> grid;
    private final int rows;
    private final int cols;

    private PaperGrid(List<String> grid) {
        this.grid = List.copyOf(grid);
        this.rows = grid.size();
        this.cols = grid.isEmpty() ? 0 : grid.get(0).length();
    }

    public static PaperGrid from(List<String> lines) {
        if (lines == null) {
            throw new IllegalArgumentException("La cuadricula no puede ser nula");
        }
        return new PaperGrid(lines);
    }

    public int countAccessibleRolls() {
        int accessibleCount = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (isPaperRoll(r, c) && countAdjacentRolls(r, c) < 4) {
                    accessibleCount++;
                }
            }
        }

        return accessibleCount;
    }

    private boolean isPaperRoll(int row, int col) {
        return grid.get(row).charAt(col) == '@';
    }

    private int countAdjacentRolls(int row, int col) {
        int count = 0;
        int[] dRow = {-1, -1, -1,  0, 0,  1, 1, 1};
        int[] dCol = {-1,  0,  1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int newRow = row + dRow[i];
            int newCol = col + dCol[i];

            if (isValidPosition(newRow, newCol) && isPaperRoll(newRow, newCol)) {
                count++;
            }
        }

        return count;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }
}