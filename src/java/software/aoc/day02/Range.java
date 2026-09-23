package software.aoc.day02;

import java.util.stream.LongStream;

public record Range(long start, long end) {

    public static Range from(String rangeString) {
        if (rangeString == null || !rangeString.contains("-")) {
            throw new IllegalArgumentException("Rango inválido: " + rangeString);
        }

        String[] parts = rangeString.split("-");
        return new Range(
                Long.parseLong(parts[0].trim()),
                Long.parseLong(parts[1].trim())
        );
    }
    public LongStream stream() {
        return LongStream.rangeClosed(start, end);
    }
}