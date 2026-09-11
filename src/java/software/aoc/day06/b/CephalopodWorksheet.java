package software.aoc.day06.b;

import java.util.ArrayList;
import java.util.List;

public class CephalopodWorksheet {
    private final List<MathProblem> problems;

    private CephalopodWorksheet(List<MathProblem> problems) {
        this.problems = List.copyOf(problems);
    }

    public static CephalopodWorksheet from(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("La hoja de calculo no puede estar vacia");
        }

        int maxLen = lines.stream().mapToInt(String::length).max().orElse(0);

        List<MathProblem> parsedProblems = new ArrayList<>();
        int startCol = 0;
        boolean inProblem = false;

        for (int c = 0; c <= maxLen; c++) {
            boolean isSpaceCol = true;

            if (c < maxLen) {
                for (String line : lines) {
                    if (c < line.length() && line.charAt(c) != ' ') {
                        isSpaceCol = false;
                        break;
                    }
                }
            }

            if (isSpaceCol || c == maxLen) {
                if (inProblem) {
                    parsedProblems.add(parseProblem(lines, startCol, c));
                    inProblem = false;
                }
            } else {
                if (!inProblem) {
                    startCol = c;
                    inProblem = true;
                }
            }
        }

        return new CephalopodWorksheet(parsedProblems);
    }

    private static MathProblem parseProblem(List<String> lines, int start, int end) {
        int operatorRow = lines.size() - 1;
        String bottomRowPart = "";

        if (start < lines.get(operatorRow).length()) {
            int actualEnd = Math.min(end, lines.get(operatorRow).length());
            bottomRowPart = lines.get(operatorRow).substring(start, actualEnd).trim();
        }

        if (bottomRowPart.isEmpty()) {
            throw new IllegalArgumentException("No se encontro operador en la ultima linea del bloque");
        }
        char operator = bottomRowPart.charAt(0);

        List<Long> numbers = new ArrayList<>();

        for (int c = start; c < end; c++) {
            StringBuilder numBuilder = new StringBuilder();

            for (int r = 0; r < operatorRow; r++) {
                String line = lines.get(r);
                if (c < line.length()) {
                    char ch = line.charAt(c);
                    if (Character.isDigit(ch)) {
                        numBuilder.append(ch);
                    }
                }
            }

            if (numBuilder.length() > 0) {
                numbers.add(Long.parseLong(numBuilder.toString()));
            }
        }

        return new MathProblem(numbers, operator);
    }

    public long calculateGrandTotal() {
        return problems.stream()
                .mapToLong(MathProblem::solve)
                .sum();
    }

    private static class MathProblem {
        private final List<Long> numbers;
        private final char operator;

        public MathProblem(List<Long> numbers, char operator) {
            this.numbers = List.copyOf(numbers);
            this.operator = operator;
        }

        public long solve() {
            if (operator == '+') {
                return numbers.stream().mapToLong(Long::longValue).sum();
            } else if (operator == '*') {
                long result = 1;
                for (long n : numbers) {
                    result *= n;
                }
                return result;
            } else {
                throw new IllegalStateException("Operador desconocido: " + operator);
            }
        }
    }
}