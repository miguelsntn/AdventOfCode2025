package software.aoc.day07.a;

import software.aoc.day07.Coordinate;
import software.aoc.day07.TachyonManifold;
import software.aoc.day07.TachyonPhysicsEngine;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassicalPhysicsEngine implements TachyonPhysicsEngine {

    private static final ClassicalPhysicsEngine INSTANCE = new ClassicalPhysicsEngine();

    private ClassicalPhysicsEngine() {}

    public static ClassicalPhysicsEngine getInstance() {
        return INSTANCE;
    }

    private record SimulationState(Set<Integer> activeBeams, long totalSplits) {}

    @Override
    public long calculate(TachyonManifold manifold) {
        Coordinate start = manifold.findStart();

        SimulationState finalState = manifold.streamRowsFrom(start.row() + 1)
                .reduce(
                        new SimulationState(Set.of(start.col()), 0L),
                        (state, row) -> {
                            Set<Integer> nextBeams = state.activeBeams().stream()
                                    .flatMap(col -> (row.charAt(col) == '^') ? Stream.of(col - 1, col + 1) : Stream.of(col))
                                    .filter(col -> col >= 0 && col < row.length())
                                    .collect(Collectors.toUnmodifiableSet());

                            long newSplits = state.activeBeams().stream()
                                    .filter(col -> row.charAt(col) == '^')
                                    .count();

                            return new SimulationState(nextBeams, state.totalSplits() + newSplits);
                        },
                        (s1, s2) -> s1
                );

        return finalState.totalSplits();
    }
}