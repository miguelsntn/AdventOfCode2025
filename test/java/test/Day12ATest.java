package test;

import software.aoc.day12.a.ChristmasTreeFarm;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12ATest {

    public static void main(String[] args) {
        Path inputPath = Paths.get("test", "resources", "day12-a", "input.txt");

        try (Stream<String> lines = Files.lines(inputPath)) {
            List<String> fileLines = lines.collect(Collectors.toList());

            ChristmasTreeFarm farm = ChristmasTreeFarm.from(fileLines);
            int validRegions = farm.countFittingRegions();

            System.out.println("El numero de regiones donde caben todos los regalos es: " + validRegions);

        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}