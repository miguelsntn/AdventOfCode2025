package test;

import software.aoc.day07.TachyonManifold;
import software.aoc.day07.a.ClassicalPhysicsEngine;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class Day07ATest {

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
    public void should_count_total_splits_correctly_for_specification_example() {
        List<String> gridLines = EXAMPLE_INPUT.lines()
                .filter(line -> !line.isBlank())
                .collect(Collectors.toList());

        TachyonManifold manifold = TachyonManifold.from(gridLines);

        long splits = manifold.simulate(ClassicalPhysicsEngine.getInstance());

        assertThat(splits).isEqualTo(21L);
    }

    @Test
    public void solve_puzzle_with_real_input() {
        Path inputPath = Paths.get("test", "resources", "day07-a", "input.txt");

        try {
            List<String> gridLines = Files.readAllLines(inputPath).stream()
                    .filter(line -> !line.isBlank())
                    .collect(Collectors.toList());

            TachyonManifold manifold = TachyonManifold.from(gridLines);

            long splits = manifold.simulate(ClassicalPhysicsEngine.getInstance());

            System.out.println("La respuesta al rompecabezas es: " + splits);

        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}