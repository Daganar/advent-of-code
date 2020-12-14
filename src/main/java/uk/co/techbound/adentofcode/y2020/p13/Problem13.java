package uk.co.techbound.adentofcode.y2020.p13;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.Arrays;
import java.util.List;

@Log4j2
@Component
public class Problem13 extends AbstractProblemSolver<Pair<Integer, List<String>>, Long> {

    @Override
    protected Long partOne(Pair<Integer, List<String>> instructions) {
        Integer earliest = instructions.getLeft();
        int earliestBus = StreamEx.of(instructions.getRight())
            .filter(NumberUtils::isCreatable)
            .mapToInt(Integer::parseInt)
            .minBy(bus -> getWaitTime(earliest, bus))
            .getAsInt();

        return getWaitTime(earliest, earliestBus) * earliestBus;
    }

    private long getWaitTime(long timestamp, long bus) {
        return (bus - (timestamp % bus)) % bus;
    }

    @Override
    protected Long partTwo(Pair<Integer, List<String>> instructions) {
        return alignNext(instructions.getRight(), 0, 1, 1);
    }

    private long alignNext(List<String> rules, int currentIndex, long currentTimestamp, long currentIncrement) {
        if(currentIndex == rules.size()) {
            return currentTimestamp;
        }
        if(rules.get(currentIndex).equals("x")) {
            return alignNext(rules, currentIndex+1, currentTimestamp, currentIncrement);
        }
        long currentBus = Long.parseLong(rules.get(currentIndex));
        while(getWaitTime(currentTimestamp + currentIndex, currentBus) != 0) {
            currentTimestamp += currentIncrement;
        }
        return alignNext(rules, currentIndex + 1, currentTimestamp, currentIncrement * currentBus);
    }

    @Override
    protected Pair<Integer, List<String>> convertInput(StreamEx<String> lines) {
        List<String> inputLines = lines.toList();
        Integer earliest = Integer.valueOf(inputLines.get(0));
        String[] split = inputLines.get(1).split(",");
        return Pair.of(earliest, Arrays.asList(split));
    }
}
