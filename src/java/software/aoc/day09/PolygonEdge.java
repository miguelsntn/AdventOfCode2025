package software.aoc.day09;

public record PolygonEdge(GridPoint start, GridPoint end) {

    public boolean isHorizontal() {
        return start.y() == end.y();
    }

    public boolean isVertical() {
        return start.x() == end.x();
    }

    public long minX() { return Math.min(start.x(), end.x()); }
    public long maxX() { return Math.max(start.x(), end.x()); }
    public long minY() { return Math.min(start.y(), end.y()); }
    public long maxY() { return Math.max(start.y(), end.y()); }
}