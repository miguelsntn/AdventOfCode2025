package software.aoc.day10.a;

import software.aoc.day10.MachineBlueprint;
import software.aoc.day10.MachineOptimizer;

import java.util.List;
import java.util.stream.IntStream;

public class LightOptimizer implements MachineOptimizer {

    @Override
    public long calculateMinimumPresses(MachineBlueprint blueprint) {
        int numButtons = blueprint.buttons().size();

        long targetMask = buildMask(blueprint.lightTargets());
        long[] buttonMasks = blueprint.buttons().stream()
                .mapToLong(this::buildMaskFromIndices)
                .toArray();

        return IntStream.range(0, 1 << numButtons)
                .filter(mask -> matchesTarget(mask, buttonMasks, targetMask))
                .map(Integer::bitCount)
                .min()
                .orElseThrow(() -> new IllegalStateException("No se encontró solución."));
    }

    private boolean matchesTarget(int mask, long[] buttonMasks, long targetMask) {
        long currentMask = 0;
        for (int i = 0; i < buttonMasks.length; i++) {
            if ((mask & (1 << i)) != 0) {
                currentMask ^= buttonMasks[i];
            }
        }
        return currentMask == targetMask;
    }

    private long buildMask(List<Integer> bits) {
        return IntStream.range(0, bits.size())
                .filter(i -> bits.get(i) == 1)
                .mapToLong(i -> 1L << i)
                .reduce(0L, (a, b) -> a | b);
    }

    private long buildMaskFromIndices(List<Integer> indices) {
        return indices.stream()
                .mapToLong(i -> 1L << i)
                .reduce(0L, (a, b) -> a | b);
    }
}