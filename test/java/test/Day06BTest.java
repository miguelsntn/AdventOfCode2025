package test;

import software.aoc.day06.b.CephalopodWorksheet;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Day06BTest {

    public static void main(String[] args) {
        Path inputPath = Paths.get("test", "resources", "day06-b", "input.txt");

        try {
            List<String> lines = Files.readAllLines(inputPath);

            while (!lines.isEmpty() && lines.get(lines.size() - 1).trim().isEmpty()) {
                lines.remove(lines.size() - 1);
            }

            CephalopodWorksheet worksheet = CephalopodWorksheet.from(lines);
            long grandTotal = worksheet.calculateGrandTotal();

            System.out.println("El total general de la tarea de matematicas es: " + grandTotal);

        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}