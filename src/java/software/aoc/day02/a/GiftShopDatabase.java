package software.aoc.day02.a;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GiftShopDatabase {
    private final List<Range> ranges;

    private GiftShopDatabase(List<Range> ranges) {
        this.ranges = ranges;
    }

    public static GiftShopDatabase from(String rawRanges) {
        if (rawRanges == null || rawRanges.isBlank()) {
            return new GiftShopDatabase(List.of());
        }

        List<Range> parsedRanges = Arrays.stream(rawRanges.split(","))
                .map(Range::from)
                .collect(Collectors.toList());

        return new GiftShopDatabase(parsedRanges);
    }

    public long sumInvalidIds() {
        return ranges.stream()
                .flatMapToLong(Range::expandToSequence)
                .filter(GiftShopDatabase::isTwiceRepeatedPattern)
                .sum();
    }

    public static boolean isTwiceRepeatedPattern(long id) {
        String idString = Long.toString(id);
        int length = idString.length();

        if (length % 2 != 0) {
            return false;
        }

        int mid = length / 2;
        String firstHalf = idString.substring(0, mid);
        String secondHalf = idString.substring(mid);

        return firstHalf.equals(secondHalf);
    }
}