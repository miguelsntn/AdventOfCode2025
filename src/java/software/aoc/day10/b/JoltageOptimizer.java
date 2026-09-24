package software.aoc.day10.b;

import software.aoc.day10.MachineBlueprint;
import software.aoc.day10.MachineOptimizer;

import java.util.*;
import java.util.stream.IntStream;

public class JoltageOptimizer implements MachineOptimizer {

    private record JoltageEffect(List<Integer> wiringEffect, int buttonPressCount) {}

    @Override
    public long calculateMinimumPresses(MachineBlueprint blueprint) {
        int numButtons = blueprint.buttons().size();
        int machineSize = blueprint.joltageTargets().size();

        List<JoltageEffect> allEffects = IntStream.range(0, 1 << numButtons)
                .mapToObj(mask -> createEffect(mask, blueprint.buttons(), machineSize))
                .toList();

        Map<List<Integer>, Long> memo = new HashMap<>();

        long result = solveDP(blueprint.joltageTargets(), allEffects, memo);

        if (result == Long.MAX_VALUE) {
            throw new IllegalStateException("No se encontró una configuración de voltaje válida.");
        }
        return result;
    }

    private long solveDP(List<Integer> targets, List<JoltageEffect> effects, Map<List<Integer>, Long> memo) {
        if (targets.stream().allMatch(v -> v == 0)) return 0;

        if (memo.containsKey(targets)) return memo.get(targets);

        long minPresses = Long.MAX_VALUE;

        for (JoltageEffect effect : effects) {
            if (canApply(targets, effect)) {
                List<Integer> nextTargets = IntStream.range(0, targets.size())
                        .mapToObj(i -> (targets.get(i) - effect.wiringEffect().get(i)) / 2)
                        .toList();

                long subResult = solveDP(nextTargets, effects, memo);

                if (subResult != Long.MAX_VALUE) {
                    minPresses = Math.min(minPresses, effect.buttonPressCount() + 2 * subResult);
                }
            }
        }

        memo.put(targets, minPresses);
        return minPresses;
    }

    private boolean canApply(List<Integer> targets, JoltageEffect effect) {
        return IntStream.range(0, targets.size())
                .allMatch(i -> {
                    int diff = targets.get(i) - effect.wiringEffect().get(i);
                    return diff >= 0 && diff % 2 == 0;
                });
    }

    private JoltageEffect createEffect(int mask, List<List<Integer>> buttons, int size) {
        int[] sums = new int[size];
        for (int i = 0; i < buttons.size(); i++) {
            if ((mask & (1 << i)) != 0) {
                for (int idx : buttons.get(i)) {
                    sums[idx]++;
                }
            }
        }
        return new JoltageEffect(Arrays.stream(sums).boxed().toList(), Integer.bitCount(mask));
    }
}