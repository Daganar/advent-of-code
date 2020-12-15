package uk.co.techbound.adentofcode.y2020.p15;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Component
public class Problem15 extends AbstractProblemSolver<List<Long>, Long> {

    @Override
    protected Long partOne(List<Long> input) {
        return getNthNumber(input, 2020);
    }

    @Override
    protected Long partTwo(List<Long> input) {
        return getNthNumber(input, 30000000);
    }

    private Long getNthNumber(List<Long> input, int targetTurn) {
        Map<Long, Long> map = new HashMap<>();
        Long nextNumber = null;
        for (int turnNumber = 0; turnNumber < input.size(); turnNumber++) {
            nextNumber = map.put(input.get(turnNumber), (long) turnNumber);
        }
        nextNumber = calculateNewNumber(input.size() + 1, nextNumber);
        for(int turnNumber = input.size(); turnNumber < targetTurn - 1; turnNumber++) {
            Long previousIndex = map.put(nextNumber, (long) turnNumber);
            nextNumber = calculateNewNumber(turnNumber, previousIndex);
        }
        return nextNumber;
    }

    private Long calculateNewNumber(int turnNumber, Long previousIndex) {
        Long nextNumber;
        if(previousIndex == null) {
            nextNumber = 0L;
        } else {
            nextNumber = turnNumber - previousIndex;
        }
        return nextNumber;
    }

    @Override
    protected List<Long> convertInput(StreamEx<String> lines) {
        return lines.flatArray(line -> line.split(",")).map(Long::valueOf).toList();
    }
}
