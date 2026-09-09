package software.aoc.day01.a;

public class Dial {
    private final int currentPosition;
    private final int zerosCount;

    private Dial(int currentPosition, int zerosCount) {
        this.currentPosition = currentPosition;
        this.zerosCount = zerosCount;
    }

    public static Dial createStartingAt(int position) {
        return new Dial(position, 0);
    }

    public Dial applyOrder(Order order) {
        int newPosition = this.currentPosition;

        if ("L".equals(order.getDirection())) {
            newPosition = (newPosition - order.getDistance()) % 100;
            if (newPosition < 0) {
                newPosition += 100;
            }
        } else if ("R".equals(order.getDirection())) {
            newPosition = (newPosition + order.getDistance()) % 100;
        }

        int newZerosCount = this.zerosCount + (newPosition == 0 ? 1 : 0);
        return new Dial(newPosition, newZerosCount);
    }

    public int getZerosCount() {
        return zerosCount;
    }
}