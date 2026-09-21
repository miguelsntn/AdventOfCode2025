package software.aoc.day12;

import java.util.Arrays;
import java.util.List;

public record ShapeVariation(int w, int h, long[] masks) {

    public static ShapeVariation from(List<Point> points) {
        int maxR = 0, maxC = 0;
        for (Point p : points) {
            maxR = Math.max(maxR, p.r());
            maxC = Math.max(maxC, p.c());
        }

        int height = maxR + 1;
        int width = maxC + 1;
        long[] rowMasks = new long[height];

        for (Point p : points) {
            rowMasks[p.r()] |= (1L << p.c());
        }

        return new ShapeVariation(width, height, rowMasks);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShapeVariation that)) return false;
        return w == that.w && h == that.h && Arrays.equals(masks, that.masks);
    }

    @Override
    public int hashCode() {
        int result = w;
        result = 31 * result + h;
        result = 31 * result + Arrays.hashCode(masks);
        return result;
    }
}