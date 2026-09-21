package software.aoc.day06;

import java.util.List;

public record MathExpression(List<Long> operands, ArithmeticOperator operator) {

    public long evaluate() {
        if (operands.isEmpty()) return 0;

        long result = operands.get(0);
        for (int i = 1; i < operands.size(); i++) {
            result = operator.apply(result, operands.get(i));
        }
        return result;
    }
}