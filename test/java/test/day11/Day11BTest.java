package test.day11;

import software.aoc.day11.NetworkParser;
import software.aoc.day11.ReactorNetwork;
import software.aoc.day11.b.MandatoryRouteAnalyzer;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class Day11BTest {

    private static final String EXAMPLE_INPUT = """
            svr: aaa bbb
            aaa: fft
            fft: ccc
            bbb: tty
            tty: ccc
            ccc: ddd eee
            ddd: hub
            hub: ff
            eee: dac
            dac: ff
            ff: ggg hhh
            ggg: out
            hhh: out
            """;

    @Test
    public void should_count_paths_through_mandatory_nodes() {
        List<String> validLines = EXAMPLE_INPUT.lines()
                .filter(Predicate.not(String::isBlank))
                .toList();

        ReactorNetwork network = NetworkParser.parse(validLines);
        MandatoryRouteAnalyzer analyzer = new MandatoryRouteAnalyzer(network);

        long totalPaths = analyzer.countRestrictedPaths("svr", "out", "dac", "fft");

        assertThat(totalPaths).isEqualTo(2L);
    }

    @Test
    public void solve_puzzle_with_real_input() {
        Path inputPath = Paths.get("test", "resources", "day11", "b", "input.txt");

        try {
            List<String> validLines = Files.lines(inputPath)
                    .filter(Predicate.not(String::isBlank))
                    .toList();

            ReactorNetwork network = NetworkParser.parse(validLines);
            MandatoryRouteAnalyzer analyzer = new MandatoryRouteAnalyzer(network);

            long totalPaths = analyzer.countRestrictedPaths("svr", "out", "dac", "fft");

            System.out.println("La respuesta al rompecabezas es: " + totalPaths);

        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}