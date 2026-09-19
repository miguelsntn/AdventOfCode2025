package software.aoc.day03;

public class BatteryBank {
    private final String ratings;

    private BatteryBank(String ratings) {
        this.ratings = ratings;
    }

    public static BatteryBank from(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Banco de baterias invalido");
        }
        return new BatteryBank(line.trim());
    }

    public String getRatings() {
        return ratings;
    }
}