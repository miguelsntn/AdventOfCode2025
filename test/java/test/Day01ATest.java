package test;

import software.aoc.day01.a.Dial;
import software.aoc.day01.a.Order;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Day01ATest {

    public static void main(String[] args) {
        Path inputPath = Paths.get("test", "resources", "day01-a", "orders.txt");

        try (Stream<String> lines = Files.lines(inputPath)) {
            Dial initialDial = Dial.createStartingAt(50);

            Dial finalDial = lines
                    .map(Order::fromString)
                    .reduce(initialDial,
                            Dial::applyOrder,
                            (dial1, dial2) -> dial2);

            System.out.println("La contraseña real es: " + finalDial.getZerosCount());

        } catch (Exception e) {
            System.err.println("Error leyendo el archivo: " + e.getMessage());
        }
    }
}