package test;

import software.aoc.day01.a.SafeDecoder;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;

public class Day01ATest {

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
    public void should_decode_password_correctly() {
        Stream<String> document = EXAMPLE_ORDERS.lines().filter(line -> !line.isBlank());

        int password = SafeDecoder.decodePassword(document);

        assertThat(password).isEqualTo(3);
    }

    @Test
    public void solve_puzzle_with_real_input() {
        Path inputPath = Paths.get("test", "resources", "day01-a", "input.txt");

        try (Stream<String> document = Files.lines(inputPath)) {

            int realPassword = SafeDecoder.decodePassword(document.filter(line -> !line.isBlank()));

            System.out.println("La respuesta al rompecabezas es: " + realPassword);

        } catch (Exception e) {
            System.err.println("Error leyendo el archivo: " + e.getMessage());
        }
    }
}