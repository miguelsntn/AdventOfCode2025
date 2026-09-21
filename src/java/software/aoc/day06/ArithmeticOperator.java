package software.aoc.day06;

public enum ArithmeticOperator {
    ADDITION('+'),
    MULTIPLICATION('*');

    private final char symbol;

    ArithmeticOperator(char symbol) {
        this.symbol = symbol;
    }

    public static ArithmeticOperator from(char symbol) {
        for (ArithmeticOperator op : values()) {
            if (op.symbol == symbol) {
                return op;
            }
        }
        throw new IllegalArgumentException("Simbolo aritmetico desconocido: " + symbol);
    }

    public long apply(long a, long b) {
        return switch (this) {
            case ADDITION -> a + b;
            case MULTIPLICATION -> a * b;
        };
    }
}