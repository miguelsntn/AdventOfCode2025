package software.aoc.day08.b;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CircuitSystem {
    private final List<Point3D> points;

    private CircuitSystem(List<Point3D> points) {
        this.points = List.copyOf(points);
    }

    public static CircuitSystem from(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("La lista de coordenadas no puede estar vacia");
        }

        List<Point3D> parsedPoints = new ArrayList<>();
        int id = 0;

        for (String line : lines) {
            String[] parts = line.split(",");
            int x = Integer.parseInt(parts[0].trim());
            int y = Integer.parseInt(parts[1].trim());
            int z = Integer.parseInt(parts[2].trim());
            parsedPoints.add(new Point3D(id++, x, y, z));
        }

        return new CircuitSystem(parsedPoints);
    }

    public long solveForFinalConnection() {
        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                edges.add(new Edge(points.get(i), points.get(j)));
            }
        }

        Collections.sort(edges);

        DisjointSet dsu = new DisjointSet(points.size());

        for (Edge e : edges) {
            if (dsu.union(e.p1.id, e.p2.id)) {
                if (dsu.getComponents() == 1) {
                    return (long) e.p1.x * e.p2.x;
                }
            }
        }

        throw new IllegalStateException("No se pudo formar un circuito unico con las cajas dadas.");
    }

    private static class Point3D {
        final int id, x, y, z;
        Point3D(int id, int x, int y, int z) {
            this.id = id; this.x = x; this.y = y; this.z = z;
        }
    }

    private static class Edge implements Comparable<Edge> {
        final Point3D p1, p2;
        final long squaredDistance;

        Edge(Point3D p1, Point3D p2) {
            this.p1 = p1;
            this.p2 = p2;

            long dx = p1.x - p2.x;
            long dy = p1.y - p2.y;
            long dz = p1.z - p2.z;
            this.squaredDistance = dx * dx + dy * dy + dz * dz;
        }

        @Override
        public int compareTo(Edge other) {
            return Long.compare(this.squaredDistance, other.squaredDistance);
        }
    }

    private static class DisjointSet {
        private final int[] parent;
        private final int[] size;
        private int components;

        DisjointSet(int n) {
            parent = new int[n];
            size = new int[n];
            components = n;
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int i) {
            if (parent[i] == i) return i;
            return parent[i] = find(parent[i]);
        }

        boolean union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);

            if (rootI != rootJ) {
                if (size[rootI] < size[rootJ]) {
                    parent[rootI] = rootJ;
                    size[rootJ] += size[rootI];
                } else {
                    parent[rootJ] = rootI;
                    size[rootI] += size[rootJ];
                }
                components--;
                return true;
            }
            return false;
        }

        int getComponents() {
            return components;
        }
    }
}