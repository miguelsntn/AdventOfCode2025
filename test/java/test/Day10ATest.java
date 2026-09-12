package test;

import software.aoc.day10.a.FactorySystem;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day10ATest {

    public static void main(String[] args) {
        Path inputPath = Paths.get("test", "resources", "day10-a", "input.txt");

        try (Stream<String> lines = Files.lines(inputPath)) {
            List<String> validLines = lines
                    .filter(line -> !line.isBlank())
                    .collect(Collectors.toList());

            FactorySystem system = FactorySystem.from(validLines);
            long minPresses = system.getMinimumTotalPresses();

            System.out.println("El numero minimo total de pulsaciones es: " + minPresses);

        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}