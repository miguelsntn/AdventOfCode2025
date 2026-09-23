package software.aoc.day05.b;

import software.aoc.day05.FreshRange;
import java.util.ArrayList;
import java.util.List;

public class InventorySystem {
    private final List<FreshRange> sortedRanges;

    private InventorySystem(List<FreshRange> sortedRanges) {
        this.sortedRanges = List.copyOf(sortedRanges);
    }

    public static InventorySystem fromRanges(String rangesSection) {
        if (rangesSection == null || rangesSection.isBlank()) {
            return new InventorySystem(List.of());
        }

        List<FreshRange> parsedRanges = rangesSection.lines()
                .filter(line -> !line.isBlank())
                .map(FreshRange::from)
                .sorted()
                .toList();

        return new InventorySystem(parsedRanges);
    }

    public long countTotalFreshCapacity() {
        if (sortedRanges.isEmpty()) {
            return 0;
        }

        List<FreshRange> mergedRanges = new ArrayList<>();
        FreshRange currentRange = sortedRanges.get(0);

        for (int i = 1; i < sortedRanges.size(); i++) {
            FreshRange nextRange = sortedRanges.get(i);

            if (currentRange.connectsWith(nextRange)) {
                currentRange = currentRange.merge(nextRange);
            } else {
                mergedRanges.add(currentRange);
                currentRange = nextRange;
            }
        }
        mergedRanges.add(currentRange);

        long totalCapacity = 0;
        for (FreshRange range : mergedRanges) {
            totalCapacity += range.size();
        }

        return totalCapacity;
    }
}