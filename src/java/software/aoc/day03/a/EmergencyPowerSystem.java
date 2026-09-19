package software.aoc.day03.a;

import software.aoc.day03.BatteryBank;
import java.util.ArrayList;
import java.util.List;

public class EmergencyPowerSystem {

    private final List<BatteryBank> banks;

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

    private int calculateSingleBank(BatteryBank bank) {
        String ratings = bank.getRatings();
        int maxJoltage = 0;
        int n = ratings.length();

        if (n < 2) return 0;

        int[] maxFromRight = new int[n];
        maxFromRight[n - 1] = ratings.charAt(n - 1) - '0';

        for (int i = n - 2; i >= 0; i--) {
            maxFromRight[i] = Math.max(maxFromRight[i + 1], ratings.charAt(i) - '0');
        }

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