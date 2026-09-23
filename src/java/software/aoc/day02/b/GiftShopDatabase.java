package software.aoc.day02.b;

import software.aoc.day02.Range;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public record GiftShopDatabase(List<Range> ranges) {

    private static final Pattern SILLY_PATTERN = Pattern.compile("^(.+)\\1+$");

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
                .filter(GiftShopDatabase::isRepeatedPattern)
                .sum();
    }

    private static boolean isRepeatedPattern(long id) {
        return SILLY_PATTERN.matcher(Long.toString(id)).matches();
    }
}