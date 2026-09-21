package software.aoc.day12;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PresentShape {
    private final int id;
    private final int area;
    private final List<ShapeVariation> variations;

    public PresentShape(int id, List<Point> initialPoints) {
        this.id = id;
        this.area = initialPoints.size();
        this.variations = generateUniqueVariations(initialPoints);
    }

    public int getId() { return id; }
    public int getArea() { return area; }
    public List<ShapeVariation> getVariations() { return variations; }

    private List<ShapeVariation> generateUniqueVariations(List<Point> pts) {
        Set<ShapeVariation> uniqueVars = new HashSet<>();
        List<Point> current = normalize(pts);

        for (int i = 0; i < 4; i++) {
            uniqueVars.add(ShapeVariation.from(current));
            uniqueVars.add(ShapeVariation.from(flip(current)));
            current = rotate(current);
        }
        return new ArrayList<>(uniqueVars);
    }

    private List<Point> rotate(List<Point> pts) {
        return normalize(pts.stream().map(Point::rotate).toList());
    }

    private List<Point> flip(List<Point> pts) {
        return normalize(pts.stream().map(Point::flip).toList());
    }

    private List<Point> normalize(List<Point> pts) {
        int minR = pts.stream().mapToInt(Point::r).min().orElse(0);
        int minC = pts.stream().mapToInt(Point::c).min().orElse(0);

        return pts.stream()
                .map(p -> new Point(p.r() - minR, p.c() - minC))
                .sorted((a, b) -> a.r() != b.r() ? Integer.compare(a.r(), b.r()) : Integer.compare(a.c(), b.c()))
                .toList();
    }
}