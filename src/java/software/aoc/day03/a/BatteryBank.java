package software.aoc.day03.a;

public class BatteryBank {
    private final String ratings;

    private BatteryBank(String ratings) {
        this.ratings = ratings;
    }

    public static BatteryBank from(String line) {
        if (line == null || line.length() < 2) {
            throw new IllegalArgumentException("Banco de baterias invalido: " + line);
        }
        return new BatteryBank(line);
    }

    public int calculateMaxJoltage() {
        int maxJoltage = 0;
        int n = ratings.length();

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