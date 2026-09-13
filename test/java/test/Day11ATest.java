package test;

import software.aoc.day11.a.Reactor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day11ATest {

    public static void main(String[] args) {
        Path inputPath = Paths.get("test", "resources", "day11-a", "input.txt");

        try (Stream<String> lines = Files.lines(inputPath)) {
            List<String> validLines = lines
                    .filter(line -> !line.isBlank())
                    .collect(Collectors.toList());

            Reactor reactor = Reactor.from(validLines);
            long paths = reactor.countPathsFromYouToOut();

            System.out.println("El numero total de caminos desde 'you' hasta 'out' es: " + paths);

        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}