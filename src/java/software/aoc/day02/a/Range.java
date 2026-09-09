package software.aoc.day02.a;

import java.util.stream.LongStream;

public class Range {
    private final long start;
    private final long end;

    private Range(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public static Range from(String rangeString) {
        if (rangeString == null || !rangeString.contains("-")) {
            throw new IllegalArgumentException("Rango inválido: " + rangeString);
        }

        String[] parts = rangeString.split("-");
        long start = Long.parseLong(parts[0].trim());
        long end = Long.parseLong(parts[1].trim());

        return new Range(start, end);
    }

    public LongStream expandToSequence() {
        return LongStream.rangeClosed(start, end);
    }
}