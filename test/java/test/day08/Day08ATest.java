package test.day08;

import software.aoc.day08.CircuitNode;
import software.aoc.day08.DisjointSetTracker;
import software.aoc.day08.a.CircuitAnalyzer;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class Day08ATest {

    private static final String EXAMPLE_INPUT = """
            162,817,812
            57,618,57
            906,360,560
            592,479,940
            352,342,300
            466,668,158
            542,29,236
            431,825,988
            739,650,466
            52,470,668
            216,146,977
            819,987,18
            117,168,530
            805,96,715
            346,949,466
            970,615,88
            941,993,340
            862,61,35
            984,92,344
            425,690,689
            """;

    @Test
    public void should_calculate_product_of_top_3_circuits_after_10_connections() {
        List<String> validLines = EXAMPLE_INPUT.lines()
                .filter(Predicate.not(String::isBlank))
                .toList();

        List<CircuitNode> nodes = IntStream.range(0, validLines.size())
                .mapToObj(i -> CircuitNode.fromLine(i, validLines.get(i)))
                .toList();

        DisjointSetTracker tracker = new DisjointSetTracker(nodes.size());
        CircuitAnalyzer analyzer = new CircuitAnalyzer(nodes, tracker);

        long result = analyzer.calculateTopClustersProduct(10);

        assertThat(result).isEqualTo(40L);
    }

    @Test
    public void solve_puzzle_with_real_input() {
        Path inputPath = Paths.get("test", "resources", "day08", "a", "input.txt");

        try {
            List<String> validLines = Files.lines(inputPath)
                    .filter(Predicate.not(String::isBlank))
                    .toList();

            List<CircuitNode> nodes = IntStream.range(0, validLines.size())
                    .mapToObj(i -> CircuitNode.fromLine(i, validLines.get(i)))
                    .toList();

            DisjointSetTracker tracker = new DisjointSetTracker(nodes.size());
            CircuitAnalyzer analyzer = new CircuitAnalyzer(nodes, tracker);

            long result = analyzer.calculateTopClustersProduct(1000);

            System.out.println("La respuesta al rompecabezas es: " + result);

        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}