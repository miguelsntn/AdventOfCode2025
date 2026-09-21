package software.aoc.day11.b;

import software.aoc.day11.ReactorNetwork;

import java.util.HashMap;
import java.util.Map;

public class MandatoryRouteAnalyzer {
    private final ReactorNetwork network;

    public MandatoryRouteAnalyzer(ReactorNetwork network) {
        this.network = network;
    }

    public long countRestrictedPaths(String start, String target, String req1, String req2) {
        return exploreRestrictedPaths(start, 0, target, req1, req2, new HashMap<>());
    }

    private long exploreRestrictedPaths(String current, int stateMask, String target, String req1, String req2, Map<DfsState, Long> memo) {
        if (current.equals(req1)) stateMask |= 1;
        if (current.equals(req2)) stateMask |= 2;

        if (current.equals(target)) {
            return (stateMask == 3) ? 1L : 0L;
        }

        DfsState currentState = new DfsState(current, stateMask);
        if (memo.containsKey(currentState)) {
            return memo.get(currentState);
        }

        long totalPaths = 0;
        for (String neighbor : network.getNeighborsOf(current)) {
            totalPaths += exploreRestrictedPaths(neighbor, stateMask, target, req1, req2, memo);
        }

        memo.put(currentState, totalPaths);
        return totalPaths;
    }

    private record DfsState(String node, int mask) {}
}