package test;

import software.aoc.day04.PaperGrid;
import software.aoc.day04.b.PaperRollManager;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class Day04BTest {

    private static final String EXAMPLE_INPUT = """
            ..@@.@@@@.
            @@@.@.@.@@
            @@@@@.@.@@
            @.@@@@..@.
            @@.@@@@.@@
            .@@@@@@@.@
            .@.@.@.@@@
            @.@@@.@@@@
            .@@@@@@@@.
            @.@.@@@.@.
            """;

    @Test
    public void should_remove_all_accessible_rolls_correctly_for_part_b_rules() {
        List<String> gridLines = EXAMPLE_INPUT.lines()
                .filter(line -> !line.isBlank())
                .collect(Collectors.toList());

        PaperGrid grid = PaperGrid.from(gridLines);
        PaperRollManager processor = new PaperRollManager();


        int totalRemoved = processor.removeAllAccessibleRolls(grid);

        assertThat(totalRemoved).isEqualTo(43);
    }

    @Test
    public void solve_puzzle_with_real_input() {
        Path inputPath = Paths.get("test", "resources", "day04-b", "input.txt");

        try (Stream<String> lines = Files.lines(inputPath)) {
            List<String> gridLines = lines
                    .filter(line -> !line.isBlank())
                    .collect(Collectors.toList());

            PaperGrid grid = PaperGrid.from(gridLines);
            PaperRollManager processor = new PaperRollManager();

            int totalRemoved = processor.removeAllAccessibleRolls(grid);

            System.out.println("La respuesta al rompecabezas es: " + totalRemoved);

        } catch (Exception e) {
            System.err.println("Error leyendo el archivo: " + e.getMessage());
        }
    }
}