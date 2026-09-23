package software.aoc.day01.a;

import software.aoc.day01.Order;

public record Dial(int position, int zerosCount) {

    public static Dial createStartingAt(int position) {
        return new Dial(position, 0);
    }

    public Dial apply(Order order) {
        int step = order.direction().equals("L") ? -order.distance() : order.distance();

        int newPosition = (this.position + step) % 100;
        if (newPosition < 0) {
            newPosition += 100;
        }

        int newZeros = this.zerosCount + (newPosition == 0 ? 1 : 0);
        return new Dial(newPosition, newZeros);
    }
}