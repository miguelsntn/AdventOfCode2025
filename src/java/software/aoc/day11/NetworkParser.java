package software.aoc.day11;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class NetworkParser {

    public static ReactorNetwork parse(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("La lista de dispositivos no puede estar vacía");
        }

        Map<String, List<String>> graph = new HashMap<>();

        for (String line : lines) {
            if (line.isBlank()) continue;

            String[] parts = line.split(":");
            String node = parts[0].trim();

            if (parts.length > 1 && !parts[1].isBlank()) {
                List<String> neighbors = Arrays.stream(parts[1].trim().split("\\s+"))
                        .filter(Predicate.not(String::isBlank))
                        .toList();
                graph.put(node, neighbors);
            } else {
                graph.put(node, List.of());
            }
        }

        if (!graph.containsKey("out")) {
            graph.put("out", List.of());
        }

        return new ReactorNetwork(graph);
    }
}