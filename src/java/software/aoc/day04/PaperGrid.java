package software.aoc.day04;

import java.util.List;

public class PaperGrid {
    private final char[][] grid;
    public final int rows;
    public final int cols;

    private PaperGrid(char[][] grid) {
        this.grid = grid;
        this.rows = grid.length;
        this.cols = grid.length > 0 ? grid[0].length : 0;
    }

    public static PaperGrid from(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("La cuadricula no puede ser nula o vacia");
        }

        char[][] gridState = new char[lines.size()][lines.get(0).length()];
        for (int r = 0; r < lines.size(); r++) {
            gridState[r] = lines.get(r).toCharArray();
        }

        return new PaperGrid(gridState);
    }

    public boolean isPaperRoll(int row, int col) {
        return grid[row][col] == '@';
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public char[][] getDeepCopy() {
        char[][] copy = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            System.arraycopy(grid[r], 0, copy[r], 0, cols);
        }
        return copy;
    }
}