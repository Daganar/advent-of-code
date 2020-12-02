package uk.co.techbound.adentofcode.y2019.p2;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.IntStreamEx;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Function;

@Log4j2
@SpringBootTest
class ProblemSolverTwoTest {

    @Autowired
    ProblemSolverTwo problemSolverTwo;

    @Test
    void performOperationOne() {
        int[] inputArray = {1, 0, 0, 0, 99};
        problemSolverTwo.performOperation(0, inputArray);
        Assertions.assertArrayEquals(new int[] {2,0,0,0,99}, inputArray);
    }

    @Test
    void performOperationTwo() {
        int[] inputArray = {2,3,0,3,99};
        problemSolverTwo.performOperation(0, inputArray);
        Assertions.assertArrayEquals(new int[] {2,3,0,6,99}, inputArray);
    }

    @Test
    void performOperationThree() {
        int[] inputArray = {2,4,4,5,99,0};
        problemSolverTwo.performOperation(0, inputArray);
        Assertions.assertArrayEquals(new int[] {2,4,4,5,99,9801}, inputArray);
    }

    @Test
    void performOperationFour() {
        int[] inputArray = {1,1,1,4,99,5,6,0,99};
        problemSolverTwo.runProgram(inputArray);
        Assertions.assertArrayEquals(new int[] {30,1,1,4,2,5,6,0,99}, inputArray);
    }

    @Test
    void calculateNounAndVerb() {
        IntStreamEx.range(100)
            .boxed()
            .flatMapToEntry(i -> IntStreamEx.range(i, 100).boxed().mapToEntry(Function.identity(), k -> i).toMap())
            .invert()
            .mapKeyValue((noun, verb) -> {
                int[] inputData = problemSolverTwo.getInputData();
                problemSolverTwo.setInitialState(inputData, noun, verb);
                problemSolverTwo.runProgram(inputData);
                if(inputData[0] == 19690720) {
                    log.info("Noun: {}, Verb: {}", noun, verb);
                    return true;
                }
                return false;
            })
            .findAny(Boolean::booleanValue);

    }
}