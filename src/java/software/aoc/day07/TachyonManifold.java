package software.aoc.day07;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TachyonManifold {
    private final List<String> grid;

    private TachyonManifold(List<String> grid) {
        this.grid = List.copyOf(grid);
    }

    public static TachyonManifold from(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("El diagrama del colector no puede estar vacio");
        }
        return new TachyonManifold(lines);
    }

    public Coordinate findStart() {
        return IntStream.range(0, grid.size())
                .boxed()
                .flatMap(r -> IntStream.range(0, grid.get(r).length())
                        .filter(c -> grid.get(r).charAt(c) == 'S')
                        .mapToObj(c -> new Coordinate(r, c)))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Punto de inicio 'S' no encontrado"));
    }

    public Stream<String> streamRowsFrom(int startRow) {
        return IntStream.range(startRow, grid.size()).mapToObj(grid::get);
    }

    public long simulate(TachyonPhysicsEngine engine) {
        return engine.calculate(this);
    }
}