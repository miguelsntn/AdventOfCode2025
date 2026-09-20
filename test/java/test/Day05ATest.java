package test;

import software.aoc.day05.a.InventorySystem;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class Day05ATest {
    private static final String EXAMPLE_INPUT = """
            3-5
            10-14
            16-20
            12-18
            
            1
            5
            8
            11
            17
            32
            """;

    @Test
    public void should_count_fresh_ingredients_correctly_for_specification_example() {
        String cleanInput = EXAMPLE_INPUT.replace("\r", "");
        String[] sections = cleanInput.trim().split("\n\\s*\n", 2);

        InventorySystem system = InventorySystem.fromRanges(sections[0]);
        long freshCount = system.countFreshIngredients(sections[1]);

        assertThat(freshCount).isEqualTo(3L);
    }

    @Test
    public void solve_puzzle_with_real_input() {
        Path inputPath = Paths.get("test", "resources", "day05-a", "input.txt");

        try {
            String rawContent = Files.readString(inputPath);

            String cleanContent = rawContent.replace("\r", "");

            String[] sections = cleanContent.trim().split("\n\\s*\n", 2);

            InventorySystem system = InventorySystem.fromRanges(sections[0]);
            long freshCount = system.countFreshIngredients(sections[1]);

            System.out.println("La respuesta al rompecabezas es: " + freshCount);

        } catch (Exception e) {
            System.err.println("Error leyendo el archivo: " + e.getMessage());
        }
    }
}