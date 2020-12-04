package uk.co.techbound.adentofcode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public abstract class AbstractProblemTest<T,U> {

    public abstract AbstractProblemSolver<T,U> underTest();

    protected U partOneExample() {
        AbstractProblemSolver<T, U> problemSolver = underTest();
        T exampleInput = problemSolver.convertInput(problemSolver.getLinesOfProblemInput("example.txt"));
        return problemSolver.partOne(exampleInput);
    }

    protected U partTwoExample() {
        AbstractProblemSolver<T, U> problemSolver = underTest();
        T exampleInput = problemSolver.convertInput(problemSolver.getLinesOfProblemInput("example.txt"));
        return problemSolver.partTwo(exampleInput);
    }

    protected U expectedPartOne() {
        return null;
    };

    protected U expectedPartTwo() {
        return null;
    }

    @Test
    void partOne() {
        U actual = partOneExample();
        Assertions.assertEquals(expectedPartOne(), actual);
    }

    @Test
    void partTwo() {
        U actual = partTwoExample();
        Assertions.assertEquals(expectedPartTwo(), actual);
    }
}
