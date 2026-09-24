package software.aoc.day11.b;

import software.aoc.day11.ReactorNetwork;

import java.util.HashMap;
import java.util.Map;

public class MandatoryRouteAnalyzer {
    private final ReactorNetwork network;
    private final Map<DfsState, Long> memo;

    public MandatoryRouteAnalyzer(ReactorNetwork network) {
        this.network = network;
        this.memo = new HashMap<>();
    }

    public long countRestrictedPaths(String start, String target, String req1, String req2) {
        DfsState initialState = new DfsState(
                start,
                start.equals(req1),
                start.equals(req2)
        );
        return exploreRestrictedPaths(initialState, target, req1, req2);
    }

    private long exploreRestrictedPaths(DfsState state, String target, String req1, String req2) {
        if (state.currentNode().equals(target)) {
            return (state.hasVisitedReq1() && state.hasVisitedReq2()) ? 1L : 0L;
        }

        if (memo.containsKey(state)) {
            return memo.get(state);
        }

        long totalPaths = network.getNeighborsOf(state.currentNode()).stream()
                .map(neighbor -> new DfsState(
                        neighbor,
                        state.hasVisitedReq1() || neighbor.equals(req1),
                        state.hasVisitedReq2() || neighbor.equals(req2)
                ))
                .mapToLong(nextState -> exploreRestrictedPaths(nextState, target, req1, req2))
                .sum();

        memo.put(state, totalPaths);
        return totalPaths;
    }

    private record DfsState(String currentNode, boolean hasVisitedReq1, boolean hasVisitedReq2) {}
}