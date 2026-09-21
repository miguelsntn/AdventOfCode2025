package software.aoc.day07.b;

import software.aoc.day07.Coordinate;
import software.aoc.day07.TachyonManifold;
import software.aoc.day07.TachyonPhysicsEngine;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuantumPhysicsEngine implements TachyonPhysicsEngine {

    private static final QuantumPhysicsEngine INSTANCE = new QuantumPhysicsEngine();

    private QuantumPhysicsEngine() {}

    public static QuantumPhysicsEngine getInstance() {
        return INSTANCE;
    }

    @Override
    public long calculate(TachyonManifold manifold) {
        Coordinate start = manifold.findStart();

        Map<Integer, Long> finalTimelines = manifold.streamRowsFrom(start.row() + 1)
                .reduce(
                        Map.of(start.col(), 1L),
                        (timelines, row) -> timelines.entrySet().stream()
                                .flatMap(entry -> {
                                    int col = entry.getKey();
                                    long count = entry.getValue();
                                    if (row.charAt(col) == '^') {
                                        return Stream.of(Map.entry(col - 1, count), Map.entry(col + 1, count));
                                    }
                                    return Stream.of(Map.entry(col, count));
                                })
                                .filter(e -> e.getKey() >= 0 && e.getKey() < row.length())
                                .collect(Collectors.groupingBy(
                                        Map.Entry::getKey,
                                        Collectors.summingLong(Map.Entry::getValue)
                                )),
                        (m1, m2) -> m1
                );

        return finalTimelines.values().stream()
                .mapToLong(Long::longValue)
                .sum();
    }
}