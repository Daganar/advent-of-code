package uk.co.techbound.adentofcode.y2020.p5;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.LongStreamEx;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.Set;

@Log4j2
@Component
public class Problem5 extends AbstractProblemSolver<LongStreamEx, Long> {

    @Override
    protected Long partOne(LongStreamEx input) {
        return input.max().getAsLong();
    }

    @Override
    protected Long partTwo(LongStreamEx input) {
        Set<Long> longs = input.boxed().toSet();
        return LongStreamEx.of(longs)
            .remove(l -> longs.contains(l - 1) && longs.contains(l + 1))
            .flatMap(l -> LongStreamEx.of(l - 1, l + 1).remove(longs::contains))
            .filter(l -> longs.contains(l - 1) && longs.contains(l + 1))
            .findAny()
            .getAsLong();
    }

    @Override
    protected LongStreamEx convertInput(StreamEx<String> lines) {
        return lines.mapToLong(line -> {
            String binaryString = line
                .replace('F', '0')
                .replace('B', '1')
                .replace('L', '0')
                .replace('R', '1');
            return Integer.parseUnsignedInt(binaryString, 2);
        });
    }

}
