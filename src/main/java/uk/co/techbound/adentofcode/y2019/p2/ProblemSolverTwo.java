package uk.co.techbound.adentofcode.y2019.p2;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import one.util.streamex.StreamEx;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.ProblemName;
import uk.co.techbound.adentofcode.ProblemSolver;

import java.io.IOException;
import java.io.UncheckedIOException;

@Component
@Log4j2
@AllArgsConstructor
public class ProblemSolverTwo implements ProblemSolver {

    private static final ProblemName PROBLEM_NAME = new ProblemName(2019, 2);

    private final OpcodeFunctions opcodeFunctions;

    @Override
    public void solve() {
        int[] inputArray = getInputData();
        setInitialState(inputArray, 12, 2);
        runProgram(inputArray);

        log.info("Output: {}", inputArray);
    }

    public void setInitialState(int[] inputArray, int noun, int verb) {
        inputArray[1] = noun;
        inputArray[2] = verb;
    }

    public int[] getInputData() {
        try {
            return StreamEx.ofLines(new ClassPathResource("problems/2/input.txt").getFile().toPath())
                .flatArray(line -> line.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    void runProgram(int[] inputArray) {
        for(int i = 0; i < inputArray.length && inputArray[i] != 99; i += 4) {
            opcodeFunctions.getFunction(inputArray, i).accept(inputArray, i);
        }
    }

    public void performOperation(int i, int[] inputArray) {
        int operation = inputArray[i];
        int operandA = inputArray[inputArray[i + 1]];
        int operandB = inputArray[inputArray[i + 2]];
        int outputIndex = inputArray[i + 3];
        inputArray[outputIndex] = operation == 1 ? operandA + operandB : operandA * operandB;
    }

    @Override
    public ProblemName getProblemName() {
        return PROBLEM_NAME;
    }
}
