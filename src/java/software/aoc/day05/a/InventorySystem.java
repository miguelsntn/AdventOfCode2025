package software.aoc.day05.a;

import software.aoc.day05.FreshRange;
import java.util.List;

public class InventorySystem {
    private final List<FreshRange> freshRanges;

    private InventorySystem(List<FreshRange> freshRanges) {
        this.freshRanges = List.copyOf(freshRanges);
    }

    public static InventorySystem fromRanges(String rangesSection) {
        if (rangesSection == null || rangesSection.isBlank()) {
            return new InventorySystem(List.of());
        }

        List<FreshRange> ranges = rangesSection.lines()
                .filter(line -> !line.isBlank())
                .map(FreshRange::from)
                .toList();

        return new InventorySystem(ranges);
    }

    public long countFreshIngredients(String idsSection) {
        if (idsSection == null || idsSection.isBlank()) {
            return 0;
        }

        return idsSection.lines()
                .filter(line -> !line.isBlank())
                .mapToLong(Long::parseLong)
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
}