package uk.co.techbound.adentofcode.y2021.p1;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class Problem1 extends AbstractProblemSolver<List<Integer>, Long> {

    @Override
    protected Long partOne(List<Integer> lines) {
        return countGreaterThan(lines);
    }

    private long countGreaterThan(List<Integer> lines) {
        long count = 0;
        for (int i = 1; i < lines.size(); i++) {
            if(lines.get(i) > lines.get(i-1)) {
                count++;
            }
        }
        return count;
    }

    @Override
    protected Long partTwo(List<Integer> lines) {
        List<Integer> window = new ArrayList<>();
        for(int i = 0; i + 2 < lines.size(); i++) {
            window.add((lines.get(i) + lines.get(i + 1) + lines.get(i + 2)));
        }
        return countGreaterThan(window);
    }

    @Override
    protected List<Integer> convertInput(StreamEx<String> lines) {
        return lines.map(Integer::valueOf).toList();
    }
}
