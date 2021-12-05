package uk.co.techbound.adentofcode.utils;

import lombok.experimental.UtilityClass;
import one.util.streamex.LongStreamEx;

@UtilityClass
public class MathFunctions {

    public static long multiply(long a, long b) {
        return a * b;
    }

    public static long product(LongStreamEx stream) {
        return stream.reduce(1, MathFunctions::multiply);
    }
}
