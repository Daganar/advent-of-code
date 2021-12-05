package uk.co.techbound.adentofcode.utils;

import lombok.experimental.UtilityClass;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@UtilityClass
public class StreamUtils {

    public static <T> Function<StreamEx<T>, StreamEx<List<T>>> groupRuns(int size) {
        AtomicInteger count = new AtomicInteger(0);
        return stream -> stream.groupRuns((l, r) -> count.incrementAndGet() % size != 0);

    }
}
