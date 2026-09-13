package software.aoc.day12.a;

import java.util.*;

public class ChristmasTreeFarm {
    private final Map<Integer, Shape> shapes;
    private final List<Region> regions;

    private ChristmasTreeFarm(Map<Integer, Shape> shapes, List<Region> regions) {
        this.shapes = shapes;
        this.regions = regions;
    }

    public static ChristmasTreeFarm from(List<String> lines) {
        Map<Integer, Shape> parsedShapes = new HashMap<>();
        List<Region> parsedRegions = new ArrayList<>();

        Shape currentShape = null;
        boolean parsingRegions = false;

        for (String line : lines) {
            if (line.isBlank()) continue;

            if (line.contains("x") && line.contains(":")) {
                parsingRegions = true;
            }

            if (parsingRegions) {
                parsedRegions.add(Region.parse(line));
            } else {
                if (line.matches("\\d+:")) {
                    currentShape = new Shape();
                    currentShape.id = Integer.parseInt(line.substring(0, line.length() - 1));
                    parsedShapes.put(currentShape.id, currentShape);
                } else if (currentShape != null) {
                    currentShape.lines.add(line);
                }
            }
        }

        for (Shape s : parsedShapes.values()) {
            s.parseVariations();
        }

        return new ChristmasTreeFarm(parsedShapes, parsedRegions);
    }

    public int countFittingRegions() {
        int fitCount = 0;
        for (Region region : regions) {
            if (canFit(region)) {
                fitCount++;
            }
        }
        return fitCount;
    }

    private boolean canFit(Region region) {
        List<Integer> piecesToPlace = new ArrayList<>();
        for (int i = 0; i < region.pieceCounts.length; i++) {
            int count = region.pieceCounts[i];
            for (int k = 0; k < count; k++) {
                piecesToPlace.add(i);
            }
        }

        int totalArea = 0;
        for (int pId : piecesToPlace) {
            totalArea += shapes.get(pId).area;
        }
        if (totalArea > region.w * region.h) return false;

        piecesToPlace.sort((a, b) -> {
            int areaA = shapes.get(a).area;
            int areaB = shapes.get(b).area;
            if (areaA != areaB) return Integer.compare(areaB, areaA);
            return Integer.compare(a, b);
        });

        long[] grid = new long[region.h];
        return solve(0, -1, grid, region.w, region.h, piecesToPlace);
    }

    private boolean solve(int pieceIdx, int lastPlacementId, long[] grid, int W, int H, List<Integer> pieces) {
        if (pieceIdx == pieces.size()) return true;

        int pId = pieces.get(pieceIdx);
        Shape shape = shapes.get(pId);

        for (int v = 0; v < shape.variations.size(); v++) {
            Variation var = shape.variations.get(v);
            if (var.h > H || var.w > W) continue;

            for (int r = 0; r <= H - var.h; r++) {
                for (int c = 0; c <= W - var.w; c++) {
                    int placementId = (r * W + c) * 10 + v;

                    if (pieceIdx > 0 && pieces.get(pieceIdx).equals(pieces.get(pieceIdx - 1))) {
                        if (placementId <= lastPlacementId) continue;
                    }

                    boolean fits = true;
                    for (int i = 0; i < var.h; i++) {
                        if ((grid[r + i] & (var.masks[i] << c)) != 0) {
                            fits = false;
                            break;
                        }
                    }

                    if (fits) {
                        for (int i = 0; i < var.h; i++) {
                            grid[r + i] ^= (var.masks[i] << c);
                        }

                        if (solve(pieceIdx + 1, placementId, grid, W, H, pieces)) return true;

                        for (int i = 0; i < var.h; i++) {
                            grid[r + i] ^= (var.masks[i] << c);
                        }
                    }
                }
            }
        }
        return false;
    }

    private static class Region {
        final int w, h;
        final int[] pieceCounts;

        Region(int w, int h, int[] pieceCounts) {
            this.w = w;
            this.h = h;
            this.pieceCounts = pieceCounts;
        }

        static Region parse(String line) {
            String[] parts = line.split(":");
            String[] dims = parts[0].trim().split("x");
            int w = Integer.parseInt(dims[0]);
            int h = Integer.parseInt(dims[1]);

            String[] countTokens = parts[1].trim().split("\\s+");
            int[] counts = new int[countTokens.length];
            for (int i = 0; i < countTokens.length; i++) {
                counts[i] = Integer.parseInt(countTokens[i]);
            }
            return new Region(w, h, counts);
        }
    }

    private static class Shape {
        int id;
        List<String> lines = new ArrayList<>();
        List<Variation> variations = new ArrayList<>();
        int area;

        void parseVariations() {
            List<Point> initialPts = new ArrayList<>();
            for (int r = 0; r < lines.size(); r++) {
                String rowStr = lines.get(r);
                for (int c = 0; c < rowStr.length(); c++) {
                    if (rowStr.charAt(c) == '#') {
                        initialPts.add(new Point(r, c));
                    }
                }
            }
            area = initialPts.size();

            Set<Variation> varSet = new HashSet<>();
            List<Point> cur = normalize(initialPts);

            for (int i = 0; i < 4; i++) {
                varSet.add(new Variation(cur));
                varSet.add(new Variation(flip(cur)));
                cur = rotate(cur);
            }
            variations.addAll(varSet);
        }
    }

    private static class Variation {
        final int w, h;
        final long[] masks;

        Variation(List<Point> pts) {
            int maxR = 0, maxC = 0;
            for (Point p : pts) {
                maxR = Math.max(maxR, p.r);
                maxC = Math.max(maxC, p.c);
            }
            this.h = maxR + 1;
            this.w = maxC + 1;
            this.masks = new long[h];
            for (Point p : pts) {
                this.masks[p.r] |= (1L << p.c);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Variation variation = (Variation) o;
            return w == variation.w && h == variation.h && Arrays.equals(masks, variation.masks);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(w, h);
            result = 31 * result + Arrays.hashCode(masks);
            return result;
        }
    }

    private static class Point {
        final int r, c;
        Point(int r, int c) { this.r = r; this.c = c; }
    }

    private static List<Point> rotate(List<Point> pts) {
        List<Point> res = new ArrayList<>();
        for (Point p : pts) {
            res.add(new Point(p.c, -p.r));
        }
        return normalize(res);
    }

    private static List<Point> flip(List<Point> pts) {
        List<Point> res = new ArrayList<>();
        for (Point p : pts) {
            res.add(new Point(p.r, -p.c));
        }
        return normalize(res);
    }

    private static List<Point> normalize(List<Point> pts) {
        int minR = Integer.MAX_VALUE;
        int minC = Integer.MAX_VALUE;
        for (Point p : pts) {
            minR = Math.min(minR, p.r);
            minC = Math.min(minC, p.c);
        }
        List<Point> res = new ArrayList<>();
        for (Point p : pts) {
            res.add(new Point(p.r - minR, p.c - minC));
        }
        res.sort((a, b) -> a.r != b.r ? Integer.compare(a.r, b.r) : Integer.compare(a.c, b.c));
        return res;
    }
}