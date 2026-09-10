package test;

import software.aoc.day05.a.InventorySystem;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day05ATest {

    public static void main(String[] args) {
        Path inputPath = Paths.get("test", "resources", "day05-a", "input.txt");

        try {
            List<String> lines = Files.readAllLines(inputPath);

            List<String> rangeLines = new ArrayList<>();
            List<Long> ingredientIds = new ArrayList<>();
            boolean isParsingRanges = true;

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    isParsingRanges = false;
                    continue;
                }

                if (isParsingRanges) {
                    rangeLines.add(line);
                } else {
                    ingredientIds.add(Long.parseLong(line.trim()));
                }
            }

            InventorySystem inventory = InventorySystem.from(rangeLines);
            long freshCount = inventory.countFresh(ingredientIds);

            System.out.println("El numero de ingredientes frescos es: " + freshCount);

        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}