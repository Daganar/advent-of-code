package uk.co.techbound.adentofcode.y2020.p8;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.EntryStream;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Log4j2
@Component
public class Problem8 extends AbstractProblemSolver<List<Pair<String, Integer>>, Long> {

    Set<String> NOP_AND_JMP = Set.of("nop", "jmp");

    @Override
    protected Long partOne(List<Pair<String, Integer>> input) {
        AtomicLong global = new AtomicLong(0);
        attemptToExecute(input, global);
        return global.get();
    }

    @Override
    protected Long partTwo(List<Pair<String, Integer>> input) {
        return
            IntStreamEx.range(input.size()).
                filter(i -> NOP_AND_JMP.contains(input.get(i).getLeft()))
                .mapToObj(index -> permeateOperationAtIndex(input, index))
                .map(program -> {
                    AtomicLong global = new AtomicLong(0);
                    boolean ranSuccessfully = attemptToExecute(program, global);
                    return Pair.of(ranSuccessfully, global.get());
                })
                .filter(Pair::getLeft)
                .mapToLong(Pair::getRight)
                .findAny()
                .getAsLong();
    }

    public List<Pair<String, Integer>> permeateOperationAtIndex(List<Pair<String, Integer>> input, int index) {
        return EntryStream.of(input)
            .mapToValue((i, line) -> {
                if(i == index) {
                    String operand = line.getLeft();
                    return Pair.of(operand.equals("nop") ? "jmp" : "nop", line.getRight());
                }
                return line;
            })
            .values()
            .toList();
    }


    public boolean attemptToExecute(List<Pair<String, Integer>> input, AtomicLong global) {
        Set<Integer> visitedLines = new HashSet<>();
        int currentLineNumber = 0;
        while (currentLineNumber < input.size()) {
            if (visitedLines.contains(currentLineNumber)) {
                return false;
            }
            visitedLines.add(currentLineNumber);
            Pair<String, Integer> currentLine = input.get(currentLineNumber);
            String currentOperand = currentLine.getLeft();
            Integer currentArgument = currentLine.getRight();
            switch (currentOperand) {
                case "nop":
                    break;
                case "jmp":
                    currentLineNumber += currentArgument;
                    continue;
                case "acc":
                    global.addAndGet(currentArgument);
                    break;
            }
            currentLineNumber++;
        }
        return true;
    }

    @Override
    protected List<Pair<String, Integer>> convertInput(StreamEx<String> lines) {
        return lines
            .map(line -> line.split(" "))
            .map(split -> {
                int value = Integer.parseInt(split[1]);
                return Pair.of(split[0], value);
            })
            .toList();
    }
}
