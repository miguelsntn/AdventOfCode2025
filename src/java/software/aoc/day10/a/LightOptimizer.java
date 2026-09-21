package software.aoc.day10.a;

import software.aoc.day10.MachineBlueprint;
import software.aoc.day10.MachineOptimizer;

import java.util.List;

public class LightOptimizer implements MachineOptimizer {

    @Override
    public long calculateMinimumPresses(MachineBlueprint blueprint) {
        long targetState = 0;
        List<Integer> lights = blueprint.lightTargets();
        for (int i = 0; i < lights.size(); i++) {
            if (lights.get(i) == 1) {
                targetState |= (1L << i);
            }
        }

        long[] buttonMasks = new long[blueprint.buttons().size()];
        for (int i = 0; i < blueprint.buttons().size(); i++) {
            long mask = 0;
            for (int bit : blueprint.buttons().get(i)) {
                mask |= (1L << bit);
            }
            buttonMasks[i] = mask;
        }

        int result = findMinPresses(buttonMasks, targetState, 0, 0L, 0, Integer.MAX_VALUE);
        if (result == Integer.MAX_VALUE) {
            throw new IllegalStateException("No se encontro solucion para la configuracion de luces.");
        }
        return result;
    }

    private int findMinPresses(long[] buttons, long targetState, int index, long currentState, int presses, int bestSoFar) {
        if (presses >= bestSoFar) return bestSoFar;

        if (index == buttons.length) {
            return (currentState == targetState) ? presses : bestSoFar;
        }

        bestSoFar = findMinPresses(buttons, targetState, index + 1, currentState, presses, bestSoFar);

        bestSoFar = findMinPresses(buttons, targetState, index + 1, currentState ^ buttons[index], presses + 1, bestSoFar);

        return bestSoFar;
    }
}