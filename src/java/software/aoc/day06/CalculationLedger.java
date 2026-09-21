package software.aoc.day06;

import java.util.List;

public class CalculationLedger {
    private final List<MathExpression> expressions;

    public CalculationLedger(List<MathExpression> expressions) {
        this.expressions = List.copyOf(expressions);
    }

    public long calculateGrandTotal() {
        return expressions.stream()
                .mapToLong(MathExpression::evaluate)
                .sum();
    }
}