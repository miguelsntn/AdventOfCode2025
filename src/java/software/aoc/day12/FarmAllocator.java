package software.aoc.day12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FarmAllocator {
    private final Map<Integer, PresentShape> catalog;

    public FarmAllocator(Map<Integer, PresentShape> catalog) {
        this.catalog = catalog;
    }

    public int countFittingRegions(List<TreeRegion> regions) {
        int fitCount = 0;
        for (TreeRegion region : regions) {
            if (canFit(region)) {
                fitCount++;
            }
        }
        return fitCount;
    }

    private boolean canFit(TreeRegion region) {
        List<Integer> piecesToPlace = new ArrayList<>();
        int totalArea = 0;

        for (int i = 0; i < region.pieceCounts().length; i++) {
            int count = region.pieceCounts()[i];
            for (int k = 0; k < count; k++) {
                piecesToPlace.add(i);
                totalArea += catalog.get(i).getArea();
            }
        }

        if (totalArea > region.getArea()) return false;

        piecesToPlace.sort((a, b) -> {
            int areaA = catalog.get(a).getArea();
            int areaB = catalog.get(b).getArea();
            return areaA != areaB ? Integer.compare(areaB, areaA) : Integer.compare(a, b);
        });

        long[] grid = new long[region.height()];
        Set<String> failedStates = new HashSet<>();

        return solve(0, -1, grid, region.width(), region.height(), piecesToPlace, failedStates);
    }

    private boolean solve(int pieceIdx, int lastPlacementId, long[] grid, int width, int height, List<Integer> pieces, Set<String> failedStates) {
        if (pieceIdx == pieces.size()) return true;

        String stateKey = pieceIdx + "_" + Arrays.toString(grid);
        if (failedStates.contains(stateKey)) return false;

        int shapeId = pieces.get(pieceIdx);
        PresentShape shape = catalog.get(shapeId);

        for (int v = 0; v < shape.getVariations().size(); v++) {
            ShapeVariation var = shape.getVariations().get(v);
            if (var.h() > height || var.w() > width) continue;

            for (int r = 0; r <= height - var.h(); r++) {
                for (int c = 0; c <= width - var.w(); c++) {
                    int placementId = (r * width + c) * 10 + v;

                    if (pieceIdx > 0 && pieces.get(pieceIdx).equals(pieces.get(pieceIdx - 1))) {
                        if (placementId <= lastPlacementId) continue;
                    }

                    if (canPlace(grid, var, r, c)) {
                        place(grid, var, r, c);

                        if (solve(pieceIdx + 1, placementId, grid, width, height, pieces, failedStates)) {
                            return true;
                        }

                        place(grid, var, r, c);
                    }
                }
            }
        }

        failedStates.add(stateKey);
        return false;
    }

    private boolean canPlace(long[] grid, ShapeVariation var, int r, int c) {
        for (int i = 0; i < var.h(); i++) {
            if ((grid[r + i] & (var.masks()[i] << c)) != 0) {
                return false;
            }
        }
        return true;
    }

    private void place(long[] grid, ShapeVariation var, int r, int c) {
        for (int i = 0; i < var.h(); i++) {
            grid[r + i] ^= (var.masks()[i] << c);
        }
    }
}