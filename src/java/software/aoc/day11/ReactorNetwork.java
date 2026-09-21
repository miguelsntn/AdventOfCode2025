package software.aoc.day11;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record ReactorNetwork(Map<String, List<String>> adjacencyList) {

    public ReactorNetwork {
        adjacencyList = adjacencyList.entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        e -> List.copyOf(e.getValue())
                ));
    }

    public List<String> getNeighborsOf(String node) {
        return adjacencyList.getOrDefault(node, List.of());
    }
}