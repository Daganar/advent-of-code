package uk.co.techbound.adentofcode.y2021.p6;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
@Component
public class Problem6 extends AbstractProblemSolver<Map<Integer, Long>, Long> {


    @Override
    protected Long partOne(Map<Integer, Long> input) {
        return getPopulationAfterXDays(input, 80);
    }

    @Override
    protected Long partTwo(Map<Integer, Long> input) {
        return getPopulationAfterXDays(input, 256);
    }

    private Long getPopulationAfterXDays(Map<Integer, Long> input, int days) {
        for (int day = 0; day < days; day++) {
            for (int fishAge = 0; fishAge < 10; fishAge++) {
                Long currentCount = input.remove(fishAge);
                if (currentCount != null) {
                    if (fishAge == 0) {
                        input.put(9, currentCount);
                        input.merge(7, currentCount, Long::sum);
                    } else {
                        input.merge(fishAge - 1, currentCount, Long::sum);
                    }
                }
            }
        }
        return EntryStream.of(input).values().mapToLong(i -> i).sum();
    }

    @Override
    protected Map<Integer, Long> convertInput(StreamEx<String> lines) {
        return lines.flatArray(line -> line.split(",")).map(Integer::parseInt).groupingBy(Function.identity(), Collectors.counting());
    }
}
