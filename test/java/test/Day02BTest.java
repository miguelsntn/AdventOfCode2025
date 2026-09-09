package test;

import software.aoc.day02.b.GiftShopDatabase;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Day02BTest {

    public static void main(String[] args) {
        Path inputPath = Paths.get("test", "resources", "day02-b", "input.txt");

        try {
            String rawRanges = Files.readString(inputPath).trim();

            GiftShopDatabase database = GiftShopDatabase.from(rawRanges);
            long totalSum = database.sumInvalidIds();

            System.out.println("La nueva suma de todos los IDs invalidos es: " + totalSum);

        } catch (Exception e) {
            System.err.println("Error leyendo el archivo: " + e.getMessage());
        }
    }
}