package test;

import software.aoc.day04.b.PaperGrid;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day04BTest {

    public static void main(String[] args) {
        Path inputPath = Paths.get("test", "resources", "day04-b", "input.txt");

        try (Stream<String> lines = Files.lines(inputPath)) {
            List<String> gridLines = lines
                    .filter(line -> !line.isBlank())
                    .collect(Collectors.toList());

            PaperGrid grid = PaperGrid.from(gridLines);
            int totalRemoved = grid.removeAllAccessibleRolls();

            System.out.println("El total de rollos de papel retirados es: " + totalRemoved);

        } catch (Exception e) {
            System.err.println("Error leyendo el archivo: " + e.getMessage());
        }
    }
}