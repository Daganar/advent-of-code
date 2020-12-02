package uk.co.techbound.adentofcode.utils;

import lombok.experimental.UtilityClass;

import java.util.concurrent.TimeUnit;

@UtilityClass
public class TimerUtils {
    public static long timeIt(Runnable runnable) {
        long start = System.nanoTime();
        runnable.run();
        long duration = System.nanoTime() - start;
        return TimeUnit.NANOSECONDS.toMillis(duration);
    }
}
