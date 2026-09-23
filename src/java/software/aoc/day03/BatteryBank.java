package software.aoc.day03;

public record BatteryBank(String ratings) {

    public static BatteryBank from(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Banco de baterías inválido");
        }
        return new BatteryBank(line.trim());
    }
}
