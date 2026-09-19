package software.aoc.day03.b;

import software.aoc.day03.BatteryBank;
import java.util.ArrayList;
import java.util.List;

public class EmergencyPowerSystem {

    private final List<BatteryBank> banks;
    private static final int TARGET_LENGTH = 12;

    private EmergencyPowerSystem(List<BatteryBank> banks) {
        this.banks = banks;
    }

    public static EmergencyPowerSystem from(String rawNotes) {
        List<BatteryBank> parsedBanks = new ArrayList<>();
        String[] lines = rawNotes.split("\\R");
        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                parsedBanks.add(BatteryBank.from(trimmed));
            }
        }
        return new EmergencyPowerSystem(List.copyOf(parsedBanks));
    }

    public long calculateTotalOutputJoltage() {
        long totalSum = 0;
        for (BatteryBank bank : banks) {
            totalSum += calculateSingleBank(bank);
        }
        return totalSum;
    }

    private long calculateSingleBank(BatteryBank bank) {
        String ratings = bank.getRatings();
        if (ratings.length() < TARGET_LENGTH) {
            throw new IllegalArgumentException("Longitud insuficiente");
        }

        int removeCount = ratings.length() - TARGET_LENGTH;
        StringBuilder stack = new StringBuilder();

        for (int i = 0; i < ratings.length(); i++) {
            char currentDigit = ratings.charAt(i);

            while (removeCount > 0 && stack.length() > 0 && stack.charAt(stack.length() - 1) < currentDigit) {
                stack.deleteCharAt(stack.length() - 1);
                removeCount--;
            }
            stack.append(currentDigit);
        }

        String bestSequence = stack.substring(0, TARGET_LENGTH);
        return Long.parseLong(bestSequence);
    }
}