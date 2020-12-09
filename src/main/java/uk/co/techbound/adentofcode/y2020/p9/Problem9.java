package uk.co.techbound.adentofcode.y2020.p9;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.LongStreamEx;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.List;
import java.util.LongSummaryStatistics;

@Log4j2
@Component
public class Problem9 extends AbstractProblemSolver<List<Long>, Long> {

    int preambleSize = 25;

    @Override
    protected Long partOne(List<Long> input) {
        int preamblestart = 0;
        int preambleEnd = 0;
        do {
        preambleEnd = preamblestart + preambleSize;
        List<Long> preamble = input.subList(preamblestart, preambleEnd);
            Long currentValue = input.get(preambleEnd);
            if(!canMakeFromPreamble(preamble, currentValue)) {
                return currentValue;
            }
            preamblestart++;
        } while(preambleEnd < input.size());

        return null;
    }

    private boolean canMakeFromPreamble(List<Long> preamble, Long currentValue) {
        for(Long first : preamble) {
            for(Long second : preamble) {
                if(first + second == currentValue && !first.equals(second)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected Long partTwo(List<Long> input) {
        long targetSum = partOne(input);
        for(int start = 0; start < input.size(); start++) {
            for(int end = start + 1; end < input.size(); end++) {
                LongSummaryStatistics summaryStatistics = LongStreamEx.of(input.subList(start, end)).summaryStatistics();
                if(summaryStatistics.getSum() == targetSum) {
                    return summaryStatistics.getMax() + summaryStatistics.getMin();
                }
            }
        }
        return null;
    }

    @Override
    protected List<Long> convertInput(StreamEx<String> lines) {
        return lines
            .map(Long::valueOf)
            .toList();
    }
}
