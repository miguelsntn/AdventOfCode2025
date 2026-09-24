package software.aoc.day11.a;

import software.aoc.day11.ReactorNetwork;

import java.util.HashMap;
import java.util.Map;

public class BasicRouteAnalyzer {
    private final ReactorNetwork network;
    private final Map<String, Long> memo;

    public BasicRouteAnalyzer(ReactorNetwork network) {
        this.network = network;
        this.memo = new HashMap<>();
    }

    public long countPaths(String start, String target) {
        return explorePaths(start, target);
    }

    private long explorePaths(String current, String target) {
        if (current.equals(target)) {
            return 1L;
        }

        if (memo.containsKey(current)) {
            return memo.get(current);
        }

        long totalPaths = network.getNeighborsOf(current).stream()
                .mapToLong(neighbor -> explorePaths(neighbor, target))
                .sum();

        memo.put(current, totalPaths);
        return totalPaths;
    }
}