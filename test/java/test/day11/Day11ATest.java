package test.day11;

import software.aoc.day11.NetworkParser;
import software.aoc.day11.ReactorNetwork;
import software.aoc.day11.a.BasicRouteAnalyzer;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class Day11ATest {

    private static final String EXAMPLE_INPUT = """
            aaa: you hhh
            you: bbb ccc
            bbb: ddd eee
            ccc: ddd eee ff
            ddd: ggg
            eee: out
            ff: out
            ggg: out
            hhh: ccc ff iii
            iii: out
            """;

    @Test
    public void should_count_all_paths_from_you_to_out() {
        List<String> validLines = EXAMPLE_INPUT.lines()
                .filter(Predicate.not(String::isBlank))
                .toList();

        ReactorNetwork network = NetworkParser.parse(validLines);
        BasicRouteAnalyzer analyzer = new BasicRouteAnalyzer(network);

        long totalPaths = analyzer.countPaths("you", "out");

        assertThat(totalPaths).isEqualTo(5L);
    }

    @Test
    public void solve_puzzle_with_real_input() {
        Path inputPath = Paths.get("test", "resources", "day11", "a", "input.txt");

        try {
            List<String> validLines = Files.lines(inputPath)
                    .filter(Predicate.not(String::isBlank))
                    .toList();

            ReactorNetwork network = NetworkParser.parse(validLines);
            BasicRouteAnalyzer analyzer = new BasicRouteAnalyzer(network);

            long totalPaths = analyzer.countPaths("you", "out");

            System.out.println("La respuesta al rompecabezas es: " + totalPaths);

        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}