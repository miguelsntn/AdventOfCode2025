package test;

import software.aoc.day10.b.FactorySystem;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day10BTest {

    public static void main(String[] args) {
        Path inputPath = Paths.get("test", "resources", "day10-b", "input.txt");

        try (Stream<String> lines = Files.lines(inputPath)) {
            List<String> validLines = lines
                    .filter(line -> !line.isBlank())
                    .collect(Collectors.toList());

            FactorySystem system = FactorySystem.from(validLines);
            long minPresses = system.getMinimumTotalPresses();

            System.out.println("El numero minimo total de pulsaciones para el voltaje es: " + minPresses);

        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}