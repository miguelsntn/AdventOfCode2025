package test;

import software.aoc.day04.a.PaperGrid;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day04ATest {

    public static void main(String[] args) {
        Path inputPath = Paths.get("test", "resources", "day04-a", "input.txt");

        try (Stream<String> lines = Files.lines(inputPath)) {
            List<String> gridLines = lines
                    .filter(line -> !line.isBlank())
                    .collect(Collectors.toList());

            PaperGrid grid = PaperGrid.from(gridLines);
            int accessibleRolls = grid.countAccessibleRolls();

            System.out.println("El numero de rollos de papel accesibles es: " + accessibleRolls);

        } catch (Exception e) {
            System.err.println("Error leyendo el archivo: " + e.getMessage());
        }
    }
}