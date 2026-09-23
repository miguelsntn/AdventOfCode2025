package software.aoc.day04.b;

import software.aoc.day04.PaperGrid;

public class PaperRollManager {

    public int removeAllAccessibleRolls(PaperGrid originalGrid) {
        boolean[][] grid = originalGrid.getMutableCopy();
        int rows = originalGrid.rows();
        int cols = originalGrid.cols();

        int totalRemoved = 0;
        boolean removedInCurrentPass;

        boolean[][] toRemove = new boolean[rows][cols];

        do {
            removedInCurrentPass = false;

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (grid[r][c] && countAdjacent(grid, r, c, rows, cols) < 4) {
                        toRemove[r][c] = true;
                        removedInCurrentPass = true;
                        totalRemoved++;
                    }
                }
            }

            if (removedInCurrentPass) {
                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < cols; c++) {
                        if (toRemove[r][c]) {
                            grid[r][c] = false;
                            toRemove[r][c] = false; // Reinicio de la máscara
                        }
                    }
                }
            }

        } while (removedInCurrentPass);

        return totalRemoved;
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