package test.day03;

import software.aoc.day03.b.EmergencyPowerSystem;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class Day03BTest {

    private static final String EXAMPLE_INPUT = """
            987654321111111
            811111111111119
            234234234234278
            818181911112111
            """;

    @Test
    public void should_calculate_max_joltage_correctly_for_part_b_rules() {
        EmergencyPowerSystem system = EmergencyPowerSystem.from(EXAMPLE_INPUT);

        long totalOutputJoltage = system.calculateTotalOutputJoltage();

        assertThat(totalOutputJoltage).isEqualTo(3121910778619L);
    }

    @Test
    public void solve_puzzle_with_real_input() {
        Path inputPath = Paths.get("test", "resources", "day03", "b", "input.txt");

        try {
            String input = Files.readString(inputPath);
            EmergencyPowerSystem system = EmergencyPowerSystem.from(input);

            long totalOutputJoltage = system.calculateTotalOutputJoltage();

            System.out.println("La respuesta al rompecabezas es: " + totalOutputJoltage);

        } catch (Exception e) {
            System.err.println("Error leyendo el archivo: " + e.getMessage());
        }
    }
}