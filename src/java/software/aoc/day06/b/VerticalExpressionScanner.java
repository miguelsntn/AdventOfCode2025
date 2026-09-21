package software.aoc.day06.b;

import software.aoc.day06.ArithmeticOperator;
import software.aoc.day06.BaseExpressionScanner;
import software.aoc.day06.MathExpression;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class VerticalExpressionScanner extends BaseExpressionScanner {

    @Override
    protected MathExpression extractExpression(List<String> lines, int start, int end) {
        int operatorRow = lines.size() - 1;
        String bottomLine = lines.get(operatorRow);

        String opToken = (start < bottomLine.length())
                ? bottomLine.substring(start, Math.min(end, bottomLine.length())).trim()
                : "";

        ArithmeticOperator operator = ArithmeticOperator.from(opToken.charAt(0));

        List<Long> operands = IntStream.range(start, end)
                .mapToObj(col -> lines.stream()
                        .limit(operatorRow)
                        .filter(line -> col < line.length())
                        .map(line -> String.valueOf(line.charAt(col)))
                        .filter(str -> Character.isDigit(str.charAt(0)))
                        .collect(Collectors.joining())
                )
                .filter(Predicate.not(String::isEmpty))
                .map(Long::parseLong)
                .toList();

        return new MathExpression(operands, operator);
    }
}