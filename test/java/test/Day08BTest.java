package test;

import software.aoc.day08.b.CircuitSystem;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day08BTest {

    public static void main(String[] args) {
        Path inputPath = Paths.get("test", "resources", "day08-b", "input.txt");

        try (Stream<String> lines = Files.lines(inputPath)) {
            List<String> pointLines = lines
                    .filter(line -> !line.isBlank())
                    .collect(Collectors.toList());

            CircuitSystem system = CircuitSystem.from(pointLines);
            long result = system.solveForFinalConnection();

            System.out.println("El producto de las coordenadas X de la conexion final es: " + result);

        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}