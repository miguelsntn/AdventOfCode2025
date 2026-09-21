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
            throw new IllegalArgumentException("Bloque vacio");
        }

        ArithmeticOperator operator = ArithmeticOperator.from(tokens.get(tokens.size() - 1).charAt(0));

        List<Long> operands = tokens.stream()
                .limit(tokens.size() - 1)
                .map(Long::parseLong)
                .toList();

        return new MathExpression(operands, operator);
    }
}