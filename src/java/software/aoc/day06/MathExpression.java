package software.aoc.day06;

import java.util.Arrays;

public record MathExpression(ArithmeticOperator operator, long[] operands) {
    public long evaluate() {
        return operator.apply(Arrays.stream(operands));
    }
}