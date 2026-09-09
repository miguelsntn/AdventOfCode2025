package software.aoc.day02.b;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GiftShopDatabase {
    private final List<Range> ranges;

    private static final Pattern SILLY_PATTERN = Pattern.compile("^(.+)\\1+$");

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
                .filter(GiftShopDatabase::isRepeatedPattern)
                .sum();
    }

    public static boolean isRepeatedPattern(long id) {
        return SILLY_PATTERN.matcher(Long.toString(id)).matches();
    }
}