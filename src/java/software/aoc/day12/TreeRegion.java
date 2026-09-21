package software.aoc.day12;

public record TreeRegion(int width, int height, int[] pieceCounts) {
    public int getArea() {
        return width * height;
    }
}