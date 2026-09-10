package test;

import software.aoc.day05.b.InventorySystem;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day05BTest {

    public static void main(String[] args) {
        Path inputPath = Paths.get("test", "resources", "day05-b", "input.txt");

        try {
            List<String> lines = Files.readAllLines(inputPath);
            List<String> rangeLines = new ArrayList<>();

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    break;
                }
                rangeLines.add(line);
            }

            InventorySystem inventory = InventorySystem.from(rangeLines);
            long totalFresh = inventory.countTotalFresh();

            System.out.println("El total de IDs de ingredientes frescos es: " + totalFresh);

        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}