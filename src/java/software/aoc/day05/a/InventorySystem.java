package software.aoc.day05.a;

import java.util.ArrayList;
import java.util.List;

public class InventorySystem {
    private final List<FreshRange> freshRanges;

    private InventorySystem(List<FreshRange> freshRanges) {
        this.freshRanges = List.copyOf(freshRanges);
    }

    public static InventorySystem from(List<String> rangeLines) {
        if (rangeLines == null) {
            throw new IllegalArgumentException("Las lineas de rangos no pueden ser nulas");
        }

        List<FreshRange> ranges = new ArrayList<>();
        for (String line : rangeLines) {
            String[] parts = line.split("-");
            long start = Long.parseLong(parts[0].trim());
            long end = Long.parseLong(parts[1].trim());
            ranges.add(new FreshRange(start, end));
        }

        return new InventorySystem(ranges);
    }

    public long countFresh(List<Long> ingredientIds) {
        return ingredientIds.stream()
                .filter(this::isFresh)
                .count();
    }

    private boolean isFresh(long id) {
        for (FreshRange range : freshRanges) {
            if (range.contains(id)) {
                return true;
            }
        }
        return false;
    }

    private static class FreshRange {
        private final long start;
        private final long end;

        public FreshRange(long start, long end) {
            this.start = start;
            this.end = end;
        }

        public boolean contains(long id) {
            return id >= start && id <= end;
        }
    }
}