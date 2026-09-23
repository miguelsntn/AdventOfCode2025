package software.aoc.day06;

import java.util.List;

public interface ExpressionScanner {
    CalculationLedger scan(List<String> textLines);
}