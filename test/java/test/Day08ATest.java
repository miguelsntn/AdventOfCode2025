package test;

import software.aoc.day08.a.CircuitSystem;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day08ATest {

    public static void main(String[] args) {
        Path inputPath = Paths.get("test", "resources", "day08-a", "input.txt");

        try (Stream<String> lines = Files.lines(inputPath)) {
            List<String> pointLines = lines
                    .filter(line -> !line.isBlank())
                    .collect(Collectors.toList());

            CircuitSystem system = CircuitSystem.from(pointLines);

            int connectionsToMake = pointLines.size() < 50 ? 10 : 1000;

            long result = system.solve(connectionsToMake);

            System.out.println("El producto de los tres circuitos mas grandes es: " + result);

        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}