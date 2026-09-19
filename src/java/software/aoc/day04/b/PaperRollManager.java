package software.aoc.day04.b;

import software.aoc.day04.PaperGrid;
import java.util.List;
import java.util.stream.IntStream;

public class PaperRollManager {

    public int removeAllAccessibleRolls(PaperGrid originalGrid) {
        char[][] mutableGrid = originalGrid.getDeepCopy();
        int rows = originalGrid.rows;
        int cols = originalGrid.cols;

        int totalRemoved = 0;
        boolean removedInCurrentPass;

        int[] dRow = {-1, -1, -1,  0, 0,  1, 1, 1};
        int[] dCol = {-1,  0,  1, -1, 1, -1, 0, 1};

        do {
            List<int[]> rollsToRemove = IntStream.range(0, rows).boxed()
                    .flatMap(r -> IntStream.range(0, cols)
                            .filter(c -> mutableGrid[r][c] == '@')
                            .filter(c -> {
                                int adj = 0;
                                for (int i = 0; i < 8; i++) {
                                    int nr = r + dRow[i];
                                    int nc = c + dCol[i];
                                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && mutableGrid[nr][nc] == '@') {
                                        adj++;
                                    }
                                }
                                return adj < 4;
                            })
                            .mapToObj(c -> new int[]{r, c}))
                    .toList();

            removedInCurrentPass = !rollsToRemove.isEmpty();

            for (int[] pos : rollsToRemove) {
                mutableGrid[pos[0]][pos[1]] = '.';
            }

            totalRemoved += rollsToRemove.size();

        } while (removedInCurrentPass);

        return totalRemoved;
    }
}