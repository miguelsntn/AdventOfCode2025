package software.aoc.day01.b;

import software.aoc.day01.Order;

public record Dial(int position, long zerosCount) {

    public static Dial createStartingAt(int position) {
        return new Dial(position, 0L);
    }

    public Dial apply(Order order) {
        int dist = order.distance();

        int distToZero = order.direction().equals("L")
                ? (this.position == 0 ? 100 : this.position)
                : (this.position == 0 ? 100 : 100 - this.position);

        long crossings = (dist >= distToZero) ? 1 + (dist - distToZero) / 100 : 0;

        int step = order.direction().equals("L") ? -dist : dist;
        int newPosition = (this.position + step) % 100;
        if (newPosition < 0) {
            newPosition += 100;
        }

        return new Dial(newPosition, this.zerosCount + crossings);
    }
}