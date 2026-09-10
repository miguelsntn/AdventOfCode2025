package test;

import software.aoc.day03.a.BatteryBank;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Day03ATest {

    public static void main(String[] args) {
        Path inputPath = Paths.get("test", "resources", "day03-a", "input.txt");

        try (Stream<String> lines = Files.lines(inputPath)) {

            int totalOutputJoltage = lines
                    .filter(line -> !line.isBlank())
                    .map(BatteryBank::from)
                    .mapToInt(BatteryBank::calculateMaxJoltage)
                    .sum();

            System.out.println("El voltaje de salida total es: " + totalOutputJoltage);

        } catch (Exception e) {
            System.err.println("Error leyendo el archivo: " + e.getMessage());
        }
    }
}