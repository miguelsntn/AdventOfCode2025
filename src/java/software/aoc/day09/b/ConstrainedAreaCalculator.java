package software.aoc.day09.b;

import software.aoc.day09.GridPoint;
import software.aoc.day09.PolygonEdge;

import java.util.List;
import java.util.stream.IntStream;

public class ConstrainedAreaCalculator {
    private final List<GridPoint> points;
    private final List<PolygonEdge> edges;

    public ConstrainedAreaCalculator(List<GridPoint> points) {
        this.points = List.copyOf(points);

        this.edges = IntStream.range(0, points.size())
                .mapToObj(i -> new PolygonEdge(points.get(i), points.get((i + 1) % points.size())))
                .toList();
    }

    public long findLargestValidArea() {
        long maxArea = 0;

        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                GridPoint p1 = points.get(i);
                GridPoint p2 = points.get(j);
                long area = p1.calculateAreaTo(p2);

                if (area > maxArea && isValidRectangle(p1, p2)) {
                    maxArea = area;
                }
            }
        }
        return maxArea;
    }

    private boolean isValidRectangle(GridPoint p1, GridPoint p2) {
        long xMin = Math.min(p1.x(), p2.x());
        long xMax = Math.max(p1.x(), p2.x());
        long yMin = Math.min(p1.y(), p2.y());
        long yMax = Math.max(p1.y(), p2.y());

        if (containsAnyVertex(xMin, xMax, yMin, yMax)) return false;
        if (isIntersectedByEdges(xMin, xMax, yMin, yMax)) return false;

        return isEntirelyInsidePolygon(xMin, xMax, yMin, yMax);
    }

    private boolean containsAnyVertex(long xMin, long xMax, long yMin, long yMax) {
        return points.stream().anyMatch(v -> {
            if (xMin < xMax && yMin < yMax) return v.x() > xMin && v.x() < xMax && v.y() > yMin && v.y() < yMax;
            if (xMin < xMax && yMin == yMax) return v.x() > xMin && v.x() < xMax && v.y() == yMin;
            if (xMin == xMax && yMin < yMax) return v.x() == xMin && v.y() > yMin && v.y() < yMax;
            return false;
        });
    }

    private boolean isIntersectedByEdges(long xMin, long xMax, long yMin, long yMax) {
        for (PolygonEdge e : edges) {
            if (xMin < xMax && yMin < yMax) {
                if (e.isHorizontal() && e.start().y() > yMin && e.start().y() < yMax && Math.max(xMin, e.minX()) < Math.min(xMax, e.maxX())) return true;
                if (e.isVertical() && e.start().x() > xMin && e.start().x() < xMax && Math.max(yMin, e.minY()) < Math.min(yMax, e.maxY())) return true;
            } else if (xMin < xMax && yMin == yMax) {
                if (e.isVertical() && e.start().x() > xMin && e.start().x() < xMax && e.minY() < yMin && yMin < e.maxY()) return true;
            } else if (xMin == xMax && yMin < yMax) {
                if (e.isHorizontal() && e.start().y() > yMin && e.start().y() < yMax && e.minX() < xMin && xMin < e.maxX()) return true;
            }
        }
        return false;
    }

    private boolean isEntirelyInsidePolygon(long xMin, long xMax, long yMin, long yMax) {
        if (xMin == xMax && yMin == yMax) return true;
        if (xMin < xMax && yMin < yMax) return rayCastInside(xMin + 0.5, yMin + 0.5);

        if (xMin < xMax && yMin == yMax) {
            for (PolygonEdge e : edges) {
                if (e.isHorizontal() && e.start().y() == yMin && e.minX() <= xMin + 0.5 && xMin + 0.5 <= e.maxX()) return true;
            }
            return rayCastInside(xMin + 0.5, yMin + 0.1);
        }

        for (PolygonEdge e : edges) {
            if (e.isVertical() && e.start().x() == xMin && e.minY() <= yMin + 0.5 && yMin + 0.5 <= e.maxY()) return true;
        }
        return rayCastInside(xMin + 0.1, yMin + 0.5);
    }

    private boolean rayCastInside(double px, double py) {
        long crossings = edges.stream()
                .filter(PolygonEdge::isVertical)
                .filter(e -> e.start().x() > px && e.minY() < py && py < e.maxY())
                .count();
        return (crossings % 2) != 0;
    }
}