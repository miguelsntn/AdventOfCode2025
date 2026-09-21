package test;

import software.aoc.day09.GridPoint;
import software.aoc.day09.b.ConstrainedAreaCalculator;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class Day09BTest {

    private static final String EXAMPLE_INPUT = """
            7,1
            11,1
            11,7
            9,7
            9,5
            2,5
            2,3
            7,3
            """;

    @Test
    public void should_calculate_max_valid_area_inside_green_polygon_for_example() {
        List<GridPoint> points = EXAMPLE_INPUT.lines()
                .filter(Predicate.not(String::isBlank))
                .map(GridPoint::fromString)
                .toList();

        ConstrainedAreaCalculator calculator = new ConstrainedAreaCalculator(points);

        long maxArea = calculator.findLargestValidArea();

        assertThat(maxArea).isEqualTo(24L);
    }

    @Test
    public void solve_puzzle_with_real_input() {
        Path inputPath = Paths.get("test", "resources", "day09-b", "input.txt");

        try {
            List<GridPoint> points = Files.lines(inputPath)
                    .filter(Predicate.not(String::isBlank))
                    .map(GridPoint::fromString)
                    .toList();

            ConstrainedAreaCalculator calculator = new ConstrainedAreaCalculator(points);

            long maxArea = calculator.findLargestValidArea();

            System.out.println("La respuesta al rompecabezas es: " + maxArea);

        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}