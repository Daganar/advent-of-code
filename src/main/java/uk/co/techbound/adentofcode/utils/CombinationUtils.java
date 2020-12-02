package uk.co.techbound.adentofcode.utils;

import lombok.experimental.UtilityClass;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

import java.util.List;

@UtilityClass
public class CombinationUtils {

    public static <T> StreamEx<T[]> allCombinations(List<T> values, int length) {
        Class<T> clazz = (Class<T>) values.iterator().next().getClass();
        return StreamEx.ofCombinations(values.size(), length)
                .map(indexes -> IntStreamEx.of(indexes).mapToObj(values::get).toArray(clazz));
    }

    public static <T> StreamEx<int[]> allCombinationsOfIntegers(List<Integer> values, int length) {
        return StreamEx.ofCombinations(values.size(), length)
                .map(indexes -> IntStreamEx.of(indexes).map(values::get).toArray());
    }
}
