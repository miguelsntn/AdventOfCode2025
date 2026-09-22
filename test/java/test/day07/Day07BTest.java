package test.day07;

import software.aoc.day07.TachyonManifold;
import software.aoc.day07.b.QuantumPhysicsEngine;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class Day07BTest {

    private static final String EXAMPLE_INPUT = """
            .......S.......
            ...............
            .......^.......
            ...............
            ......^.^......
            ...............
            .....^.^.^.....
            ...............
            ....^.^...^....
            ...............
            ...^.^...^.^...
            ...............
            ..^...^.....^..
            ...............
            .^.^.^.^.^...^.
            ...............
            """;

    @Test
    public void should_count_quantum_timelines_correctly_for_specification_example() {
        List<String> gridLines = EXAMPLE_INPUT.lines()
                .filter(line -> !line.isBlank())
                .collect(Collectors.toList());

        TachyonManifold manifold = TachyonManifold.from(gridLines);

        long quantumTimelines = manifold.simulate(QuantumPhysicsEngine.getInstance());

        assertThat(quantumTimelines).isEqualTo(40L);
    }

    @Test
    public void solve_puzzle_with_real_input() {
        Path inputPath = Paths.get("test", "resources", "day07", "b", "input.txt");

        try {
            List<String> gridLines = Files.readAllLines(inputPath).stream()
                    .filter(line -> !line.isBlank())
                    .collect(Collectors.toList());

            TachyonManifold manifold = TachyonManifold.from(gridLines);

            long quantumTimelines = manifold.simulate(QuantumPhysicsEngine.getInstance());

            System.out.println("La respuesta al rompecabezas es: " + quantumTimelines);

        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}