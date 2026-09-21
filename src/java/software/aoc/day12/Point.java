package software.aoc.day12;

public record Point(int r, int c) {
    public Point rotate() {
        return new Point(c, -r);
    }

    public Point flip() {
        return new Point(r, -c);
    }
}