package uk.co.techbound.adentofcode.y2020.p25;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.LongStreamEx;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

@Log4j2
@Component
public class Problem25 extends AbstractProblemSolver<Pair<Long, Long>, Long> {

    @Override
    protected Long partOne(Pair<Long, Long> input) {
        long loopSizeCard = calculateLoopNumber(input.getLeft());
        long loopSizeDoor = calculateLoopNumber(input.getRight());
        log.info("card: {}, door: {}", loopSizeCard, loopSizeDoor);
        return transformSubjectNumber(input.getLeft(), loopSizeDoor);
    }

    public long calculateLoopNumber(long expected) {
        long currentValue = 1;
        long loopSize = 0;
        while(currentValue != expected) {
            currentValue = getNextValue(currentValue, 7);
            loopSize++;
        }
        return loopSize;
    }

    public long transformSubjectNumber(long subjectNumber, long loopCount) {
        return LongStreamEx.iterate(1L, l -> getNextValue(l, subjectNumber))
            .limit(loopCount + 1)
            .reduce((l, r) -> r)
            .getAsLong();
    }


    private long getNextValue(long currentValue, long subjectNumber) {
        currentValue *= subjectNumber;
        currentValue = currentValue % 20201227;
        return currentValue;
    }

    @Override
    protected Long partTwo(Pair<Long, Long> input) {
        return null;
    }

    @Override
    protected Pair<Long, Long> convertInput(StreamEx<String> lines) {
        long[] longs = lines.mapToLong(Long::parseLong).toArray();
        return Pair.of(longs[0], longs[1]);
    }
}
