package test;

import software.aoc.day11.b.Reactor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day11BTest {

    public static void main(String[] args) {
        Path inputPath = Paths.get("test", "resources", "day11-b", "input.txt");

        try (Stream<String> lines = Files.lines(inputPath)) {
            List<String> validLines = lines
                    .filter(line -> !line.isBlank())
                    .collect(Collectors.toList());

            Reactor reactor = Reactor.from(validLines);
            long paths = reactor.countRestrictedPaths("svr", "out", "dac", "fft");

            System.out.println("El numero de caminos que pasan por 'dac' y 'fft' es: " + paths);

        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}