package software.aoc.day09.b;

import java.util.ArrayList;
import java.util.List;

public class MovieTheater {
    private final List<Coordinate> redTiles;
    private final List<Edge> horizontalEdges;
    private final List<Edge> verticalEdges;

    private MovieTheater(List<Coordinate> redTiles) {
        this.redTiles = List.copyOf(redTiles);
        this.horizontalEdges = new ArrayList<>();
        this.verticalEdges = new ArrayList<>();

        for (int i = 0; i < redTiles.size(); i++) {
            Coordinate p1 = redTiles.get(i);
            Coordinate p2 = redTiles.get((i + 1) % redTiles.size());

            Edge e = new Edge(p1, p2);
            if (e.isHorizontal) {
                horizontalEdges.add(e);
            } else {
                verticalEdges.add(e);
            }
        }
    }

    public static MovieTheater from(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("La lista de coordenadas no puede estar vacia");
        }

        List<Coordinate> parsedTiles = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",");
            long x = Long.parseLong(parts[0].trim());
            long y = Long.parseLong(parts[1].trim());
            parsedTiles.add(new Coordinate(x, y));
        }

        return new MovieTheater(parsedTiles);
    }

    public long findLargestValidRectangleArea() {
        long maxArea = 0;
        int n = redTiles.size();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Coordinate p1 = redTiles.get(i);
                Coordinate p2 = redTiles.get(j);

                long width = Math.abs(p1.x - p2.x) + 1;
                long height = Math.abs(p1.y - p2.y) + 1;
                long area = width * height;

                if (area <= maxArea) {
                    continue;
                }

                if (isValidRectangle(p1, p2)) {
                    maxArea = area;
                }
            }
        }

        return maxArea;
    }

    private boolean isValidRectangle(Coordinate p1, Coordinate p2) {
        long xMin = Math.min(p1.x, p2.x);
        long xMax = Math.max(p1.x, p2.x);
        long yMin = Math.min(p1.y, p2.y);
        long yMax = Math.max(p1.y, p2.y);

        for (Coordinate v : redTiles) {
            boolean vertexInside = false;
            if (xMin < xMax && yMin < yMax) {
                vertexInside = (v.x > xMin && v.x < xMax && v.y > yMin && v.y < yMax);
            } else if (xMin < xMax && yMin == yMax) {
                vertexInside = (v.x > xMin && v.x < xMax && v.y == yMin);
            } else if (xMin == xMax && yMin < yMax) {
                vertexInside = (v.x == xMin && v.y > yMin && v.y < yMax);
            }

            if (vertexInside) return false;
        }

        if (xMin < xMax && yMin < yMax) {
            for (Edge e : horizontalEdges) {
                if (e.fixedCoord > yMin && e.fixedCoord < yMax && Math.max(xMin, e.minBoundary) < Math.min(xMax, e.maxBoundary)) return false;
            }
            for (Edge e : verticalEdges) {
                if (e.fixedCoord > xMin && e.fixedCoord < xMax && Math.max(yMin, e.minBoundary) < Math.min(yMax, e.maxBoundary)) return false;
            }
        } else if (xMin < xMax && yMin == yMax) {
            for (Edge e : verticalEdges) {
                if (e.fixedCoord > xMin && e.fixedCoord < xMax && e.minBoundary < yMin && yMin < e.maxBoundary) return false;
            }
        } else if (xMin == xMax && yMin < yMax) {
            for (Edge e : horizontalEdges) {
                if (e.fixedCoord > yMin && e.fixedCoord < yMax && e.minBoundary < xMin && xMin < e.maxBoundary) return false;
            }
        }

        if (xMin == xMax && yMin == yMax) return true;

        if (xMin < xMax && yMin < yMax) {
            return isPointInside(xMin + 0.5, yMin + 0.5);
        } else if (xMin < xMax && yMin == yMax) {
            for (Edge e : horizontalEdges) {
                if (e.fixedCoord == yMin && e.minBoundary <= xMin + 0.5 && xMin + 0.5 <= e.maxBoundary) return true;
            }
            return isPointInside(xMin + 0.5, yMin + 0.1);
        } else if (xMin == xMax && yMin < yMax) {
            for (Edge e : verticalEdges) {
                if (e.fixedCoord == xMin && e.minBoundary <= yMin + 0.5 && yMin + 0.5 <= e.maxBoundary) return true;
            }
            return isPointInside(xMin + 0.1, yMin + 0.5);
        }

        return false;
    }

    private boolean isPointInside(double px, double py) {
        int crossings = 0;
        for (Edge e : verticalEdges) {
            if (e.fixedCoord > px && e.minBoundary < py && py < e.maxBoundary) {
                crossings++;
            }
        }
        return (crossings % 2) != 0;
    }

    private static class Coordinate {
        final long x, y;
        Coordinate(long x, long y) { this.x = x; this.y = y; }
    }

    private static class Edge {
        final boolean isHorizontal;
        final long minBoundary, maxBoundary;
        final long fixedCoord;

        Edge(Coordinate p1, Coordinate p2) {
            if (p1.y == p2.y) {
                isHorizontal = true;
                fixedCoord = p1.y;
                minBoundary = Math.min(p1.x, p2.x);
                maxBoundary = Math.max(p1.x, p2.x);
            } else if (p1.x == p2.x) {
                isHorizontal = false;
                fixedCoord = p1.x;
                minBoundary = Math.min(p1.y, p2.y);
                maxBoundary = Math.max(p1.y, p2.y);
            } else {
                throw new IllegalArgumentException("El poligono debe ser puramente ortogonal.");
            }
        }
    }
}