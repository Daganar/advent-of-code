package uk.co.techbound.adentofcode.y2021.p6;

import uk.co.techbound.adentofcode.AbstractProblemSolver;
import uk.co.techbound.adentofcode.AbstractProblemTest;

import java.util.Map;

public class Problem6Test extends AbstractProblemTest<Map<Integer, Long>, Long> {

    @Override
    protected Long expectedPartOne() {
        return 5934L;
    }

    @Override
    protected Long expectedPartTwo() {
        return 26984457539L;
    }

    @Override
    public AbstractProblemSolver<Map<Integer, Long>, Long> underTest() {
        return new Problem6();
    }
}