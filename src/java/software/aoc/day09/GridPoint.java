package software.aoc.day09;

public record GridPoint(long x, long y) {

    public static GridPoint fromString(String line) {
        String[] parts = line.split(",");
        return new GridPoint(
                Long.parseLong(parts[0].trim()),
                Long.parseLong(parts[1].trim())
        );
    }

    public long calculateAreaTo(GridPoint other) {
        long width = Math.abs(this.x - other.x) + 1;
        long height = Math.abs(this.y - other.y) + 1;
        return width * height;
    }
}