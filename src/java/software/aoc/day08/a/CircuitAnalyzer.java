package software.aoc.day08.a;

import software.aoc.day08.CircuitNode;
import software.aoc.day08.NetworkTracker;
import software.aoc.day08.Wire;
import software.aoc.day08.WireOptimizer;

import java.util.Comparator;
import java.util.List;

public class CircuitAnalyzer {
    private final List<Wire> sortedWires;
    private final NetworkTracker tracker;

    public CircuitAnalyzer(List<CircuitNode> nodes, NetworkTracker tracker) {
        this.tracker = tracker;
        this.sortedWires = WireOptimizer.generateSortedWires(nodes);
    }

    public long calculateTopClustersProduct(int connectionsToMake) {
        int limit = Math.min(connectionsToMake, sortedWires.size());

        for (int i = 0; i < limit; i++) {
            Wire w = sortedWires.get(i);
            tracker.linkNodes(w.a().id(), w.b().id());
        }
        return tracker.getClusterSizes().stream()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .mapToLong(Integer::longValue)
                .reduce(1L, (acc, size) -> acc * size);
    }
}