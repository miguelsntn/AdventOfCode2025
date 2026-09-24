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
        SimulationState initialState = new SimulationState(Set.of(start.col()), 0L);

        SimulationState finalState = manifold.streamRowsFrom(start.row() + 1)
                .reduce(
                        initialState,
                        this::computeNextState,
                        (state1, state2) -> state1
                );

        return finalState.totalSplits();
    }

    private SimulationState computeNextState(SimulationState currentState, String row) {
        Set<Integer> nextBeams = currentState.activeBeams().stream()
                .flatMap(col -> propagateBeam(col, row))
                .collect(Collectors.toUnmodifiableSet());

        long newSplits = currentState.activeBeams().stream()
                .filter(col -> hitSplitter(col, row))
                .count();

        return new SimulationState(nextBeams, currentState.totalSplits() + newSplits);
    }

    private Stream<Integer> propagateBeam(int col, String row) {
        if (hitSplitter(col, row)) {
            return Stream.of(col - 1, col + 1).filter(c -> isWithinBounds(c, row));
        }
        return Stream.of(col).filter(c -> isWithinBounds(c, row));
    }

    private boolean hitSplitter(int col, String row) {
        return row.charAt(col) == '^';
    }

    private boolean isWithinBounds(int col, String row) {
        return col >= 0 && col < row.length();
    }
}