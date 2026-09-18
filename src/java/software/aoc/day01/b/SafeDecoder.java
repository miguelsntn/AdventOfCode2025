package software.aoc.day01.b;

import java.util.stream.Stream;

public class SafeDecoder {

    public static long decodePassword(Stream<String> document) {
        Dial currentDial = Dial.createStartingAt(50);

        Iterable<String> lines = document::iterator;

        for (String line : lines) {
            if (!line.isBlank()) {
                Order order = Order.fromString(line);
                currentDial = currentDial.applyOrder(order);
            }
        }

        return currentDial.getZerosCount();
    }
}