package software.aoc.day01;

public record Order(String direction, int distance) {

    public static Order fromString(String instruction) {
        if (instruction == null || instruction.trim().length() < 2) {
            throw new IllegalArgumentException("Instrucción inválida: " + instruction);
        }

        String clean = instruction.trim().toUpperCase();
        return new Order(
                clean.substring(0, 1),
                Integer.parseInt(clean.substring(1))
        );
    }
}