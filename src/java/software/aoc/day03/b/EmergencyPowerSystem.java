package software.aoc.day03.b;

import software.aoc.day03.BatteryBank;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public record EmergencyPowerSystem(List<BatteryBank> banks) {

    private static final int TARGET_LENGTH = 12;

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

    private long calculateSingleBank(BatteryBank bank) {
        String ratings = bank.ratings();
        if (ratings.length() < TARGET_LENGTH) {
            throw new IllegalArgumentException("Longitud insuficiente");
        }

        int removeCount = ratings.length() - TARGET_LENGTH;
        StringBuilder stack = new StringBuilder();

        for (int i = 0; i < ratings.length(); i++) {
            char currentDigit = ratings.charAt(i);

            while (removeCount > 0 && !stack.isEmpty() && stack.charAt(stack.length() - 1) < currentDigit) {
                stack.deleteCharAt(stack.length() - 1);
                removeCount--;
            }
            stack.append(currentDigit);
        }

        return Long.parseLong(stack.substring(0, TARGET_LENGTH));
    }
}