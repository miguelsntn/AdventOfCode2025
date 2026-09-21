package software.aoc.day08;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WireOptimizer {
    public static List<Wire> generateSortedWires(List<CircuitNode> nodes) {
        List<Wire> generated = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                generated.add(Wire.connect(nodes.get(i), nodes.get(j)));
            }
        }
        Collections.sort(generated);
        return generated;
    }
}