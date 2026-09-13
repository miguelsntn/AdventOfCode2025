package software.aoc.day11.a;

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

    public long countPathsFromYouToOut() {
        Map<String, Long> memo = new HashMap<>();
        return dfs("you", "out", memo);
    }

    private long dfs(String current, String target, Map<String, Long> memo) {
        if (current.equals(target)) {
            return 1;
        }

        if (memo.containsKey(current)) {
            return memo.get(current);
        }

        long totalPaths = 0;
        List<String> neighbors = adjacencyList.getOrDefault(current, new ArrayList<>());

        for (String neighbor : neighbors) {
            totalPaths += dfs(neighbor, target, memo);
        }

        memo.put(current, totalPaths);
        return totalPaths;
    }
}