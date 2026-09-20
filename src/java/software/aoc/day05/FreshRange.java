package software.aoc.day05;

public class FreshRange implements Comparable<FreshRange> {
    public final long start;
    public final long end;

    private FreshRange(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public static FreshRange from(String line) {
        if (line == null || !line.contains("-")) {
            throw new IllegalArgumentException("Rango invalido: " + line);
        }
        String[] parts = line.split("-");
        return new FreshRange(
                Long.parseLong(parts[0].trim()),
                Long.parseLong(parts[1].trim())
        );
    }

    public boolean contains(long id) {
        return id >= start && id <= end;
    }

    public long size() {
        return end - start + 1;
    }

    public boolean connectsWith(FreshRange other) {
        return other.start <= this.end + 1;
    }

    public FreshRange merge(FreshRange other) {
        return new FreshRange(Math.min(this.start, other.start), Math.max(this.end, other.end));
    }

    @Override
    public int compareTo(FreshRange other) {
        int startCompare = Long.compare(this.start, other.start);
        return startCompare != 0 ? startCompare : Long.compare(this.end, other.end);
    }
}