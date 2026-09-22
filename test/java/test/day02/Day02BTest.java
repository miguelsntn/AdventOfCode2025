package test.day02;

import software.aoc.day02.b.GiftShopDatabase;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class Day02BTest {

    private static final String EXAMPLE_RANGES = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124";

    @Test
    public void should_sum_invalid_ids_correctly_for_part_b_rules() {
        GiftShopDatabase database = GiftShopDatabase.from(EXAMPLE_RANGES);

        long totalSum = database.sumInvalidIds();

        assertThat(totalSum).isEqualTo(4174379265L);
    }

    @Test
    public void solve_puzzle_with_real_input() {
        Path inputPath = Paths.get("test", "resources", "day02", "b", "input.txt");

        try {
            String rawRanges = Files.readString(inputPath).trim();
            GiftShopDatabase database = GiftShopDatabase.from(rawRanges);

            long totalSum = database.sumInvalidIds();

            System.out.println("La respuesta al rompecabezas es: " + totalSum);

        } catch (Exception e) {
            System.err.println("Error leyendo el archivo: " + e.getMessage());
        }
    }
}