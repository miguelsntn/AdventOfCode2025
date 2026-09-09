package software.aoc.day01.b;

public class Dial {
    private final int currentPosition;
    private final long zerosCount;

    private Dial(int currentPosition, long zerosCount) {
        this.currentPosition = currentPosition;
        this.zerosCount = zerosCount;
    }

    public static Dial createStartingAt(int position) {
        return new Dial(position, 0L);
    }

    public Dial applyOrder(Order order) {
        int newPosition = this.currentPosition;
        long newZerosCount = this.zerosCount;
        int dist = order.getDistance();

        if ("L".equals(order.getDirection())) {
            int distToZero = (this.currentPosition == 0) ? 100 : this.currentPosition;

            if (dist >= distToZero) {
                newZerosCount += 1 + (dist - distToZero) / 100;
            }

            newPosition = (this.currentPosition - dist) % 100;
            if (newPosition < 0) {
                newPosition += 100;
            }

        } else if ("R".equals(order.getDirection())) {
            int distToZero = (this.currentPosition == 0) ? 100 : 100 - this.currentPosition;

            if (dist >= distToZero) {
                newZerosCount += 1 + (dist - distToZero) / 100;
            }

            newPosition = (this.currentPosition + dist) % 100;
        }

        return new Dial(newPosition, newZerosCount);
    }

    public long getZerosCount() {
        return zerosCount;
    }
}