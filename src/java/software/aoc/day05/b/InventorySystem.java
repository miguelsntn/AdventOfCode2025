package software.aoc.day05.b;

import software.aoc.day05.FreshRange;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

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
        return sortedRanges.stream()
                .collect(mergeOverlappingRanges())
                .stream()
                .mapToLong(FreshRange::size)
                .sum();
    }

    private Collector<FreshRange, List<FreshRange>, List<FreshRange>> mergeOverlappingRanges() {
        return Collector.of(
                ArrayList::new,
                (mergedList, currentRange) -> {
                    if (mergedList.isEmpty()) {
                        mergedList.add(currentRange);
                    } else {
                        int lastIndex = mergedList.size() - 1;
                        FreshRange lastRange = mergedList.get(lastIndex);

                        if (lastRange.connectsWith(currentRange)) {
                            mergedList.set(lastIndex, lastRange.merge(currentRange));
                        } else {
                            mergedList.add(currentRange);
                        }
                    }
                },
                (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                }
        );
    }
}