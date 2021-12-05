package uk.co.techbound.adentofcode.y2021.p3;

import uk.co.techbound.adentofcode.AbstractProblemSolver;
import uk.co.techbound.adentofcode.AbstractProblemTest;

import java.util.List;

public class Problem3Test extends AbstractProblemTest<List<String>, Long> {

    @Override
    protected Long expectedPartOne() {
        return 198L;
    }

    @Override
    protected Long expectedPartTwo() {
        return 230L;
    }

    @Override
    public AbstractProblemSolver<List<String>, Long> underTest() {
        return new Problem3();
    }
}