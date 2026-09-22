package test.day05;

import software.aoc.day05.b.InventorySystem;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class Day05BTest {
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
    public void should_calculate_total_fresh_capacity_correctly() {
        String cleanInput = EXAMPLE_INPUT.replace("\r", "");
        String[] sections = cleanInput.trim().split("\n\\s*\n", 2);

        InventorySystem system = InventorySystem.fromRanges(sections[0]);
        long totalCapacity = system.countTotalFreshCapacity();

        assertThat(totalCapacity).isEqualTo(14L);
    }

    @Test
    public void solve_puzzle_with_real_input() {
        Path inputPath = Paths.get("test", "resources", "day05", "b", "input.txt");

        try {
            String rawContent = Files.readString(inputPath);

            String cleanContent = rawContent.replace("\r", "");
            String[] sections = cleanContent.trim().split("\n\\s*\n", 2);

            InventorySystem system = InventorySystem.fromRanges(sections[0]);
            long totalCapacity = system.countTotalFreshCapacity();

            System.out.println("La respuesta al rompecabezas es: " + totalCapacity);

        } catch (Exception e) {
            System.err.println("Error leyendo el archivo: " + e.getMessage());
        }
    }
}