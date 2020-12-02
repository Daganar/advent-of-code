package uk.co.techbound.adentofcode.utils;

import lombok.experimental.UtilityClass;
import one.util.streamex.IntStreamEx;

@UtilityClass
public class ArrayMathUtils {
    public static long multiply(int[] values) {
        return IntStreamEx.of(values).mapToLong(Long::valueOf).reduce((a, b) -> a*b).getAsLong();
    }

    public static int sum(int[] combinations) {
        return IntStreamEx.of(combinations).sum();
    }
}
