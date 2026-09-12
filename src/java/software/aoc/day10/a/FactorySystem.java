package software.aoc.day10.a;

import java.util.ArrayList;
import java.util.List;

public class FactorySystem {
    private final List<Machine> machines;

    private FactorySystem(List<Machine> machines) {
        this.machines = List.copyOf(machines);
    }

    public static FactorySystem from(List<String> lines) {
        List<Machine> parsedMachines = new ArrayList<>();
        for (String line : lines) {
            parsedMachines.add(Machine.parse(line));
        }
        return new FactorySystem(parsedMachines);
    }

    public long getMinimumTotalPresses() {
        long total = 0;
        for (Machine machine : machines) {
            total += machine.getMinPresses();
        }
        return total;
    }
}