package test;

import software.aoc.day03.a.EmergencyPowerSystem;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class Day03ATest {

    private static final String EXAMPLE_INPUT = """
            987654321111111
            811111111111119
            234234234234278
            818181911112111
            """;

    @Test
    public void should_calculate_max_joltage_correctly_for_specification_example() {
        EmergencyPowerSystem system = EmergencyPowerSystem.from(EXAMPLE_INPUT);

        long totalOutputJoltage = system.calculateTotalOutputJoltage();

        assertThat(totalOutputJoltage).isEqualTo(357L);
    }

    @Test
    public void solve_puzzle_with_real_input() {
        Path inputPath = Paths.get("test", "resources", "day03-a", "input.txt");

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