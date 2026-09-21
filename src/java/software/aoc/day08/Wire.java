package software.aoc.day08;

public record Wire(CircuitNode a, CircuitNode b, long cost) implements Comparable<Wire> {

    public static Wire connect(CircuitNode a, CircuitNode b) {
        return new Wire(a, b, a.squaredDistanceTo(b));
    }

    @Override
    public int compareTo(Wire other) {
        return Long.compare(this.cost, other.cost);
    }
}