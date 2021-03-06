package uk.co.techbound.adentofcode.y2020.p1;

import uk.co.techbound.adentofcode.AbstractProblemSolver;
import uk.co.techbound.adentofcode.AbstractProblemTest;


public class Problem1Test<T> extends AbstractProblemTest<T, Long> {

    @Override
    protected Long expectedPartOne() {
        return 514579L;
    }

    @Override
    protected Long expectedPartTwo() {
        return 241861950L;
    }

    @Override
    public AbstractProblemSolver underTest() {
        return new Problem1();
    }
}