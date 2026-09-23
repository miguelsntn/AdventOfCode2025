package software.aoc.day06;

import java.util.List;
import java.util.stream.IntStream;

public abstract class BaseExpressionScanner implements ExpressionScanner {

    @Override
    public CalculationLedger scan(List<String> textLines) {
        if (textLines == null || textLines.isEmpty()) {
            throw new IllegalArgumentException("El documento no puede estar vacío");
        }

        int maxWidth = textLines.stream().mapToInt(String::length).max().orElse(0);

        int[] emptyColumns = IntStream.rangeClosed(0, maxWidth)
                .filter(col -> col == maxWidth || textLines.stream()
                        .allMatch(line -> col >= line.length() || line.charAt(col) == ' '))
                .toArray();

        List<MathExpression> expressions = IntStream.range(0, emptyColumns.length)
                .mapToObj(i -> {
                    int start = (i == 0) ? 0 : emptyColumns[i - 1] + 1;
                    int end = emptyColumns[i];
                    return new int[]{start, end};
                })
                .filter(bounds -> bounds[0] < bounds[1])
                .map(bounds -> extractExpression(textLines, bounds[0], bounds[1]))
                .toList();

        return new CalculationLedger(expressions);
    }

    protected abstract MathExpression extractExpression(List<String> lines, int start, int end);
}