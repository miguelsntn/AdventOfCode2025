package software.aoc.day01.b;

import software.aoc.day01.Order;
import java.util.stream.Stream;

public class SafeDecoder {

    public static long decodePassword(Stream<String> document) {
        return document
                .filter(line -> !line.isBlank())
                .map(Order::fromString)
                .reduce(
                        Dial.createStartingAt(50),
                        Dial::apply,
                        (dial1, dial2) -> dial1
                )
                .zerosCount();
    }
}