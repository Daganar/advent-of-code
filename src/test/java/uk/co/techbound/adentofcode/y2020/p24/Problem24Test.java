package uk.co.techbound.adentofcode.y2020.p24;

import org.apache.commons.lang3.tuple.Pair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Problem24Test {

    @Test
    public void getNeighboursEven() {
        List<Pair<Integer, Integer>> neighbours = new Problem24().getNeighbours(4, 2);
        List<Pair<Integer, Integer>> expected = List.of(
            Pair.of(4, 1),
            Pair.of(5, 1),
            Pair.of(5, 2),
            Pair.of(4, 3),
            Pair.of(3, 2),
            Pair.of(3, 1)
        );
        MatcherAssert.assertThat(neighbours, Matchers.containsInAnyOrder(expected.toArray(new Object[0])));
    }

    @Test
    public void getNeighboursOdd() {
        List<Pair<Integer, Integer>> neighbours = new Problem24().getNeighbours(3, 2);
        List<Pair<Integer, Integer>> expected = List.of(
            Pair.of(3, 1),
            Pair.of(4, 2),
            Pair.of(4, 3),
            Pair.of(3, 3),
            Pair.of(2, 3),
            Pair.of(2, 2)
        );
        MatcherAssert.assertThat(neighbours, Matchers.containsInAnyOrder(expected.toArray(new Object[0])));
    }
}