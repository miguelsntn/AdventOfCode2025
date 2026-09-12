package test;

import software.aoc.day09.b.MovieTheater;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day09BTest {

    public static void main(String[] args) {
        Path inputPath = Paths.get("test", "resources", "day09-b", "input.txt");

        try (Stream<String> lines = Files.lines(inputPath)) {
            List<String> coordsLines = lines
                    .filter(line -> !line.isBlank())
                    .collect(Collectors.toList());

            MovieTheater theater = MovieTheater.from(coordsLines);
            long maxArea = theater.findLargestValidRectangleArea();

            System.out.println("El area maxima del rectangulo verde es: " + maxArea);

        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}