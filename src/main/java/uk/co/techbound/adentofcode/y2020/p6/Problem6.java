package uk.co.techbound.adentofcode.y2020.p6;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Log4j2
@Component
public class Problem6 extends AbstractProblemSolver<StreamEx<Set<Set<String>>>, Long> {

    @Override
    protected Long partOne(StreamEx<Set<Set<String>>> input) {
        return input
            .map(group -> StreamEx.of(group).flatMap(Collection::stream).toSet())
            .mapToLong(Collection::size).sum();
    }

    @Override
    protected Long partTwo(StreamEx<Set<Set<String>>> input) {
        return input.map(group ->
            StreamEx.of(group)
                .headTail((head, tail) -> {
                    Set<String> inCommon = new HashSet<>(head);
                    tail.forEach(inCommon::retainAll);
                    return inCommon.stream();
                })
            .toSet()
        )
        .mapToLong(Collection::size)
        .sum();
    }

    @Override
    protected StreamEx<Set<Set<String>>> convertInput(StreamEx<String> lines) {
        return lines.groupRuns((first, second) -> !second.trim().isEmpty())
            .map(list ->
                StreamEx.of(list)
                    .remove(String::isEmpty)
                    .map(line -> StreamEx.of(line.split("")).toSet())
                    .toSet()
            );
    }
}
