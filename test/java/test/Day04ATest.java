package test;

import software.aoc.day04.PaperGrid;
import software.aoc.day04.a.PaperRollManager;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class Day04ATest {

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
    public void should_count_accessible_rolls_correctly() {
        List<String> gridLines = EXAMPLE_INPUT.lines()
                .filter(line -> !line.isBlank())
                .collect(Collectors.toList());

        PaperGrid grid = PaperGrid.from(gridLines);
        PaperRollManager processor = new PaperRollManager();

        int accessibleRolls = processor.countAccessibleRolls(grid);

        assertThat(accessibleRolls).isEqualTo(13);
    }

    @Test
    public void solve_puzzle_with_real_input() {
        Path inputPath = Paths.get("test", "resources", "day04-a", "input.txt");

        try (Stream<String> lines = Files.lines(inputPath)) {
            List<String> gridLines = lines
                    .filter(line -> !line.isBlank())
                    .collect(Collectors.toList());

            PaperGrid grid = PaperGrid.from(gridLines);
            PaperRollManager processor = new PaperRollManager();

            int accessibleRolls = processor.countAccessibleRolls(grid);

            System.out.println("La respuesta al rompecabezas es: " + accessibleRolls);

        } catch (Exception e) {
            System.err.println("Error leyendo el archivo: " + e.getMessage());
        }
    }
}