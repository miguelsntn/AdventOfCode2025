package software.aoc.day08;

public record CircuitNode(int id, int x, int y, int z) {

    public static CircuitNode fromLine(int id, String line) {
        String[] parts = line.split(",");
        return new CircuitNode(
                id,
                Integer.parseInt(parts[0].trim()),
                Integer.parseInt(parts[1].trim()),
                Integer.parseInt(parts[2].trim())
        );
    }

    public long squaredDistanceTo(CircuitNode other) {
        long dx = this.x - other.x;
        long dy = this.y - other.y;
        long dz = this.z - other.z;
        return dx * dx + dy * dy + dz * dz;
    }
}