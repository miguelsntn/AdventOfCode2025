package software.aoc.day06;

import java.util.function.Function;
import java.util.stream.LongStream;

public enum ArithmeticOperator {
    ADDITION(LongStream::sum),
    MULTIPLICATION(s -> s.reduce((a, b) -> a * b).orElse(0L));

    private final Function<LongStream, Long> operation;

    ArithmeticOperator(Function<LongStream, Long> operation) {
        this.operation = operation;
    }

    public long apply(LongStream operands) {
        return operation.apply(operands);
    }

    public static ArithmeticOperator from(char symbol) {
        return symbol == '+' ? ADDITION : MULTIPLICATION;
    }
}