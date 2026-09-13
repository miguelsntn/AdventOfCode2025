package software.aoc.day11.b;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reactor {
    private final Map<String, List<String>> adjacencyList;

    private Reactor(Map<String, List<String>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public static Reactor from(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("La lista de dispositivos no puede estar vacia");
        }

        Map<String, List<String>> graph = new HashMap<>();

        for (String line : lines) {
            String[] parts = line.split(":");
            String node = parts[0].trim();
            List<String> neighbors = new ArrayList<>();

            if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                String[] neighborTokens = parts[1].trim().split("\\s+");
                for (String neighbor : neighborTokens) {
                    neighbors.add(neighbor);
                }
            }

            graph.put(node, neighbors);
        }

        return new Reactor(graph);
    }

    public long countRestrictedPaths(String start, String target, String req1, String req2) {
        Map<String, Long> memo = new HashMap<>();
        return dfs(start, 0, memo, target, req1, req2);
    }

    private long dfs(String current, int state, Map<String, Long> memo, String target, String req1, String req2) {
        if (current.equals(req1)) state |= 1;
        if (current.equals(req2)) state |= 2;

        if (current.equals(target)) {
            return (state == 3) ? 1 : 0;
        }

        String memoKey = current + "_" + state;
        if (memo.containsKey(memoKey)) {
            return memo.get(memoKey);
        }

        long totalPaths = 0;
        List<String> neighbors = adjacencyList.getOrDefault(current, new ArrayList<>());

        for (String neighbor : neighbors) {
            totalPaths += dfs(neighbor, state, memo, target, req1, req2);
        }

        memo.put(memoKey, totalPaths);
        return totalPaths;
    }
}