package uk.co.techbound.adentofcode.y2021.p2;

import uk.co.techbound.adentofcode.AbstractProblemSolver;
import uk.co.techbound.adentofcode.AbstractProblemTest;

import java.util.List;

public class Problem2Test extends AbstractProblemTest<List<String>, Long> {

    @Override
    protected Long expectedPartOne() {
        return 150L;
    }

    @Override
    protected Long expectedPartTwo() {
        return 900L;
    }

    @Override
    public AbstractProblemSolver<List<String>, Long> underTest() {
        return new Problem2();
    }
}