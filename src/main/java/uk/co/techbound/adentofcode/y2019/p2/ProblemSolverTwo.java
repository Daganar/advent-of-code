package uk.co.techbound.adentofcode.y2019.p2;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;
import uk.co.techbound.adentofcode.ProblemName;

import java.util.Arrays;
import java.util.function.Function;

@Component
@Log4j2
@AllArgsConstructor
public class ProblemSolverTwo extends AbstractProblemSolver<int[], Integer> {

    private static final ProblemName PROBLEM_NAME = new ProblemName(2019, 2);

    private final OpcodeFunctions opcodeFunctions;

    public void setInitialState(int[] inputArray, int noun, int verb) {
        inputArray[1] = noun;
        inputArray[2] = verb;
    }

    void runProgram(int[] inputArray) {
        for(int i = 0; i < inputArray.length && inputArray[i] != 99; i += 4) {
            opcodeFunctions.getFunction(inputArray, i).accept(inputArray, i);
        }
    }

    @Override
    public ProblemName getProblemName() {
        return PROBLEM_NAME;
    }

    @Override
    protected Integer partOne(int[] input) {
        setInitialState(input, 12, 2);
        runProgram(input);

        log.info("Output: {}", input);
        return input[0];
    }

    @Override
    protected Integer partTwo(int[] input) {
        return
            IntStreamEx.range(100)
            .boxed()
            .flatMapToEntry(i -> IntStreamEx.range(i, 100).boxed().mapToEntry(Function.identity(), k -> i).toMap())
            .invert()
            .filterKeyValue((noun, verb) -> {
                int[] inputData = Arrays.copyOf(input, input.length);
                setInitialState(inputData, noun, verb);
                runProgram(inputData);
                return inputData[0] == 19690720;
            })
            .mapKeyValue((noun, verb) -> noun * 100 + verb)
            .findAny()
            .orElseThrow();
    }

    @Override
    protected int[] convertInput(StreamEx<String> lines) {
        return lines.flatArray(line -> line.split(","))
            .mapToInt(Integer::parseInt)
            .toArray();
    }
}
