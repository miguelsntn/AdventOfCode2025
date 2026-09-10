package software.aoc.day03.b;

public class BatteryBank {
    private final String ratings;
    private static final int TARGET_LENGTH = 12;

    private BatteryBank(String ratings) {
        this.ratings = ratings;
    }

    public static BatteryBank from(String line) {
        if (line == null || line.length() < TARGET_LENGTH) {
            throw new IllegalArgumentException("Banco de baterias invalido: " + line);
        }
        return new BatteryBank(line);
    }

    public long calculateMaxJoltage() {
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