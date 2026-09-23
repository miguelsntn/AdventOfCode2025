package software.aoc.day02.a;

import software.aoc.day02.Range;
import java.util.Arrays;
import java.util.List;

public record GiftShopDatabase(List<Range> ranges) {

    public static GiftShopDatabase from(String rawRanges) {
        if (rawRanges == null || rawRanges.isBlank()) {
            return new GiftShopDatabase(List.of());
        }

        List<Range> parsedRanges = Arrays.stream(rawRanges.split(","))
                .map(Range::from)
                .toList();

        return new GiftShopDatabase(parsedRanges);
    }

    public long sumInvalidIds() {
        return ranges.stream()
                .flatMapToLong(Range::stream)
                .filter(GiftShopDatabase::isTwiceRepeatedPattern)
                .sum();
    }

    private static boolean isTwiceRepeatedPattern(long id) {
        String idString = Long.toString(id);
        int length = idString.length();

        if (length % 2 != 0) {
            return false;
        }

        int mid = length / 2;
        return idString.substring(0, mid).equals(idString.substring(mid));
    }
}