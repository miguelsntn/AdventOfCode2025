package software.aoc.day04;

import java.util.List;

public record PaperGrid(boolean[][] grid, int rows, int cols) {

    public static PaperGrid from(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("La cuadrícula no puede ser nula o vacía");
        }

        int rows = lines.size();
        int cols = lines.get(0).length();
        boolean[][] parsedGrid = new boolean[rows][cols];

        for (int r = 0; r < rows; r++) {
            String line = lines.get(r);
            for (int c = 0; c < cols; c++) {
                parsedGrid[r][c] = line.charAt(c) == '@';
            }
        }

        return new PaperGrid(parsedGrid, rows, cols);
    }

    public boolean isPaperRoll(int row, int col) {
        return isValidPosition(row, col) && grid[row][col];
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public boolean[][] getMutableCopy() {
        boolean[][] copy = new boolean[rows][cols];
        for (int r = 0; r < rows; r++) {
            System.arraycopy(grid[r], 0, copy[r], 0, cols);
        }
        return copy;
    }
}