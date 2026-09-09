package software.aoc.day01.b;

public class Order {
    private final String direction;
    private final int distance;

    private Order(String direction, int distance) {
        this.direction = direction;
        this.distance = distance;
    }

    public static Order fromString(String instruction) {
        if (instruction == null || instruction.trim().length() < 2) {
            throw new IllegalArgumentException("Instrucción inválida: " + instruction);
        }

        String direction = instruction.substring(0, 1).toUpperCase();
        int distance = Integer.parseInt(instruction.substring(1));

        return new Order(direction, distance);
    }

    public String getDirection() {
        return direction;
    }

    public int getDistance() {
        return distance;
    }
}
