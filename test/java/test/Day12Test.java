package test;

import software.aoc.day12.FarmAllocator;
import software.aoc.day12.FarmParser;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class Day12Test {

    private static final String EXAMPLE_INPUT = """
            0:
            ###
            ##.
            ##.
            
            1:
            ###
            ##.
            .##
            
            2:
            .##
            ###
            ##.
            
            3:
            ##.
            ###
            ##.
            
            4:
            ###
            #..
            ###
            
            5:
            ###
            .#.
            ###
            
            4x4: 0 0 0 0 2 0
            12x5: 1 0 1 0 2 2
            12x5: 1 0 1 0 3 2
            """;

    @Test
    public void should_count_fitting_regions_for_example() {
        List<String> validLines = EXAMPLE_INPUT.lines()
                .filter(Predicate.not(String::isBlank))
                .toList();

        FarmParser.ParsedFarm farm = FarmParser.parse(validLines);
        FarmAllocator allocator = new FarmAllocator(farm.shapes());

        int validRegions = allocator.countFittingRegions(farm.regions());

        assertThat(validRegions).isEqualTo(2);
    }

    @Test
    public void solve_puzzle_with_real_input() {
        Path inputPath = Paths.get("test", "resources", "day12", "input.txt");

        try {
            List<String> validLines = Files.lines(inputPath)
                    .filter(Predicate.not(String::isBlank))
                    .toList();

            FarmParser.ParsedFarm farm = FarmParser.parse(validLines);
            FarmAllocator allocator = new FarmAllocator(farm.shapes());

            int validRegions = allocator.countFittingRegions(farm.regions());

            System.out.println("La respuesta al rompecabezas es: " + validRegions);

        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}