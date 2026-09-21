package software.aoc.day11.a;

import software.aoc.day11.ReactorNetwork;

import java.util.HashMap;
import java.util.Map;

public class BasicRouteAnalyzer {
    private final ReactorNetwork network;

    public BasicRouteAnalyzer(ReactorNetwork network) {
        this.network = network;
    }

    public long countPaths(String start, String target) {
        return explorePaths(start, target, new HashMap<>());
    }

    private long explorePaths(String current, String target, Map<String, Long> memo) {
        if (current.equals(target)) {
            return 1L;
        }

        if (memo.containsKey(current)) {
            return memo.get(current);
        }

        long totalPaths = 0;
        for (String neighbor : network.getNeighborsOf(current)) {
            totalPaths += explorePaths(neighbor, target, memo);
        }

        memo.put(current, totalPaths);
        return totalPaths;
    }
}