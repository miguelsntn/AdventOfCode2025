package software.aoc.day09.a;

import software.aoc.day09.GridPoint;
import java.util.List;

public class TheaterAreaCalculator {
    private final List<GridPoint> points;

    public TheaterAreaCalculator(List<GridPoint> points) {
        this.points = List.copyOf(points);
    }

    public long findMaxArea() {
        long maxArea = 0;

        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                long currentArea = points.get(i).calculateAreaTo(points.get(j));
                if (currentArea > maxArea) {
                    maxArea = currentArea;
                }
            }
        }
        return maxArea;
    }
}