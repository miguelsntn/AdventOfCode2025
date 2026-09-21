package software.aoc.day08.b;

import software.aoc.day08.CircuitNode;
import software.aoc.day08.NetworkTracker;
import software.aoc.day08.Wire;
import software.aoc.day08.WireOptimizer;

import java.util.List;

public class NetworkUnifier {
    private final List<Wire> sortedWires;
    private final NetworkTracker tracker;

    public NetworkUnifier(List<CircuitNode> nodes, NetworkTracker tracker) {
        this.tracker = tracker;
        this.sortedWires = WireOptimizer.generateSortedWires(nodes);
    }

    public long findLastConnectionProduct() {
        for (Wire w : sortedWires) {
            if (tracker.linkNodes(w.a().id(), w.b().id())) {
                if (tracker.getRemainingClusters() == 1) {
                    return (long) w.a().x() * w.b().x();
                }
            }
        }
        throw new IllegalStateException("No se pudo unificar la red eléctrica.");
    }
}