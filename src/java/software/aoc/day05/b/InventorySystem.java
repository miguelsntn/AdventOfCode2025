package software.aoc.day05.b;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventorySystem {
    private final List<FreshRange> ranges;

    private InventorySystem(List<FreshRange> ranges) {
        this.ranges = new ArrayList<>(ranges);
        Collections.sort(this.ranges);
    }

    public static InventorySystem from(List<String> rangeLines) {
        if (rangeLines == null) {
            throw new IllegalArgumentException("Las lineas de rangos no pueden ser nulas");
        }

        List<FreshRange> parsedRanges = new ArrayList<>();
        for (String line : rangeLines) {
            String[] parts = line.split("-");
            long start = Long.parseLong(parts[0].trim());
            long end = Long.parseLong(parts[1].trim());
            parsedRanges.add(new FreshRange(start, end));
        }

        return new InventorySystem(parsedRanges);
    }

    public long countTotalFresh() {
        if (ranges.isEmpty()) {
            return 0;
        }

        long total = 0;
        long currentStart = ranges.get(0).start;
        long currentEnd = ranges.get(0).end;

        for (int i = 1; i < ranges.size(); i++) {
            FreshRange next = ranges.get(i);

            if (next.start <= currentEnd + 1) {
                currentEnd = Math.max(currentEnd, next.end);
            } else {
                total += (currentEnd - currentStart + 1);
                currentStart = next.start;
                currentEnd = next.end;
            }
        }

        total += (currentEnd - currentStart + 1);

        return total;
    }

    private static class FreshRange implements Comparable<FreshRange> {
        private final long start;
        private final long end;

        public FreshRange(long start, long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public int compareTo(FreshRange other) {
            int startCompare = Long.compare(this.start, other.start);
            if (startCompare != 0) {
                return startCompare;
            }
            return Long.compare(this.end, other.end);
        }
    }
}