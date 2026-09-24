package software.aoc.day09.a;

import software.aoc.day09.GridPoint;
import java.util.List;
import java.util.stream.IntStream;

public class TheaterAreaCalculator {
    private final List<GridPoint> points;

    public TheaterAreaCalculator(List<GridPoint> points) {
        this.points = List.copyOf(points);
    }

    public long findMaxArea() {
        return IntStream.range(0, points.size())
                .boxed()
                .flatMap(i -> IntStream.range(i + 1, points.size())
                        .mapToObj(j -> points.get(i).calculateAreaTo(points.get(j))))
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);
    }
}