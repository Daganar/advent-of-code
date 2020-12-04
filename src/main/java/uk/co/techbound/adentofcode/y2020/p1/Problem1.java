package uk.co.techbound.adentofcode.y2020.p1;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;
import uk.co.techbound.adentofcode.utils.ArrayMathUtils;
import uk.co.techbound.adentofcode.utils.CombinationUtils;

import java.util.List;

@Log4j2
@Component
public class Problem1 extends AbstractProblemSolver<List<Integer>> {

    private long solveCombinations(List<Integer> values, int length) {
        int[] solution = CombinationUtils.allCombinationsOfIntegers(values, length)
                .findAny(combinations -> ArrayMathUtils.sum(combinations) == 2020)
                .orElseThrow();
        log.debug("Values: {}", solution);
        return ArrayMathUtils.multiply(solution);
    }

    @Override
    protected Object partOne(List<Integer> lines) {
        return solveCombinations(lines, 2);
    }

    @Override
    protected Object partTwo(List<Integer> lines) {
        return solveCombinations(lines, 3);
    }

    @Override
    protected List<Integer> convertInput(StreamEx<String> lines) {
        return lines.map(Integer::valueOf).toList();
    }
}
