package software.aoc.day04.b;

import java.util.ArrayList;
import java.util.List;

public class PaperGrid {
    private final char[][] grid;
    private final int rows;
    private final int cols;

    private PaperGrid(char[][] grid) {
        this.grid = grid;
        this.rows = grid.length;
        this.cols = grid.length > 0 ? grid[0].length : 0;
    }

    public static PaperGrid from(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("La cuadricula no puede ser nula o vacia");
        }

        int rows = lines.size();
        int cols = lines.get(0).length();
        char[][] gridState = new char[rows][cols];

        for (int r = 0; r < rows; r++) {
            gridState[r] = lines.get(r).toCharArray();
        }

        return new PaperGrid(gridState);
    }

    public int removeAllAccessibleRolls() {
        int totalRemoved = 0;
        boolean removedInCurrentPass;

        do {
            List<int[]> rollsToRemove = new ArrayList<>();

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (isPaperRoll(r, c) && countAdjacentRolls(r, c) < 4) {
                        rollsToRemove.add(new int[]{r, c});
                    }
                }
            }

            removedInCurrentPass = !rollsToRemove.isEmpty();

            for (int[] pos : rollsToRemove) {
                grid[pos[0]][pos[1]] = '.';
            }

            totalRemoved += rollsToRemove.size();

        } while (removedInCurrentPass);

        return totalRemoved;
    }

    private boolean isPaperRoll(int row, int col) {
        return grid[row][col] == '@';
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