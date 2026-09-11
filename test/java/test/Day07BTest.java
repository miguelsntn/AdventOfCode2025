package test;

import software.aoc.day07.b.TachyonManifold;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day07BTest {

    public static void main(String[] args) {
        Path inputPath = Paths.get("test", "resources", "day07-b", "input.txt");

        try (Stream<String> lines = Files.lines(inputPath)) {
            List<String> gridLines = lines
                    .filter(line -> !line.isBlank())
                    .collect(Collectors.toList());

            TachyonManifold manifold = TachyonManifold.from(gridLines);
            long timelines = manifold.countQuantumTimelines();

            System.out.println("El numero total de lineas temporales es: " + timelines);

        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}