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
public class Problem1 extends AbstractProblemSolver {

    private void solveCombinations(List<Integer> values, int length) {
        CombinationUtils.allCombinationsOfIntegers(values, length)
            .filter(combinations -> ArrayMathUtils.sum(combinations) == 2020)
            .forEach(combinations -> log.info("Values: {}, Product: {}", combinations, ArrayMathUtils.multiply(combinations)));
    }

    @Override
    public void solve(List<String> problemArguments) {
        StreamEx<String> lines = getLinesOfProblemInput();
        List<Integer> values = lines.map(Integer::valueOf).toList();

        log.info("Solving part one");
        solveCombinations(values, 2);

        log.info("Solving part two");
        solveCombinations(values, 3);
    }
}
