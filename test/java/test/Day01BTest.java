package test;

import software.aoc.day01.b.SafeDecoder;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;

public class Day01BTest {

    private static final String EXAMPLE_ORDERS = """
            L68
            L30
            R48
            L5
            R60
            L55
            L1
            L99
            R14
            L82
            """;

    @Test
    public void should_decode_password_correctly_for_part_b() {
        Stream<String> document = EXAMPLE_ORDERS.lines().filter(line -> !line.isBlank());

        long password = SafeDecoder.decodePassword(document);

        assertThat(password).isEqualTo(6L);
    }

    @Test
    public void solve_puzzle_with_real_input() {
        Path inputPath = Paths.get("test", "resources", "day01-b", "input.txt");

        try (Stream<String> document = Files.lines(inputPath)) {

            long realPassword = SafeDecoder.decodePassword(document.filter(line -> !line.isBlank()));

            System.out.println("La respuesta al rompecabezas es: " + realPassword);

        } catch (Exception e) {
            System.err.println("Error leyendo el archivo: " + e.getMessage());
        }
    }
}