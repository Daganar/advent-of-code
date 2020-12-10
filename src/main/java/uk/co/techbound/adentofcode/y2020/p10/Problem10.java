package uk.co.techbound.adentofcode.y2020.p10;

import com.google.common.collect.Iterables;
import lombok.extern.log4j.Log4j2;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.List;
import java.util.Objects;

@Log4j2
@Component
public class Problem10 extends AbstractProblemSolver<StreamEx<Long>, Long> {

    @Override
    protected Long partOne(StreamEx<Long> input) {
        List<Long> differences = input.pairMap((left, right) -> right - left)
            .sorted()
            .groupRuns(Object::equals)
            .map(List::size)
            .map(Long::valueOf)
            .toList();
        return Iterables.getLast(differences) * differences.iterator().next();
    }

    @Override
    protected Long partTwo(StreamEx<Long> input) {
        return input
            .pairMap((left, right) -> right - left)
            .groupRuns(Objects::equals)
            .removeBy(Iterables::getLast, 3L)
            .map(List::size)
            .map(this::getCombs)
            .mapToLong(Long::valueOf)
            .reduce(1, Math::multiplyExact);
    }

    int getCombs(int n) {
        if(n < 2) {
            return 1;
        } else if(n == 2) {
            return 2;
        } else {
            return getCombs(n - 1) + getCombs(n - 2) + getCombs(n - 3);
        }
    }

    @Override
    protected StreamEx<Long> convertInput(StreamEx<String> lines) {
        List<Long> longs = lines
            .map(Long::valueOf)
            .sorted()
            .toList();
        return StreamEx.of(longs).prepend(0L).append(Iterables.getLast(longs) + 3);
    }
}
