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
        Map<Integer, Long> initialTimelines = Map.of(start.col(), 1L);

        Map<Integer, Long> finalTimelines = manifold.streamRowsFrom(start.row() + 1)
                .reduce(
                        initialTimelines,
                        this::computeNextQuantumState,
                        (map1, map2) -> map1
                );

        return finalTimelines.values().stream()
                .mapToLong(Long::longValue)
                .sum();
    }

    private Map<Integer, Long> computeNextQuantumState(Map<Integer, Long> currentTimelines, String row) {
        return currentTimelines.entrySet().stream()
                .flatMap(entry -> propagateQuantumBeam(entry.getKey(), entry.getValue(), row))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.summingLong(Map.Entry::getValue)
                ));
    }

    private Stream<Map.Entry<Integer, Long>> propagateQuantumBeam(int col, long timelineCount, String row) {
        if (hitSplitter(col, row)) {
            return Stream.of(
                    Map.entry(col - 1, timelineCount),
                    Map.entry(col + 1, timelineCount)
            ).filter(e -> isWithinBounds(e.getKey(), row));
        }
        return Stream.of(Map.entry(col, timelineCount)).filter(e -> isWithinBounds(e.getKey(), row));
    }

    private boolean hitSplitter(int col, String row) {
        return row.charAt(col) == '^';
    }

    private boolean isWithinBounds(int col, String row) {
        return col >= 0 && col < row.length();
    }
}