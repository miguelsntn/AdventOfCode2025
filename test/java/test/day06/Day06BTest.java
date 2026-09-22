package test.day06;

import software.aoc.day06.CalculationLedger;
import software.aoc.day06.ExpressionScanner;
import software.aoc.day06.b.VerticalExpressionScanner;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Day06BTest {

    private static final String EXAMPLE_INPUT = """
            123 328  51 64\s
             45 64  387 23\s
              6 98  215 314
              *   +   *   +
            """;

    @Test
    public void should_calculate_grand_total_correctly_for_vertical_reading() {
        List<String> lines = EXAMPLE_INPUT.lines().toList();
        ExpressionScanner scanner = new VerticalExpressionScanner();

        CalculationLedger ledger = scanner.scan(lines);
        long grandTotal = ledger.calculateGrandTotal();

        assertThat(grandTotal).isEqualTo(3263827L);
    }

    @Test
    public void solve_puzzle_with_real_input() {
        Path inputPath = Paths.get("test", "resources", "day06", "b", "input.txt");

        try {
            List<String> lines = Files.readAllLines(inputPath);

            while (!lines.isEmpty() && lines.get(lines.size() - 1).trim().isEmpty()) {
                lines.remove(lines.size() - 1);
            }

            ExpressionScanner scanner = new VerticalExpressionScanner();
            CalculationLedger ledger = scanner.scan(lines);
            long grandTotal = ledger.calculateGrandTotal();

            System.out.println("La respuesta al rompecabezas es: " + grandTotal);

        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}