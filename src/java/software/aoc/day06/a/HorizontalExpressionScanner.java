package software.aoc.day06.a;

import software.aoc.day06.ArithmeticOperator;
import software.aoc.day06.BaseExpressionScanner;
import software.aoc.day06.MathExpression;

import java.util.List;
import java.util.function.Predicate;

public class HorizontalExpressionScanner extends BaseExpressionScanner {

    @Override
    protected MathExpression extractExpression(List<String> lines, int start, int end) {
        List<String> tokens = lines.stream()
                .filter(line -> start < line.length())
                .map(line -> line.substring(start, Math.min(end, line.length())).trim())
                .filter(Predicate.not(String::isEmpty))
                .toList();

        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Bloque vacío");
        }

        ArithmeticOperator operator = ArithmeticOperator.from(tokens.getLast().charAt(0));

        long[] operands = tokens.stream()
                .limit(tokens.size() - 1)
                .mapToLong(Long::parseLong)
                .toArray();

        return new MathExpression(operator, operands);
    }
}