package software.aoc.day03.a;

import software.aoc.day03.BatteryBank;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public record EmergencyPowerSystem(List<BatteryBank> banks) {

    public static EmergencyPowerSystem from(String rawNotes) {
        List<BatteryBank> parsedBanks = Arrays.stream(rawNotes.split("\\R"))
                .map(String::trim)
                .filter(Predicate.not(String::isEmpty))
                .map(BatteryBank::from)
                .toList();

        return new EmergencyPowerSystem(parsedBanks);
    }

    public long calculateTotalOutputJoltage() {
        return banks.stream()
                .mapToLong(this::calculateSingleBank)
                .sum();
    }

    private int calculateSingleBank(BatteryBank bank) {
        String ratings = bank.ratings();
        int n = ratings.length();
        if (n < 2) return 0;

        int[] maxFromRight = new int[n];
        maxFromRight[n - 1] = ratings.charAt(n - 1) - '0';

        for (int i = n - 2; i >= 0; i--) {
            maxFromRight[i] = Math.max(maxFromRight[i + 1], ratings.charAt(i) - '0');
        }

        int maxJoltage = 0;
        for (int i = 0; i < n - 1; i++) {
            int tens = ratings.charAt(i) - '0';
            int ones = maxFromRight[i + 1];
            int currentJoltage = tens * 10 + ones;

            if (currentJoltage > maxJoltage) {
                maxJoltage = currentJoltage;
            }
        }

        return maxJoltage;
    }
}