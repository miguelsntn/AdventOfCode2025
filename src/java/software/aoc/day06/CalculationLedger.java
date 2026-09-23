package software.aoc.day06;

import java.util.List;

public record CalculationLedger(List<MathExpression> expressions) {
    public long calculateGrandTotal() {
        return expressions.stream()
                .mapToLong(MathExpression::evaluate)
                .sum();
    }
}