package software.aoc.day06;

import java.util.List;
import java.util.stream.IntStream;

public interface ExpressionScanner {
    CalculationLedger scan(List<String> textLines);
}