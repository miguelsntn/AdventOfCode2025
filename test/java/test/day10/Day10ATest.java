package test.day10;

import software.aoc.day10.MachineBlueprint;
import software.aoc.day10.a.LightOptimizer;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class Day10ATest {
    private static final String EXAMPLE_INPUT = """
            [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
            [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
            [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
            """;

    @Test
    public void should_calculate_minimum_light_presses_for_example() {
        List<MachineBlueprint> blueprints = EXAMPLE_INPUT.lines()
                .filter(Predicate.not(String::isBlank))
                .map(MachineBlueprint::parse)
                .toList();

        LightOptimizer optimizer = new LightOptimizer();

        long totalPresses = blueprints.stream()
                .mapToLong(optimizer::calculateMinimumPresses)
                .sum();

        assertThat(totalPresses).isEqualTo(7L);
    }

    @Test
    public void solve_puzzle_with_real_input() {
        Path inputPath = Paths.get("test", "resources", "day10", "a", "input.txt");

        try {
            List<MachineBlueprint> blueprints = Files.lines(inputPath)
                    .filter(Predicate.not(String::isBlank))
                    .map(MachineBlueprint::parse)
                    .toList();

            LightOptimizer optimizer = new LightOptimizer();
            long minPresses = blueprints.stream()
                    .mapToLong(optimizer::calculateMinimumPresses)
                    .sum();

            System.out.println("La respuesta al rompecabezas es: " + minPresses);

        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}